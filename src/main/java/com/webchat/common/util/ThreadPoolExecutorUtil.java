package com.webchat.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by by loks666 on GitHub on 2021/12/3.
 */
@Slf4j
public class ThreadPoolExecutorUtil {

    /**
     * 核心线程数
     */
    private static int corePoolSize = 20;

    /**
     * 最大线程数
     */
    private static int maximumPoolSize = corePoolSize * 5;

    /**
     * 空闲等待时间
     */
    private static long keepAliveTime = 60;

    /**
     * 等待队列大小
     */
    private static int queueSize = 1024 * 2;

    /**
     * 如果poolSize < coreSize, 线程优先加入corePool中;
     * 如果poolSize >= coreSize, 线程加入到queue中;
     * 如果queue满了, 就创建新线程, 直到maxPool大小;
     * 如果poolSize > coreSize, 大于部分的线程会在keepAliveTime没有接到工作任务后销毁。
     **/
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 执行task
     *
     * @param task
     */
    public static void execute(Runnable task) {
        pool.execute(task);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    /**
     * 线程池状态
     */
    public static void printStatus() {
        int active = pool.getActiveCount();
        int queue = pool.getQueue().size();
        long complete = pool.getCompletedTaskCount();
        long task = pool.getTaskCount();
        if (active > 0 || queue > 0) {
            log.debug("[busy] ThreadPool active:" + active + ",queue:" + queue + ",complete:" + complete + ",task:"
                    + task);
        } else {
            log.debug("[free] ThreadPool is empty. active:" + active + ",queue:" + queue + ",complete:" + complete
                    + ",task:" + task);
        }
    }
}

