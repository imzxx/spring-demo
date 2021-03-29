package com.example.order.mapper;

import com.example.order.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface OrderMapper {
    List<Order> findAll();

    void insertOrder(Order order);
}
