package com.geo.rcs.modules.source.service;

import com.geo.rcs.modules.rule.inter.entity.EngineInter;

import java.util.List;
import java.util.Map;

/**
 * Author:  yongmingz
 * Created on : 2018.1.11
 */
public interface SourceMapperService {

    String[] getFieldIds(String rulesConfig) throws Exception;

    List<EngineInter> getRulesInter(String[] rulesFieldIds) throws Exception;

    List<Map<String,Object>> findEntryMapByFieldId(Long userId) throws Exception;

}
