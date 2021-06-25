package com.pattern.prototype;

/**
 * 单例模式
 *
 * @ClassName Singleton
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/23 7:28
 * @Version 1.0
 **/
public class SingletonTest01 {

    private final static SingletonTest01 singleton = new SingletonTest01();

    private SingletonTest01() {
    }

    public static SingletonTest01 getInstance() {
        return singleton;
    }

}
