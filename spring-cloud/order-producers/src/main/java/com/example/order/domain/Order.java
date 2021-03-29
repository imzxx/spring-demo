package com.example.order.domain;


import java.io.Serializable;


/**
 * @ClassName Order
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 16:08
 * @Version 1.0
 **/
public class Order implements Serializable {

    private String id;

    private String content;

    private String regDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
