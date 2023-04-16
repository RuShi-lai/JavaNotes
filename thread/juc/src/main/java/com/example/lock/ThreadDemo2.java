package com.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//线程间定制化通信
class ShareResource {
    //定义标志位
    private int flag = 1; // AA 1 BB 2 CC 3
    // 创建lock锁
    private Lock lock = new ReentrantLock();

    //创建3个condition
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    //打印5次， 参数第几轮
    public void print5(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 1){
                c1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i + "轮数：" + loop);
            }
            flag = 2;  //修改标志位
            c2.signal(); //通知BB线程
        }finally {
            lock.unlock();
        }
    }

    //打印10次， 参数第几轮
    public void print10(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 2){
                c2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i + "轮数：" + loop);
            }
            flag = 3;  //修改标志位
            c3.signal(); //通知CC线程
        }finally {
            lock.unlock();
        }
    }


    //打印15次， 参数第几轮
    public void print15(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 3){
                c3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i + "轮数：" + loop);
            }
            flag = 1;  //修改标志位
            c1.signal(); //通知AA线程
        }finally {
            lock.unlock();
        }
    }
}

public class ThreadDemo2 {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() ->{
            for(int i = 1; i <= 5; i++){
                try {
                    shareResource.print5(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"AA").start();

        new Thread(() ->{
            for(int i = 1; i <= 10; i++){
                try {
                    shareResource.print10(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"BB").start();

        new Thread(() ->{
            for(int i = 1; i <= 15; i++){
                try {
                    shareResource.print15(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"CC").start();
    }
}
