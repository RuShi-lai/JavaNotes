package com.example.juc;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static final int NUMBER = 7;
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, () -> {
            System.out.println("集齐7龙珠召唤神龙");
        });

        for (int i = 1; i <= 7; i++) {
            new Thread(()->{
                try {
                System.out.println(Thread.currentThread().getName() + " 星龙珠被收集");
                cyclicBarrier.await();
                }catch (Exception e){
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
