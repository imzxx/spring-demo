package com.example.order.domain;

/**
 * @ClassName OrderMessage
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 22:16
 * @Version 1.0
 **/
public class OrderMessage {

    private String id;

    private String content;

    private String status;

    private String regDate;

    @Override
    public String toString() {
        return "OrderMessage{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
