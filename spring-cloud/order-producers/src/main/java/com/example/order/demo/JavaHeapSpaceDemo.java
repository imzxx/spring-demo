package com.example.order.demo;

import java.util.Random;

/**
 * @ClassName JavaHeapSpaceDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/2 7:11
 * @Version 1.0
 **/
public class JavaHeapSpaceDemo {
    public static void main(String[] args) {
        //-Xms10m -Xmx10m
        //堆内存溢出错误
        //java.lang.OutOfMemoryError: Java heap space
        String str = "Hello World";
        while (true) {
            str += str + new Random().nextInt(111111) + new Random().nextInt(22222);
            str.intern();
        }
    }
}
