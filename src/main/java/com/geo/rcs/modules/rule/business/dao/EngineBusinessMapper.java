package com.geo.rcs.modules.rule.business.dao;

import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 11:47 2019/3/12
 */
@Mapper
@Component(value = "engineBusinessMapper")
public interface EngineBusinessMapper {
    Page<BusinessType> findByPage(BusinessType business);

    void insertSelective(BusinessType business);

    List<BusinessType> queryBusinessName(@Param("typeName") String typeName, @Param("uniqueCode") Long uniqueCode);

    BusinessType selectByPrimaryKey(Long id);

    void updateByPrimaryKeySelective(BusinessType business);
}
