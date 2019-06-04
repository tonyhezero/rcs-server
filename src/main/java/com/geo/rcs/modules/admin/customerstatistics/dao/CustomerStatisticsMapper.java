package com.geo.rcs.modules.admin.customerstatistics.dao;

import com.geo.rcs.modules.event.entity.EventEntry;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年5月2日 下午4:24:05
 */
@Mapper
@Component(value = "customerStatisticsMapper")
public interface CustomerStatisticsMapper {
	Page<EventEntry> findAllByPage(EventEntry entry);
}
