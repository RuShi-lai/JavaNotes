package com.example.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyThread1 implements Runnable{

    @Override
    public void run() {

    }
}
class MyThread2 implements Callable{

    @Override
    public Integer call() throws Exception {
        return 200;
    }
}


public class Demo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // new Thread(new MyThread2(),"BB").start();
        new Thread(new MyThread1(),"AA").start();
        FutureTask<Integer> futureTask1 = new FutureTask<>(()->{
            return 1024;
        });

        FutureTask<Integer> futureTask2 = new FutureTask<>(()->{
            System.out.println(Thread.currentThread().getName() + " come in callabel");
            return 1024;
        });

        new Thread(futureTask2,"lucy").start();
        while (!futureTask2.isDone()){
            System.out.println("waiting....................");
        }

        System.out.println(futureTask2.get());

        System.out.println(futureTask2.get());
        System.out.println(Thread.currentThread().getName() + "come over");
    }
}
