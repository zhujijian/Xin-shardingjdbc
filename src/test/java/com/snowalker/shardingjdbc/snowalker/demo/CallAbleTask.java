package com.snowalker.shardingjdbc.snowalker.demo;

import java.util.concurrent.*;

public class CallAbleTask implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 1;
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newCachedThreadPool();
        //使用FutureTask
        Callable<Integer> task = new CallAbleTask();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        executor.submit(futureTask);

        //使用Future
        Callable<Integer> call = new CallAbleTask();
        Future<Integer> future = executor.submit(call);



        executor.shutdown();
        System.out.println("主线程在执行任务");
        Thread.sleep(2000);
        try {
            System.out.println(future.get());
            System.out.println("task运行结果" + futureTask.get()); //future.get()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("所有任务执行完毕");
    }


}
