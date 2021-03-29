package com.example.distribution.controller;

import com.example.distribution.domain.Distribution;
import com.example.distribution.domain.Order;
import com.example.distribution.mapper.DistributionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.Transient;

/**
 * @ClassName controller
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 21:33
 * @Version 1.0
 **/
@Controller
@ResponseBody
public class DistriController {

    @Autowired
    private DistributionMapper distributionMapper;

    @RequestMapping("/save/order")
    @Transactional
    public Distribution saveOrder(@RequestBody Order order) {
        Distribution distribution = new Distribution();
        distribution.setRegDate(order.getRegDate());
        distribution.setOrderContent(order.getContent());
        distribution.setOrderId(order.getId());
        distribution.setDistriId("2222");
        distributionMapper.insert(distribution);
        //int a = 1 / 0;
        return distribution;
    }

}
