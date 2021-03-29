package com.example.distribution.mapper;

import com.example.distribution.domain.Distribution;

import java.util.List;

public interface DistributionMapper {

    List<Distribution> findAll();

    void insert(Distribution distribution);



}
