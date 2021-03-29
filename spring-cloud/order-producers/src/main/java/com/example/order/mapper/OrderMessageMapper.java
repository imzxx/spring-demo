package com.example.order.mapper;

import com.example.order.domain.Order;
import com.example.order.domain.OrderMessage;

import java.util.List;

public interface OrderMessageMapper {
    List<Order> findAll();

    void insertOrder(OrderMessage orderMessage);

    int updateOrderStatus(OrderMessage orderMessage);
}
