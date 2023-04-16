package com.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Share {
    private int number = 0;

    //创建lock
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void incr() throws InterruptedException {
        lock.lock();
        try {
            //判断
            while ( number != 0){
                condition.await();
            }
            //干活
            number++;
            System.out.println(Thread.currentThread().getName() + ": " + number);
            //通知其它线程
            condition.signalAll();
        }finally {
           //解锁
            lock.unlock();
        }
    }

    public void decr() throws InterruptedException {
        lock.lock();
        try {
            //判断
            while ( number != 1){
                condition.await();
            }
            //干活
            number--;
            System.out.println(Thread.currentThread().getName() + ": " + number);
            //通知其它线程
            condition.signalAll();
        }finally {
            //解锁
            lock.unlock();
        }
    }
}
public class ThreadDemo {
    public static void main(String[] args) {
        Share share = new Share();
        new Thread(() ->{
            for(int i = 1; i <= 10; i++){
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"AA").start();

        new Thread(() ->{
            for(int i = 1; i <= 10; i++){
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"BB").start();

        new Thread(() ->{
            for(int i = 1; i <= 10; i++){
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"CC").start();
        new Thread(() ->{
        for(int i = 1; i <= 10; i++){
            try {
                share.decr();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    },"DD").start();
}
}
