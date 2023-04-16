package com.example.lock;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//演示线程不安全
public class ThreadDemo4 {
    public static void main(String[] args) {
        //List<String> list = new Vector<>();
        //List<String> list = Collections.synchronizedList(new ArrayList<>());
        //List<String> list = new CopyOnWriteArrayList<>();
        //List<String> list = new ArrayList<>();

      /*  for (int i = 0; i < 100; i++) {
            new Thread(() ->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }*/

        Set<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() ->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }
}
