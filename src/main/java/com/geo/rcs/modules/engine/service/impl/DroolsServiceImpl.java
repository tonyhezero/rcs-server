package com.geo.rcs.modules.engine.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.engine.entity.*;
import com.geo.rcs.modules.engine.handler.RulesConfigParser;
import com.geo.rcs.modules.engine.handler.SerializeUtil;
import com.geo.rcs.modules.engine.service.DroolsService;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DroolsServiceImpl implements DroolsService {
    private static Map<String, Map<String, Object>> kieBaseMapCache = new HashMap<>(50);
    private static final String RULES_DRL_ = "RULES:DRL:";
    private static final String KIEBASE_KEY = "KIEBASE_KEY";
    private static final String RULES_CONTENT_KEY = "RULES_CONTENT_KEY";
    private static final String RULES_DRL_KEY_SET = "RULES:DRL:KEY:SET";
    @Value("${geo.event.cache.type}")
    private String cacheType;
    @Value("${geo.event.drl.tmp}")
    private boolean on;

    private static final KieBase KIE_BASE;
    static {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write(ResourceFactory.newClassPathResource("startDemo.drl"));
        kieServices.newKieBuilder(kfs).buildAll();
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KIE_BASE = kieContainer.getKieBase();
    }

    @PostConstruct
    private void init(){
        Rules rules = JSON.parseObject(RulesConfig.getRulesConfig(), Rules.class);
        runStaticRules(rules);
    }



    @Scheduled(cron = "0 0 0 ? * *")
    private void initMapCache(){
        kieBaseMapCache = new ConcurrentHashMap<>(50);
    }

    @Override
    public Object runStaticRules(Object runner) {
        KieSession kieSession = KIE_BASE.newKieSession();

        Rules rules = (Rules) runner;
        if(rules.getRuleList()!=null){
            for (Rule rule : rules.getRuleList()) {
                rule.setConditionResultJs(rule.getConditionRelationShip());
                kieSession.insert(rule);
                if(rule.getConditionsList()!=null){
                    for (Condition condition : rule.getConditionsList()) {
                        condition.setFieldResultJs(condition.getFieldRelationShip());
                        kieSession.insert(condition);
                        if(condition.getFieldList()!=null){
                            for (Field field : condition.getFieldList()) {
                                if(field.getFunctionSet()!=null){
                                    field.getFunctionSet().forEach(kieSession::insert);
                                }
                                kieSession.insert(field);
                            }
                        }
                    }
                }
            }
        }
        kieSession.insert(rules);
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("规则结束");

        return runner;
    }


    /***
     * Automatic
     * Refer: https://docs.jboss.org/jbpm/v6.0.Beta2/javadocs/org/kie/api/builder/KieFileSystem.html#write(java.lang.String, byte[])
     * @ write method
     */
    @Override
    public Object runDynamicRules(String ruleContent, String rulesFileId, Object runner){
        // TODO:从配置读取路径信息
        //String fileName = String.format("/Users/mingming/gitroom/rcs/rules/%s.drl", rulesFileId);

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();

        kfs.write( String.format("src/main/resources/rules/%s", rulesFileId),ruleContent.getBytes());
        //kfs.write( String.format("src/main/resources/rules/%s.drl", rulesFileId), ruleContent);
        KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
        Results results = kieBuilder.getResults();
        if( results.hasMessages( org.kie.api.builder.Message.Level.ERROR ) ){
            System.out.println( results.getMessages() );
            throw new IllegalStateException( String.format("语法错误：\n%s",results.getMessages() ) );
        }
        KieContainer kieContainer = kieServices.newKieContainer( kieServices.getRepository().getDefaultReleaseId() );
        KieBase kieBase = kieContainer.getKieBase();
        KieSession ksession = kieBase.newKieSession();

        ksession.insert(runner);
        ksession.fireAllRules();
        ksession.dispose();
        System.out.println("规则结束");

        kfs.delete(String.format("src/main/resources/rules/%s.drl", rulesFileId));

        return runner;
    }

    @Override
    public Object runDynamicCacheRules(String ruleContent, String rulesFileId, Object runner) {
//        rulesFileId = RULES_DRL_ + rulesFileId;
        long startTime = System.currentTimeMillis();
        System.out.println("开始时间:"+startTime);
        KieBase kieBase = getKieBaseByCache(rulesFileId, ruleContent,runner);
        System.out.println("获取缓存耗时:"+(System.currentTimeMillis() - startTime));
        KieSession ksession = kieBase.newKieSession();
        ksession.insert(runner);
        ksession.fireAllRules();
        ksession.dispose();
        System.out.println("规则结束");
        return runner;
    }

    private KieBase getKieBaseByCache(String rulesKey, String ruleContent, Object runner) {
        KieBase kieBase = null;
        return kieBase;
    }

    private KieBase getByRedis(Jedis jedis,String rulesKey, String ruleContent,Object runner){
        KieBase kieBase;
        Map<byte[], byte[]> map = jedis.hgetAll((RULES_DRL_+rulesKey).getBytes());
        if (map == null || map.isEmpty()) {
            map = putKieBaseToRedis(jedis,rulesKey, ruleContent);
        }
        byte[] ruleContentByte = map.get(RULES_CONTENT_KEY.getBytes());
        String oldRulesContent = new String(ruleContentByte);
        if (ruleContent.equals(oldRulesContent)&&SerializeUtil.compareContent(rulesKey,ruleContent)) {
            kieBase = (KieBase) unserizlize(jedis,rulesKey,map.get(KIEBASE_KEY.getBytes()),runner,ruleContent);
        } else {
            kieBase = updateKieBaseToRedis(jedis,rulesKey,ruleContent);
        }
        return kieBase;
    }

    private synchronized Map<byte[],byte[]> putKieBaseToRedis(Jedis jedis,String rulesKey, String ruleContent) {
        Map<byte[], byte[]> map = jedis.hgetAll((RULES_DRL_+rulesKey).getBytes());
        if(map == null || map.isEmpty()){
            KieBase kieBase = getKieBase(rulesKey,ruleContent);
            map = new HashMap<>(2);
            map.put(KIEBASE_KEY.getBytes(),serialize((KnowledgeBaseImpl) kieBase));
            map.put(RULES_CONTENT_KEY.getBytes(),ruleContent.getBytes());
            jedis.hmset((RULES_DRL_+rulesKey).getBytes(),map);
            map = jedis.hgetAll((RULES_DRL_+rulesKey).getBytes());
        }
        return map;
    }

    private synchronized KieBase updateKieBaseToRedis(Jedis jedis,String rulesKey, String ruleContent) {
        KieBase kieBase = getKieBase(rulesKey,ruleContent);
        Map<byte[],byte[]> map = new HashMap<>(2);
        map.put(KIEBASE_KEY.getBytes(),serialize((KnowledgeBaseImpl) kieBase));
        map.put(RULES_CONTENT_KEY.getBytes(),ruleContent.getBytes());
        jedis.hmset((RULES_DRL_+rulesKey).getBytes(),map);
        jedis.srem(RULES_DRL_KEY_SET,rulesKey);
        SerializeUtil.updateCache(rulesKey, (KnowledgeBaseImpl) kieBase,ruleContent);
        return kieBase;
    }

    private KieBase getKieBaseByMap(String rulesFileId, String ruleContent) {
        Map<String, Object> valueMaP = kieBaseMapCache.get(rulesFileId);
        if (valueMaP == null) {
            KieBase kieBase = putInCache(rulesFileId,ruleContent);
            if(kieBase == null){
                kieBase = (KieBase) kieBaseMapCache.get(rulesFileId).get(KIEBASE_KEY);
            }
            return kieBase;
        } else {
            if (ruleContent.equals((String) valueMaP.get(RULES_CONTENT_KEY))) {
                return (KieBase) valueMaP.get(KIEBASE_KEY);
            } else {
                KieBase kieBase = putInCache(rulesFileId,ruleContent);
                if(kieBase == null){
                    kieBase = (KieBase) kieBaseMapCache.get(rulesFileId).get(KIEBASE_KEY);
                }
                return kieBase;
            }
        }
    }

    private synchronized KieBase putInCache(String rulesFileId,String ruleContent){
        if(kieBaseMapCache.get(rulesFileId) == null){
            KieBase kieBase = getKieBase(rulesFileId,ruleContent);
            HashMap<String, Object> valueMap = new HashMap<>(2);
            valueMap.put(KIEBASE_KEY, kieBase);
            valueMap.put(RULES_CONTENT_KEY, ruleContent);
            kieBaseMapCache.put(rulesFileId, valueMap);
            return kieBase;
        }
        return null;
    }

    private KieBase getKieBase(String rulesId,String ruleContent) {
        if(on){
            RulesConfigParser.rulesParserToFile(rulesId, ruleContent);
        }
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write(String.format("src/main/resources/rules/%s.drl", rulesId), ruleContent.getBytes());
        kieServices.newKieBuilder(kfs).buildAll();
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        return kieContainer.getKieBase();
    }

    //序列化
    public static byte[] serialize(KnowledgeBaseImpl object) {
        try {
            return SerializeUtil.serialize(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //反序列化
    public Object unserizlize(Jedis jedis,String rulesKey,byte[] byt,Object runner,String ruleContent) {
        long startTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName()+"---开始时间："+(startTime));
        try {
            KieBase obj = null;
            if(!SerializeUtil.isContains(rulesKey)){
                obj = unserizlizeFirst(jedis,rulesKey,byt,runner,ruleContent);
            }
            if(obj == null){
                obj = SerializeUtil.getKieBase(rulesKey);
            }
            System.out.println(Thread.currentThread().getName()+"unserialize 耗时："+(System.currentTimeMillis() - startTime));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private synchronized KieBase unserizlizeFirst(Jedis jedis, String rulesKey, byte[] byt,Object runner,String ruleContent){
        KieBase obj = null;
        if(!SerializeUtil.isContains(rulesKey)){
            obj = SerializeUtil.unserialize(rulesKey ,byt ,ruleContent);
            jedis.sadd(RULES_DRL_KEY_SET,rulesKey);
            KieSession kieSession = obj.newKieSession();
            kieSession.insert(runner);
            kieSession.fireAllRules();
        } else {
            obj = SerializeUtil.getKieBase(rulesKey);
        }
        return obj;
    }
}
