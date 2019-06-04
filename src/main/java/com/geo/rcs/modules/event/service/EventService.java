package com.geo.rcs.modules.event.service;

import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.vo.EventReport;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 事件进件
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/1/16 11:41
 */
public interface EventService {
    /**
     * 保存
     * @param entry
     * @return
     */
    EventEntry save(EventEntry entry);

    /**
     * 修改
     * @param entry
     * @return
     */
    int update(EventEntry entry);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    EventReport findById(Long id);

    /**
     * 分页查询
     * @return
     */
    Page<EventEntry> findByPage(EventEntry eventEntry);


    /** 事件统计相关接口 */
    /**
     * 今日事件统计
     * @return
     */
    EventStatEntry todayEventStat(Map<String, Object> map);

    /**
     * 风险事件统计
     * @return
     */
    EventStatEntry riskEventStat(Map<String, Object> map);

    /**
     * 风险地图统计
     */
    EventStatEntry mapEventStat(Map<String, Object> map);

    /**
     * 获取所有产品字段（名称：描述）不从redis读取，不缓存
     * @return
     */
    Map<String,String> findAllFieldKV2();

    /**
     * 客户事件统计
     * @param map
     * @return
     */
    EventStatEntry custEventStat(Map<String, Object> map);

    /**
     * 昨日事件统计
     * @param map
     * @return
     */
    List<EventStatEntry> yesterdayEventStat(Map<String, Object> map);

    EventStatEntry thisRuleSetEventStat(Map<String, Object> map);

    List<EventEntry> thisRuleSetRecentStat(Map<String, Object> map);

    /**
     * 规则集进件结果字段分组
     * 分类：
     * 【运营商信息】、【涉诉信息】、【多平台借贷信息】、【关注名单】、【资产信息】、【多头信息】、【其它信息】
     * @param eventId
     * @return
     */
    Map<String,Map<String,Object>> getClassifyField(Long eventId);


    Map<String,Map<String,Object>> getClassifyCollection(Rules rules);

    /**
     * 字段名字中英文映射Map
     * @return
     */
    Map<String,String> fieldNameMapping();

}
