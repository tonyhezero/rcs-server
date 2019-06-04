package com.geo.rcs.modules.source.verified.client.dao;


import com.geo.rcs.modules.source.verified.client.entity.VerfiedLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "verfiedLogMapperr")
public interface VerfiedLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VerfiedLog record);

    int insertSelective(VerfiedLog record) ;

    VerfiedLog selectByPrimaryKey(Long id);

    List<VerfiedLog> selectAll(VerfiedLog record);

    int updateByPrimaryKeySelective(VerfiedLog record);

    int updateByPrimaryKeyWithBLOBs(VerfiedLog record);

    int updateByPrimaryKey(VerfiedLog record);

    Page<Map<String,Object>> findByPageIds(Long[] ids);

    Page<Map<String,Object>> findByPageAll(VerfiedLog record);
}