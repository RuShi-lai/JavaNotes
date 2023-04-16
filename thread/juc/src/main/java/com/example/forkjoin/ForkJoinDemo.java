package com.example.forkjoin;


import java.util.concurrent.*;

class  MyTask extends RecursiveTask<Integer>{
    //拆分差值不能超过10
    public static final Integer VALUE = 10;
    private int begin;
    private int end;
    private int result;

    public MyTask(int begin, int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        //判断 相加2个数是否大于10
        if(end - begin <= VALUE){
            for(int i = begin; i <= end; i++){
                result = result + i;
            }
        }else{
            int middle = (begin + end) / 2;

            MyTask task1 = new MyTask(begin,middle);

            MyTask task2 = new MyTask(middle+1,end);

            task1.fork();
            task2.fork();

            result = task1.join() + task2.join();
        }
        return result;
    }
}
public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyTask task = new MyTask(0,100);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(task);
        Integer result = forkJoinTask.get();
        System.out.println(result);
        forkJoinPool.shutdown();

 /*       CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName() + " completableFuture1");
        });

        CompletableFuture<Integer> completableFuture1 =CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName() + " completableFuture2");
            return 1024;
        });
        completableFuture1.whenComplete((t,u)->{
            System.out.println(t); //返回值
            System.out.println(u); //异常
         }).get();*/
    }
}
