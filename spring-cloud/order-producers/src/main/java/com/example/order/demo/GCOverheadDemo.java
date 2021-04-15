package com.example.order.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GCOverheadDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/2 7:23
 * @Version 1.0
 **/
public class GCOverheadDemo {
    public static void main(String[] args) {
        /**
         * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
         * GC回收时间过长时会抛出OutOfMemroyError。超过98%的时间用来做GC并且回收了不到2%的堆内存。
         */
        int i = 0;
        List<String> list = new ArrayList<>();
        try {
            list.add(String.valueOf(++i).intern());
        } catch (Throwable e) {
            System.out.println("=====i:"+i);
            e.printStackTrace();
            throw e;
        }
    }
}
