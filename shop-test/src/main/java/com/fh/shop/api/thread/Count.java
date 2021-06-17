package com.fh.shop.api.thread;

public class Count implements Runnable{

    private Integer count=0;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public void run() {
        add();
    }

    public synchronized void add(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.count++;
    }
}
