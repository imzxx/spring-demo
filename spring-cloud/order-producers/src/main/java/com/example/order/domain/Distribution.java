package com.example.order.domain;
import java.io.Serializable;


/**
 * @ClassName Order
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 16:08
 * @Version 1.0
 **/

public class Distribution implements Serializable {


    private String distriId;

    private String orderId;

    private String orderContent;

    private String regDate;

    @Override
    public String toString() {
        return "Distribution{" +
                "distriId='" + distriId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderContent='" + orderContent + '\'' +
                ", reg_date='" + regDate + '\'' +
                '}';
    }

    public String getDistriId() {
        return distriId;
    }

    public void setDistriId(String distriId) {
        this.distriId = distriId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
