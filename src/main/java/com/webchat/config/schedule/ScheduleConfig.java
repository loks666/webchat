package com.webchat.config.schedule;

import com.webchat.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private RedisService redisService;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void refreshHotArticleCacheTask() {
//        String requestId = IDGenerateUtil.createId("TASK");
//        RedisKeyEnum redisConfEnum = RedisKeyEnum.REFRESH_HOT_ARTICLE_CACHE_TASK_LOCK;
//        Long begin = System.currentTimeMillis();
//        log.info("refreshHotArticleCacheTask start....");
//        try {
//            if (redisService.installDistributedLock(redisConfEnum.getKey(), requestId, redisConfEnum.getExpireTime())) {
//
//                articleCacheService.refreshHotArticleCache();
//
//                Long end = System.currentTimeMillis();
//                log.info("refreshHotArticleCacheTask success, cost time:{}", (end - begin));
//            } else {
//                log.info("refreshHotArticleCacheTask, lock is invalid");
//            }
//        } catch (Exception e) {
//            log.error("refreshHotArticleCacheTask err,cost time:{}", (System.currentTimeMillis() - begin), e);
//        } finally {
//            redisService.releaseDistributedLock(redisConfEnum.getKey(), requestId);
//        }
    }
}
