package com.geo.rcs.modules.event.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.modules.event.service.BatchEventService;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author wp
 * @date Created in 10:52 2019/4/19
 */
@Service
public class BatchEventServiceImpl implements BatchEventService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    EngineInterService engineInterService;

    /**
     * 构造导出信息
     * @param parameters 入参
     * @return 三要素
     */
    private String getBaseInfo(String parameters){
        StringBuilder baseInfo = new StringBuilder();
        JSONObject jsonObject = JSONObject.parseObject(parameters);
        baseInfo.append(jsonObject.get("realName")).append("\n");
        baseInfo.append(jsonObject.get("cid")).append("\n");
        baseInfo.append(jsonObject.get("idNumber"));
        return baseInfo.toString();
    }

    private String getTaskStatus(int status){
        switch (status){
            case 0: return "未执行";
            case 1: return "通过";
            case 2: return "人工审核";
            case 3: return "拒绝";
            case 4: return "错误";
            default: return "异常";
        }
    }
}
