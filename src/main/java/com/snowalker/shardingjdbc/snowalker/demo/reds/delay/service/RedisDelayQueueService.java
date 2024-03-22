package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service;

import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.Task;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.TaskDie;

import java.util.List;

/**
 * @Method_Name : 任务操作类
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
public interface RedisDelayQueueService {

    /**
     * 添加task元信息
     *
     * @param task 元信息
     */
    void addTask(Task task);


    /**
     * 删除task信息
     *
     * @param taskDie
     */
    void deleteTask(TaskDie taskDie);


    /**
     * 批量添加task
     * @param tasks
     */
    void addTasks(List<Task> tasks);
}
