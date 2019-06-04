package com.geo.rcs.modules.source.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.rule.inter.dao.EngineInterMapper;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.source.client.ParameterCalcClient;
import com.geo.rcs.modules.source.handler.ResponseParser;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.source.verified.client.dao.VerfiedLogMapper;
import com.geo.rcs.modules.source.verified.client.entity.VerfiedLog;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 底层数据源接口请求-RawService
 *
 * @author yongmingz
 * @created on 2017.12.29
 *
 */

@Service("interfaceService")
public class InterfaceServiceImpl implements InterfaceService {
    @Autowired
    private VerfiedLogMapper verfiedLogMapper;
    @Autowired
    private EngineInterMapper interMapper;


    @Autowired
    private EngineInterMapper engineInterMapper;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public List<EngineInter> getAllInter() {
        return interMapper.getAllInter();
    }

    @Override
    public Map<String, Map> getParameterCalcData(List<String> datasourceInnerList, String parameters,Map<String,String> interFieldMap,String rulesConfig,String channel,Long userId) {

        Map<String,Map> result = new HashMap<>();
        ParameterCalcClient calcClient = new ParameterCalcClient();
        for (String inner : datasourceInnerList){
            String data = "";
            if (inner.equals("INPUT_IDNUMBER")){
                long responseTimeStart = System.currentTimeMillis();
                data = calcClient.getData(inner,parameters);
                long responseTimeEnd = System.currentTimeMillis();
                insertLog(rulesConfig,channel,data,parameters,responseTimeStart,responseTimeEnd,userId);
                logger.info("入参返回数据[{}][{}]", DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),data);
            }else if (inner.equalsIgnoreCase("SBZW")){
                long responseTimeStart = System.currentTimeMillis();
                data = calcClient.getSBZWInter();
                long responseTimeEnd = System.currentTimeMillis();
                insertLog(rulesConfig,channel,data,parameters,responseTimeStart,responseTimeEnd,userId);
                logger.info("入参返回数据[{}][{}]", DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),data);
            }else{
                String fieldName = interFieldMap.get(inner);
                long responseTimeStart = System.currentTimeMillis();
                data = calcClient.getData(inner,parameters,fieldName);
                long responseTimeEnd = System.currentTimeMillis();
                insertLog(rulesConfig,channel,data,parameters,responseTimeStart,responseTimeEnd,userId);
                logger.info("入参返回数据[{}][{}]", DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"),data);
            }

            result.putAll(ResponseParser.parserParameterCalcData(inner,data));
        }
        return result;
    }


    @Override
    public EngineInter findInterById(Long id) {
        return engineInterMapper.selectByPrimaryKey(id);
    }

    private void insertLog(String rulesConfig,String channel,String data,String parameters,long responseTimeStart,long responseTimeEnd,Long userId){

    }
}
