package com.example.order.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName CyclicBarrierDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/5 8:50
 * @Version 1.0
 **/
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10,()->{
            System.out.println("=====启动最后的线程");
        });
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"---启动");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },"线程"+String.valueOf(i)).start();
        }
    }
}
