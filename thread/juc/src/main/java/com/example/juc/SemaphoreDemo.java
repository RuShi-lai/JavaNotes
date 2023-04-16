package com.example.juc;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {

           new Thread(() ->{
               try {
                   semaphore.acquire();
                   System.out.println(Thread.currentThread().getName() + " 抢到车位");

                   //停车时间
                   TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                   System.out.println(Thread.currentThread().getName() + " --------------离开车位");

               }catch (Exception e){
                   e.printStackTrace();
               }finally {
                   semaphore.release();
               }
           },String.valueOf(i)).start();
        }
    }
}
