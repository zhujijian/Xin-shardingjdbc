package com.snowalker.shardingjdbc.snowalker.demo.reds.enrity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 业务类型
 */
@Data
public class TaskDie implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  任务id
     */
    @NotBlank
    private String taskId;


    /**
     * 业务类型
     */
    @NotBlank
    private String topic;
}
