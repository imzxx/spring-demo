package com.pattern.factory;

/**
 * @ClassName Pizza
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/25 7:20
 * @Version 1.0
 **/
public abstract class Pizza {

    private String name;

    protected void prepare(String name) {
        this.name = name;
        System.out.println(name + "披萨原材料准备完毕");
    }

    protected void bake() {
        System.out.println(name + "披萨制作完毕");
    }

    protected void cut() {
        System.out.println(name + "披萨切割完毕");
    }

    protected void box() {
        System.out.println(name + "披萨打包完毕");
    }
}
