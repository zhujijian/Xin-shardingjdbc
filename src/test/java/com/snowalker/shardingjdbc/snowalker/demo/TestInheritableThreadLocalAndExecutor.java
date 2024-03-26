package com.snowalker.shardingjdbc.snowalker.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestInheritableThreadLocalAndExecutor implements Runnable {
    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * InheritableThreadLocal和线程池搭配使用时，可能得不到想要的结果，因为线程池中的线程是复用的，并没有重新初始化线程，
     * InheritableThreadLocal之所以起作用是因为在Thread类中最终会调用init()方法去把InheritableThreadLocal的map复制到子线程中。
     * 由于线程池复用了已有线程，所以没有调用init()方法这个过程，也就不能将父线程中的InheritableThreadLocal值传给子线程。
     * */

    public static void main(String[] args) throws Exception{
        System.out.println("----主线程启动");
        inheritableThreadLocal.set("主线程第一次赋值");
        System.out.println("----主线程设置后获取值：" + inheritableThreadLocal.get());
        executorService.submit(new TestInheritableThreadLocalAndExecutor());
        System.out.println("主线程休眠2秒");
        Thread.sleep(2000);
        System.out.println("主线程休眠2秒");
        inheritableThreadLocal.set("主线程第二次赋值");
        executorService.submit(new TestInheritableThreadLocalAndExecutor());
        executorService.shutdown();
    }

    @Override
    public void run() {
        System.out.println("----子线程获取值：" + inheritableThreadLocal.get());
    }
}