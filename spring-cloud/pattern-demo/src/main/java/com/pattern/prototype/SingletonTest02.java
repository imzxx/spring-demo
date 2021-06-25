package com.pattern.prototype;

/**
 * @ClassName SingletonTest02
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/24 7:05
 * @Version 1.0
 **/
public class SingletonTest02 {

    private static volatile SingletonTest02 singleton;

    private SingletonTest02() {

    }

    public static SingletonTest02 getInstance() {
        if (singleton == null) {
            synchronized (SingletonTest02.class) {
                if (singleton == null) {
                    singleton = new SingletonTest02();
                }
            }
        }
        return singleton;
    }
}
