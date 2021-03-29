package com.example.distribution;

import com.example.distribution.domain.Distribution;
import com.example.distribution.mapper.DistributionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName DistributionTest
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 20:43
 * @Version 1.0
 **/
@SpringBootTest(classes = DistributionApplication.class)
@RunWith(SpringRunner.class)
public class DistributionTest {

    @Autowired
    private DistributionMapper distributionMapper;

    @Test
    public void Test(){
        /*List<Distribution> distributions=distributionMapper.findAll();
        System.out.println(distributions);*/
        Distribution distribution = new Distribution();
        distribution.setDistriId("1111");
        distribution.setOrderContent("添加测试");
        distribution.setOrderId("1111");
        distribution.setRegDate("2021-03-28");
        distributionMapper.insert(distribution);
    }

}
