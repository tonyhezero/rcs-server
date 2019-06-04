package com.geo.rcs.common.redis;

import com.geo.rcs.modules.sys.dao.SysIdGeneratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wp
 * @date Created in 10:38 2019/3/11
 */
@Component
public class IDGenerator {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private SysIdGeneratorMapper sysIdGeneratorMapper;
    private int next = 500;
    private Map<String,Long> lastIdMap = new ConcurrentHashMap<>();

    public Long getId(String key){
        Long id = redisTemplate.opsForValue().increment(key,1);
        if(id>=lastIdMap.get(key)){
            sysIdGeneratorMapper.updateValue(key,id+next);
            lastIdMap.put(key,id+next);
        }
        return id;
    }

    @PostConstruct
    private void init(){
        List<Map<String,Object>> list = sysIdGeneratorMapper.findAll();
        for (Map<String, Object> map : list) {
            lastIdMap.put((String) map.get("key"), (Long) map.get("value"));
        }
        for (String s : lastIdMap.keySet()) {
            if(!redisTemplate.hasKey(s)){
                redisTemplate.opsForValue().increment(s,lastIdMap.get(s));
            }
        }
    }
}
