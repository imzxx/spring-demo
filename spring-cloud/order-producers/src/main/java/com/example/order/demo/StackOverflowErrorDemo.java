package com.example.order.demo;

/**
 * @ClassName StackOverflowErrorDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/2 7:05
 * @Version 1.0
 **/
public class StackOverflowErrorDemo {
    public static void main(String[] args) {
        //栈内存溢出错误
        //com.example.order.demo.StackOverflowErrorDemo.stackOverflowError
        stackOverflowError();
    }

    private static void stackOverflowError() {
        stackOverflowError();
    }
}
