package com.example.sync;

//第一步 创建资源类 定义属性和操作方法
class Share {
    private int number = 0;

    // +1
    public synchronized void incr() throws InterruptedException {
        //判断  干活  通知
        while (number != 0){
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName() + ": " + number);

        //通知其它线程
        this.notifyAll();
    }

    // -1
    public synchronized void decr() throws InterruptedException {
        //判断  干活  通知
        while (number != 1){
            this.wait();
        }
        //干活
        number--;
        System.out.println(Thread.currentThread().getName() + ": " + number);

        //通知其它线程
        this.notifyAll();

    }

}
public class TheadDemo {

    //创建多个线程，调用资源类的操作方法

    public static void main(String[] args) {
        Share share = new Share();
        //创建线程
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
    }
}
