package com.fh.shop.api.pool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class MainTest {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println("===="+i);
            createFile();
        }
    }

    public static void createFile(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File file = new File("c:/ThreadPoolExecutorTest/" + UUID.randomUUID() + ".txt");
        FileWriter f=null;
        try {
             f = new FileWriter(file);
//             f.write("xxx");
             f.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(f==null){
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
