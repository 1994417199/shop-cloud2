package com.fh.shop.api.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainPool {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,20,1,TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println("========="+i);
            threadPoolExecutor.execute(new MainFile());
        }
    }

}
