package com.example.order.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName CountDownLatchDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/4 15:55
 * @Version 1.0
 **/
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() ->{
                System.out.println(Thread.currentThread().getName()+"==线程执行");
                countDownLatch.countDown();
            },"线程"+String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println("主线程main方法执行");
    }
}
