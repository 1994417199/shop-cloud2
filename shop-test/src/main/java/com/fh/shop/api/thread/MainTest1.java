package com.fh.shop.api.thread;

public class MainTest1 {
    public static void main(String[] args) {
        Thread thread = new Thread(new Thread1());
        thread.start();
        System.out.println("==========="+Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
