package com.fh.shop.api.thread;

public class LX2{
    public static void main(String[] args){
        Thread  t =new Thread(new LX1());
        t.start();
    }
}
