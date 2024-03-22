package com.snowalker.shardingjdbc.snowalker.demo.reds.delay;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

/**
 * @Method_Name : 任务执行管理
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
@Slf4j
public class TaskManager {

    private TaskManager() {
    }

    /**
     * 创建一个可重用固定线程数的线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 异步执行任务
     *
     * @param task  任务
     * @param title 标题
     */
    public static void doTask(final ITask task, String title) {
        executorService.execute(() -> SpeedTimeLogSuit.wrap((Supplier<Void>) () -> {
            try {
                task.doTask();
            } catch (Exception e) {
                log.error("TaskManager doTask execute error.", e);
            }
            return null;
        }, title));
    }

    /**
     * 带有返回值的task
     *
     * @param task  任务
     * @param title 标题
     */
    public static <T> FutureTask<T> doFutureTask(final IFutureTask<T> task, String title) {
        FutureTask<T> future = new FutureTask<T>(() -> SpeedTimeLogSuit.wrap((task::doTask), title));
        executorService.execute(future);
        return future;
    }
}
