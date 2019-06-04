package com.geo.rcs.modules.source.dataSourceList.service.impl;

import com.geo.rcs.modules.source.dataSourceList.dao.InterCorrelationMapper;
import com.geo.rcs.modules.source.dataSourceList.entity.InterCorrelation;
import com.geo.rcs.modules.source.dataSourceList.service.SeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by geo on 2019/5/15.
 */
@Service
public class SeparationServiceImpl implements SeparationService {
    @Autowired
    private InterCorrelationMapper interCorrelationMapper;
    @Override
    public List<InterCorrelation> selectByEntity(InterCorrelation record) {
        return interCorrelationMapper.selectByEntity(record);
    }

    @Override
    public int insertSelective(InterCorrelation record) {
        return interCorrelationMapper.insertSelective(record);
    }

    @Override
    public int deleteByEntity(InterCorrelation record) {
        return interCorrelationMapper.deleteByEntity(record);
    }

    @Override
    public List<String> selectDataSourcePerm(Long userId) {
        return interCorrelationMapper.selectDataSourcePerm(userId);
    }
}
