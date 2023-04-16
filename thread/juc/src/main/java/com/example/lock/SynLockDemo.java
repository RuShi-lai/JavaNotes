package com.example.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//可重入锁
public class SynLockDemo {


    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        new Thread(() ->{
            try {

                lock.lock();
                System.out.println(Thread.currentThread().getName() + "  外层");

                try {
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + ": 内层");
                } finally {
                    lock.unlock();
                }
            }finally {
                lock.unlock();
            }
        },"aa").start();

    }
}
