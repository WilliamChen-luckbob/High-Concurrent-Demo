package com.wwstation.learn;

import java.util.concurrent.*;

public class TestLearn {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,
                5,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(5),
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 6; i++) {
            threadPoolExecutor.submit(new Runner("runner" + i));
        }


        TimeUnit.MILLISECONDS.sleep(500);

        while (!threadPoolExecutor.isTerminated()) {
            System.out.println("active count = " + threadPoolExecutor.getActiveCount());
            System.out.println("poolsize = " + threadPoolExecutor.getPoolSize());
            System.out.println("completed = " + threadPoolExecutor.getCompletedTaskCount());
            System.out.println("core pool = " + threadPoolExecutor.getCorePoolSize());
            System.out.println("pool size = " + threadPoolExecutor.getPoolSize());
            System.out.println("task count = " + threadPoolExecutor.getTaskCount());
            System.out.println("==================================================");
            TimeUnit.SECONDS.sleep(3);
        }
    }
}

class Runner implements Runnable {
    private String name;

    public Runner(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("///////////////////" + this.name + " start to work/////////////////");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("/////////////////" + this.name + " finished///////////////////");

    }
}
