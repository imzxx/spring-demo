package com.example.order.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockSupportDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/5 22:21
 * @Version 1.0
 **/
public class LockSupportDemo {
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "线程启动");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "线程被唤醒");
        }, "A");
        a.start();

        Thread b = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "线程通知");
            LockSupport.unpark(a);
        }, "B");
        b.start();
    }
}
