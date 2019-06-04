package com.geo.rcs.modules.source.service;

import com.geo.rcs.modules.rule.inter.entity.EngineInter;

import java.util.List;
import java.util.Map;

/**
 * Author:  yongmingz
 * Created on : 2018.1.11
 */
public interface InterfaceService {

    /**
     * 获取所有的接口集合
     * @return
     */
    List<EngineInter> getAllInter();

    /**
     * 返回入参计算模块数据
     * @param datasourceInnerList
     * @param parameters
     * @return
     */
    Map<String, Map> getParameterCalcData(List<String> datasourceInnerList, String parameters, Map<String, String> interFieldMap, String rulesConfig, String channel, Long userId);


    EngineInter findInterById(Long id);


}
