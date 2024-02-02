package com.webchat.service.queue.dto;

import com.webchat.common.util.IDGenerateUtil;
import lombok.Data;

/**
 * Redis数据传输实体
 */
@Data
public class BaseQueueDTO {

    /**
     * 任务ID
     */
    private String taskId = IDGenerateUtil.uuid();

    /**
     * 被执行次数
     */
    private int retryTimes;

    /**
     * 重回队列的最大次数
     */
    private int backQueueTimes;
}