package com.pattern.prototype;

/**
 * @ClassName SingletonTest03
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/24 7:20
 * @Version 1.0
 **/
public class SingletonTest03 {

    private SingletonTest03() {

    }

    public static SingletonTest03 getInstance() {
        return SingletonInstance.SINGLETON;
    }

    private static class SingletonInstance {
        private static SingletonTest03 SINGLETON = new SingletonTest03();
    }

}
