package com.snowalker.shardingjdbc.snowalker.demo.reds.enrity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 任务id
 */
@Data
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * taskId 唯一标示
     */
    @NotBlank
    private String taskId;


    /**
     * 业务类型
     */
    @NotBlank
    private String topic;

    /**
     * 延迟时间  单位  时间戳
     */
    private Long delay;

    /**
     * 消息体
     */
    @NotBlank
    private String body;

    /**
     * 重试次数
     */
    private int retry = 0;

}
