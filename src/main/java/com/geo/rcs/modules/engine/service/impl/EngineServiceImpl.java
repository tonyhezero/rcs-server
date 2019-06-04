package com.geo.rcs.modules.engine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.engine.handler.RulesConfigParser;
import com.geo.rcs.modules.engine.service.DroolsService;
import com.geo.rcs.modules.engine.service.EngineService;
import org.kie.api.runtime.rule.ConsequenceException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.service.impl
 * @Description : RuleEngineer
 * @Author yongmingz
 * @email yongmingz@geotmt.com
 * @Creation Date : 2018.1.11
 */

@Service
public class EngineServiceImpl implements EngineService{
    @Value("${geo.event.engine.static}")
    private boolean on;
    @Autowired
    private DroolsService droolsService;

    private static Logger logger = LogUtil.logger(LogFileName.DAILY_LOG);


    /**
     * Run and get RulesEngine result
     * @param rulesConfig
     * @return
     */

    @Override
    public String getRulesRes(String rulesConfig,String rulesKey) throws Exception {
        System.out.println("[RCS-INFO]:规则引擎核心启动运行！");

        long point4 = System.currentTimeMillis();

        // 规则文件
        // String rulesConfig2 = RulesConfig.getRulesConfig();

        String Result = runner(rulesConfig,rulesKey);

        System.out.println("[RCS-INFO]:规则引擎返回运算结果：" + Result);

        System.out.println("[RCS-INFO]:规则引擎核心运行结束！" +(System.currentTimeMillis()-point4)) ;

        return Result;
    }



    @Override
    public String updateResStatusToInvalid(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig , Rules.class);
        rules.setStatus(RulesEngineCode.INVALID.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String updateResStatusToHuman(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig , Rules.class);
        rules.setStatus(RulesEngineCode.HUMAN.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String updateResStatusToPass(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig , Rules.class);
        rules.setStatus(RulesEngineCode.PASS.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String updateResStatusToReject(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig , Rules.class);
        rules.setStatus(RulesEngineCode.REJECT.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String runner(String rulesConfig,String rulesKey) {

        Rules rules = JSONObject.parseObject(rulesConfig , Rules.class);

        try{
//            String rulesContent = RulesConfigParser.rulesParserToContent(rulesConfig);
//            String ruleFile = String.format("%s", rulesFileId);
//            System.out.println("[RCS-INFO]:RULE FILE："  + ruleFile);

            // 动态加载规则(自带KFS生成文件)
//            droolsService.runDynamicRules(rulesContent, rulesFileId, rules);

            // 缓存加载规则(自带KFS生成文件)
            long startTime = System.currentTimeMillis();
            System.out.println("开始时间:"+startTime);
            droolsService.runStaticRules( rules);
            System.out.println("耗时:"+(System.currentTimeMillis() - startTime));
            return checkRulesResult(JSONObject.toJSONString(rules));

        }catch (RcsException e) {
            e.printStackTrace();
            logger.error("【规则集异常】："+e);
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(e.getMsg());
        }catch (IllegalStateException e) {
            e.printStackTrace();
            logger.error("【规则集异常】："+e);
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
        }catch (ConsequenceException e) {
            logger.error("【规则集异常】："+e);
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            logger.error("【规则集异常】："+e);
            e.printStackTrace();
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
        }

        return JSONObject.toJSONString(rules);
    }

    private String checkRulesResult(String rules) {

        Rules ruleEntity = JSON.parseObject(rules, Rules.class);
        System.out.println("RULE：");
        if(ruleEntity.getStatus()==RulesEngineCode.INIT.getCode()){
            throw new RcsException(StatusCode.RULE_EXCUTE_ERROR.getMessage(), StatusCode.RULE_EXCUTE_ERROR.getCode());
        }else{
            String message = "[RCS-INFO]:RUN FINISH：";
            System.out.println(message);
        }

        return rules;
    }
}
