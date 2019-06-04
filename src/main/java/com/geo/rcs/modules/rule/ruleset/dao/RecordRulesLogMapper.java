package com.geo.rcs.modules.rule.ruleset.dao;

import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月10日 上午10:24
 */
@Mapper
@Component(value = "recordRulesLogMapper")
public interface RecordRulesLogMapper {

    void insertBySelective(EngineHistoryLog engineHistoryLog);

    Page<EngineHistoryLog> getRecordById(EngineHistoryLog engineHistoryLog);

    void deleteById(Long logId);

    Page<EngineHistoryLog> findByPage(@Param("userId") Long userId, @Param("ruleSetId") Long ruleSetId, @Param("actionType") Integer actionType, @Param("submitter") String submitter, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<EngineHistoryLog> findAllOption();

    EngineHistoryLog getNearPublishLog(Long ruleSetId);

    EngineHistoryLog selectByPrimaryKey(Long id);

    List<EngineRules> getRecordedRules(Map<String, Object> map);

    void updateAppStatus(EngineHistoryLog engineHistoryLog);
}
