package com.fh.shop.api.thread;
public class SingleTon {
    //申明静态成员变量，当前类对象的引用、
    private volatile static SingleTon uniqueInstance;
    //私有构造方法
    private SingleTon(){}
    //提供公共的静态的方法给外界访问，返回当前类的对象
    public static SingleTon getInstance(){
        //第一次判断
        if(uniqueInstance==null){
            //加锁防止多线程并发的问题
            synchronized (SingleTon.class){
                //第二重判断
                if(uniqueInstance==null){
                    //创建对象，该对象是去全局只有一个的单例对象
                    uniqueInstance = new SingleTon();
                }
            }
        }
        return uniqueInstance;
    }


}
