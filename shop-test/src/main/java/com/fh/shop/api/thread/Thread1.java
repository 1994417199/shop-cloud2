package com.fh.shop.api.thread;

public class Thread1 implements Runnable  {
    @Override
    public void run() {
        System.out.println("***********"+Thread.currentThread().getName());
    }
}
