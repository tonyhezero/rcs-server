package com.geo.rcs.modules.source.dataSourceList.service;

import com.geo.rcs.modules.source.dataSourceList.entity.InterCorrelation;

import java.util.List;

/**
 * Created by geo on 2019/5/15.
 */
public interface SeparationService {
    List<InterCorrelation> selectByEntity(InterCorrelation record);

    int insertSelective(InterCorrelation record);

    int deleteByEntity(InterCorrelation record);

    List<String> selectDataSourcePerm(Long userId);
}
