package com.geo.rcs.modules.source.dataSourceList.dao;

import com.geo.rcs.modules.source.dataSourceList.entity.InterCorrelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value="interCorrelationMapper")
public interface InterCorrelationMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByEntity(InterCorrelation record);
    int insert(InterCorrelation record);

    int insertSelective(InterCorrelation record);

    InterCorrelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InterCorrelation record);

    int updateByPrimaryKey(InterCorrelation record);

    List<InterCorrelation> selectByEntity(InterCorrelation record);
    List<String> selectDataSourcePerm(Long userId);
}