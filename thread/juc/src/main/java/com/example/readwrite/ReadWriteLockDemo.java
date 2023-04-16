package com.example.readwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache{
    private volatile Map<String,Object> mp = new HashMap<>();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public void put(String key, Object value){
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 正在写操作" + key);
            TimeUnit.MILLISECONDS.sleep(300);

            mp.put(key,value);
            System.out.println(Thread.currentThread().getName() + " 写完了" + key);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public Object get(String key){
        readWriteLock.readLock().lock();
        Object result = null;
        try {
            System.out.println(Thread.currentThread().getName() + " 正在读取" + key);
            TimeUnit.MILLISECONDS.sleep(300);
            result = mp.get(key);
            System.out.println(Thread.currentThread().getName() + " 取完了" + key);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            readWriteLock.readLock().unlock();
        }

        return result;
    }
}


public class ReadWriteLockDemo {
    public static void main(String[] args) throws InterruptedException {
        MyCache myCache = new MyCache();

        for (int i = 0; i < 5; i++) {
            final int num = i;
            new Thread(() ->{
                myCache.put(num +"",num+"");
            },String.valueOf(i)).start();
        }

        TimeUnit.MILLISECONDS.sleep(3000);
        for (int i = 0; i < 5; i++) {
            final int num = i;
            new Thread(() ->{
                myCache.get(num +"");
            },String.valueOf(i)).start();
        }
    }
}
