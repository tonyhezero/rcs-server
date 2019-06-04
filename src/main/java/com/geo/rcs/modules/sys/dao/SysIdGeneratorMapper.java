package com.geo.rcs.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author wp
 * @date Created in 11:07 2019/3/11
 */
@Mapper
@Component(value = "sysIdGeneratorMapper")
public interface SysIdGeneratorMapper {

    List<Map<String,Object>> findAll();

    void updateValue(@Param("key") String key, @Param("value") Long value);
}
