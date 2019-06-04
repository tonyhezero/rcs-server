package com.geo.rcs.modules.rule.ruleset.dao;

import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRulesOnline;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author wp
 * @date Created in 11:18 2019/2/18
 */
@Mapper
@Component(value = "engineRulesOnlineMapper")
public interface EngineRulesOnlineMapper {

    Long insertSelective(EngineRulesOnline record);

    Long updateRuleSet(EngineRulesOnline record);

    Long updateRuleSetVerify(EngineRulesOnline record);

    int deleteByPrimaryKey(Long id);

    EngineRulesOnline findRulesById(Long id);

    Page<EngineRules> findByPage(EngineRules ruleSet);
}
