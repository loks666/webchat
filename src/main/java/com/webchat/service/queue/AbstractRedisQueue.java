package com.webchat.service.queue;

import com.webchat.common.util.JsonUtil;
import com.webchat.service.RedisService;
import com.webchat.service.queue.dto.BaseQueueDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的redis队列实现方法
 *
 * @param <T>
 */
@Slf4j
public abstract class AbstractRedisQueue<T extends BaseQueueDTO> {

    @Autowired
    protected RedisService redisService;

    private volatile boolean isInit = false;

    private volatile boolean isRun = true;

    private ThreadPoolExecutor poolExecutor;

    public void init() {
        if (isInit) {
            return;
        }
        log.info("开始初始化消息队列，参数如下: \n" +
                        "queue name: {} \n" +
                        "queue timeout: {}\n" +
                        "queue retry times: {}\n" +
                        "queue back queue times: {}\n" +
                        "queue pool size: {}",
                getQueueName(), getTimeout(), getRetryTimes(), getBackQueueTimes(), getPoolSize()
        );
        initThreadPool(getPoolSize());
        Long startSize = redisService.lsize(getQueueName());
        log.info("启动消息队列{}成功, 目前还有 {} 个元素没被消费", getQueueName(), startSize);
        isInit = true;
    }

    /**
     * 向消息队列加入消息
     *
     * @param data
     */
    public void submit(T data) {
        String jsonStr = JsonUtil.toJsonString(data);
        redisService.lleftPush(getQueueName(), jsonStr);
    }

    /**
     * 从消息队列redis读取数据的延迟
     *
     * @return
     */
    protected long getTimeout() {
        return 3000;
    }

    /**
     * 一个任务元素重试的次数
     *
     * @return 默认不重试
     */
    protected int getRetryTimes() {
        return 0;
    }

    /**
     * 一个任务最多被重返队列的次数
     *
     * @return
     */
    protected int getBackQueueTimes() {
        return 0;
    }

    /**
     * 获取最大执行任务数量
     *
     * @return
     */
    protected int getPoolSize() {
        return 1;
    }

    /**
     * 当一个服务不是主服务的时候，需要等待下次检测的时间间隔
     * 默认20s
     *
     * @return
     */
    protected int getMasterWaitTime() {
        return 20000;
    }

    /**
     * 将字符串转化为对象
     *
     * @param s
     * @return
     */
    protected abstract T convert(String s);

    /**
     * 队列的名称
     *
     * @return
     */
    protected abstract String getQueueName();

    /**
     * 监听到数据
     *
     * @param data
     */
    protected abstract void receive(T data);

    /**
     * 监听到错误
     *
     * @param data
     * @param ex
     */
    protected abstract void error(T data, Exception ex);


    /**
     * 重试逻辑
     *
     * @param data
     * @return
     */
    protected boolean retry(T data) {
        if (data == null) {
            return false;
        }
        if (data.getRetryTimes() <= 0) {
            return false;
        }
        for (int i = 1; i <= getRetryTimes(); ++i) {
            data.setRetryTimes(i);
            try {
                log.info("{}开始执行第{}次重试", data.getClass(), i);
                receive(data);
                return true;
            } catch (Exception ex) {
                log.error("队列任务执行失败, 重试失败第{}次", i);
            }
        }
        return false;
    }

    /**
     * 将数据重新加入队列中
     *
     * @param data
     * @return true: 成功  false: 失败
     */
    protected boolean backToQueue(T data) {
        if (data == null) {
            return false;
        }
        if (data.getBackQueueTimes() < getBackQueueTimes()) {
            log.error("数据重试失败，开始重新放入队列，已归队次数 {}", data.getBackQueueTimes());
            data.setBackQueueTimes(data.getBackQueueTimes() + 1);
            submit(data);
            return true;
        }
        return false;
    }

    /**
     * 负责调度的核心方法
     * 通过循环读取redis的阻塞队列，获取元素，并且转化为用户可直接操作的类型
     * 如果处理失败采用两种容错方式:
     * 1. 重试，可以通过设置retryTime来设置重试次数
     * 2. 重新归队，只有在重试全部失败后才回执行该逻辑，可以设置backQueueTimes设置归队的最大次数
     * 如果两种方式都失败，则进入用户自定义的容错方式中
     */
    public void schedule() {
        init();
        try {
            final Semaphore semaphore = new Semaphore(getPoolSize());
            while (isRun) {
                // 判断是否目前达到了使用上限
                log.info("QueueName:{} 信号量剩余:{}", getQueueName(), semaphore.availablePermits());
                semaphore.acquire();

                final String s = redisService.lrightPop(getQueueName(), getTimeout(), TimeUnit.MILLISECONDS);
                if (StringUtils.isEmpty(s)) {
                    log.info("QueueName:{} 目前没有元素", getQueueName());
                    semaphore.release();
                    continue;
                }
                poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        T data = null;
                        try {
                            data = convert(s);
                            receive(data);
                        } catch (Exception ex) {
                            try {
                                // 先让任务重试
                                if (!retry(data)) {
                                    // 任务重试失败，判断元素是否有重新加入队列的资格
                                    if (!backToQueue(data)) {
                                        error(data, ex);
                                    }
                                }
                            } catch (Exception innerEx) {
                                log.error("消费队列异常捕获失败，原因: ", innerEx);
                            }
                        } finally {
                            // 任务执行完释放信号量
                            semaphore.release();
                        }
                    }
                });
            }

        } catch (final Exception outerEx) {
            log.error("启动消息队列失败, 原因 ", outerEx);
        }
    }

    /**
     * 初始化数据库连接池
     *
     * @param poolSize
     */
    private void initThreadPool(int poolSize) {
        if (poolExecutor == null) {
            synchronized (this) {
                if (poolExecutor == null) {
                    poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
                }
            }
        }
    }
}
