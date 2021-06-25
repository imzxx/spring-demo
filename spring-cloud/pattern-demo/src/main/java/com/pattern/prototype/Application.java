package com.pattern.prototype;

/**
 * @ClassName Application
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/24 6:53
 * @Version 1.0
 **/
public class Application {
    public static void main(String[] args) {
        /*for (int i = 0; i < 10; i++) {
            System.out.println(SingletonTest01.getInstance());
        }*/
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+SingletonTest02.getInstance());
            },"线程-"+i).start();
        }
    }
}
