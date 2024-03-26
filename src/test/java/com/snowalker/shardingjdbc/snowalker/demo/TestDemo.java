package com.snowalker.shardingjdbc.snowalker.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestDemo {

    public static void main(String[] args) {

/*        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> rFuture = CompletableFuture
                .runAsync(() -> System.out.println("hello siting"), executor);
        //supplyAsync的使用
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.print("hello ");
                    return "siting";
                }, executor);

        //阻塞等待，runAsync 的future 无返回值，输出null
        System.out.println(rFuture.join());
        //阻塞等待
        String name = future.join();
        System.out.println(name);
        executor.shutdown();*/

        //第一个异步任务，常量任务
        CompletableFuture<String> f = CompletableFuture.completedFuture("OK");
        //第二个异步任务
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "hello world", executor)
                .thenComposeAsync(data -> {
                    System.out.println(data);
                    return f; //使用第一个任务作为返回
                }, executor);
        System.out.println(future.join());
        executor.shutdown();

         int COUNT_BITS = Integer.SIZE - 3;
         int CAPACITY   = (1 << COUNT_BITS) - 1;

        // runState is stored in the high-order bits
         int RUNNING    = -1 << COUNT_BITS;
         int SHUTDOWN   =  0 << COUNT_BITS;
         int STOP       =  1 << COUNT_BITS;
         int TIDYING    =  2 << COUNT_BITS;
         int TERMINATED =  3 << COUNT_BITS;

        System.out.println(COUNT_BITS);
        System.out.println(RUNNING);
        System.out.println(SHUTDOWN);
        System.out.println(STOP);
        System.out.println(TIDYING);
        System.out.println(TERMINATED);
    }
}
