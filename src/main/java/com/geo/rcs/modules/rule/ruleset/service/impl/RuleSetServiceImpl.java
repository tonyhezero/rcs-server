package com.geo.rcs.modules.rule.ruleset.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.RcsCache;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.engine.entity.FunctionField;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesOnlineMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRulesOnline;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.geo.rcs.modules.rule.util.RulesExcelUtil;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:50
 */
@Service
public class RuleSetServiceImpl implements RuleSetService {

    @Value("${geo.redis.open}")
    private  boolean redisSwitch;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private EngineRulesOnlineMapper engineRulesOnlineMapper;
    @Autowired
    private EngineInterService engineInterService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private FieldService fieldService;

    @Override
    public Page<EngineRules> findByPage(EngineRules ruleSet) {
        PageHelper.startPage(ruleSet.getPageNo(), ruleSet.getPageSize());
        return engineRulesMapper.findByPage(ruleSet);
    }

    @Override
    public Page<EngineRules> findOnlineByPage(EngineRules ruleSet) {
        PageHelper.startPage(ruleSet.getPageNo(), ruleSet.getPageSize());
        return engineRulesOnlineMapper.findByPage(ruleSet);
    }

    @Override
    public EngineRules findAllById(Long id,boolean unActive) {
        //如果从redis取到规则集，直接返回
        EngineRules engineRules;
        try{
            engineRules = findOnlineByRedis(id);
        } catch (Exception e){
            LogUtil.error("redis查找规则集",id.toString(),"",e);
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        if(unActive&&engineRules.getRuleList()!=null){
            List<EngineRule> list = new ArrayList<>();
            for (EngineRule engineRule : engineRules.getRuleList()) {
                if(engineRule.getActive()==1){
                    list.add(engineRule);
                }
            }
            engineRules.setRuleList(list);
        }
        Integer modifyFlag = (Integer) redisTemplate.opsForValue().get(RcsCache.RULESET_MODIFY.getHeader() +id);
        if(modifyFlag==null){
            modifyFlag = updateRulesModifyFlag(id);
        }
        engineRules.setModifyFlag(modifyFlag);
        return engineRules;
    }

    private Integer updateRulesModifyFlag(Long id){
        EngineRules engineRulesForView;
        EngineRules engineRules;
        try {
            engineRules = findOnlineByRedis(id);
            engineRulesForView = findConfigByRedis(id);
        } catch (RcsException e){
            redisTemplate.opsForValue().set(RcsCache.RULESET_MODIFY.getHeader() +id,0);
            return 0;
        }
        engineRulesForView.setActive(engineRules.getActive());
        engineRulesForView.setRuleJson(null);
        engineRules.setRuleJson(null);
        Map map = JSONUtil.beanToMap(engineRulesForView);
        Map map2 = JSONUtil.beanToMap(engineRules);
        if(!comparisonMap(map,map2)){
            redisTemplate.opsForValue().set(RcsCache.RULESET_MODIFY.getHeader() +id,2);
            return 2;
        } else {
            redisTemplate.opsForValue().set(RcsCache.RULESET_MODIFY.getHeader() +id,1);
            return 1;
        }
    }

    private boolean comparisonMap(Map map1,Map map2){
        boolean result = true;
        for (Object key : map1.keySet()) {
            if("verify".equals(key.toString())||"showName".equals(key.toString())||"addUser".equals(key.toString())||"addTime".equals(key.toString())||"ruleJson".equals(key.toString())){
                continue;
            }
            if(map1.get(key) instanceof List){
                List list = (List) map1.get(key);
                List list2 = (List) map2.get(key);
                if(list2==null||list.size()!=list2.size()){
                    return false;
                }
                for (int i = 0; i < list.size(); i++) {
                    result = comparisonMap((Map) list.get(i), (Map) list2.get(i));
                    if(!result){
                        break;
                    }
                }
            } else {
                if(!comparison(map1.get(key),map2.get(key))){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private boolean comparison(Object o1,Object o2){
        if(o1 == null){
            return o2 == null;
        } else {
            if(o2 == null){
                return false;
            }
            return JSONObject.toJSONString(o1).equals(JSONObject.toJSONString(o2));
        }
    }

    private EngineRules findOnlineByRedis(Long id) {
        EngineRules engineRules= null;
        try{
            engineRules = (EngineRules) redisTemplate.opsForValue().get(RcsCache.RULESET_ONLINE.getHeader() +id);
        }
        catch (Exception e){
            LogUtil.error("redis查找规则集",id.toString(),"",e);
        }
        if( engineRules == null) {
            //从mysql获取
            EngineRulesOnline engineRulesOnline = engineRulesOnlineMapper.findRulesById(id);
            if(engineRulesOnline == null) {
                throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
            }
            engineRules = new EngineRules(engineRulesOnline);
            if(engineRulesOnline.getRulesJSON()!=null&&engineRulesOnline.getRulesJSON().length()>0) {
                List<EngineRule> engineRuleList = JSON.parseArray(engineRulesOnline.getRulesJSON(),EngineRule.class);
                engineRules.setRuleList(engineRuleList);
            }
            saveEngineRulesOnlineInRedis(engineRules,true);
        }
        return engineRules;
    }

    private EngineRules findConfigByRedis(Long id) {
        EngineRules engineRules= null;
        try{
            engineRules = (EngineRules) redisTemplate.opsForValue().get(RcsCache.RULESET_CONF.getHeader() +id);
        } catch (Exception e){
            LogUtil.error("redis查找规则集",id.toString(),"",e);
        }
         if(engineRules == null ){
            //从mysql获取
            engineRules = engineRulesMapper.findRulesById(id);
            if(engineRules == null) {
                throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
            }
            if(StringUtils.isEmpty(engineRules.getParameters())){
                String params = engineInterService.getInterParams(id);
                engineRules.setParameters(params);
                engineRulesMapper.updateParams(id, params);
            }
            if(engineRules.getRuleJson()!=null&&engineRules.getRuleJson().length()>0){
                engineRules.setRuleList(JSON.parseArray(engineRules.getRuleJson(),EngineRule.class));
            }
            saveEngineRulesOnlineInRedis(engineRules,false);
        }
        return engineRules;
    }

    @Override
    public EngineRules findAllByIdForTest(Long id) {
        //从mysql获取
        EngineRules engineRules = findAllByIdForView(id);
        if(engineRules.getRuleList()!=null){
            List<EngineRule> list = new ArrayList<>();
            for (EngineRule engineRule : engineRules.getRuleList()) {
                if(engineRule.getActive()==1){
                    list.add(engineRule);
                }
            }
            engineRules.setRuleList(list);
        }
        engineRules.setRuleJson(null);
        return engineRules;
    }


    @Override
    public EngineRules selectById(Long id) {
        return engineRulesMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long id) {
        engineRulesMapper.deleteByPrimaryKey(id);
        deleteRedisCache(id,false);
    }

    @Override
    public void updateEngineRules(EngineRules engineRules) {
        engineRules.setVerify(0);
        engineRulesMapper.updateByPrimaryKeySelective(engineRules);
        //更新规则集参数
        updateRuleSetParams(engineRules.getId());
        deleteRedisCache(engineRules.getId(),false);
    }

    @Override
    public void updateRuleSetParams(Long id){
        String params = engineInterService.getInterParams(id);
        engineRulesMapper.updateParams(id, params);
        deleteRedisCache(id,false);
    }

    private void saveEngineRulesOnlineInRedis(EngineRules engineRules,boolean isOnline){
        try{
            if (redisSwitch) {
                //修改完成后更新缓存
                redisTemplate.opsForValue().set((isOnline? RcsCache.RULESET_ONLINE.getHeader(): RcsCache.RULESET_CONF.getHeader())+engineRules.getId(),engineRules,86400, TimeUnit.SECONDS);
            }
        }
        catch (Exception e) {
            //TODO: redis连接异常时处理，推送邮件，打印日志等等
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRedisCache(Long id, boolean isOnline){
        try{
            redisTemplate.delete((isOnline ? RcsCache.RULESET_ONLINE.getHeader() : RcsCache.RULESET_CONF.getHeader()) + id);
            updateRulesModifyFlag(id);
        } catch (Exception e) {
            //TODO: redis连接异常时处理，推送邮件，打印日志等等
            e.printStackTrace();
        }
    }

    @Override
    public EngineRules addEngineRules(EngineRules engineRules) {
        engineRulesMapper.insertSelective(engineRules);
        return engineRules;
    }

    @Override
    public List<EngineRules> getRulesList(Long userId) {
        return engineRulesMapper.getRulesList(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateEngineRulesVerify(Approval approval) {
        EngineRules engineRules = new EngineRules();
        engineRules.setVerify(1);
        engineRules.setId(approval.getOnlyId());
        engineRulesMapper.updateEngineRulesVerify(engineRules);
        deleteRedisCache(approval.getOnlyId(),false);
    }

    @Override
    public void updateRulesSelect(PatchData patchData) {
        engineRulesMapper.updateRulesSelect(patchData);
        deleteRedisCache(patchData.getOnlyId(),false);
    }

    @Override
    public EngineRules getRuleSetAndRuleInfo(Long id) {
        return engineRulesMapper.getRuleSetAndRuleInfo(id);
    }

    @Override
    public void updateEngineRulesNo(EngineRules engineRules) {
        engineRulesMapper.updateByPrimaryKeySelective(engineRules);
        deleteRedisCache(engineRules.getId(),false);
    }

    @Override
    public Page<EngineRules> findAllByPage(EngineRules ruleSet) {
        PageHelper.startPage(ruleSet.getPageNo(), ruleSet.getPageSize());
        return engineRulesMapper.findAllByPage(ruleSet);
    }

    @Override
    public Page<EngineRules> findAll(EngineRules ruleSet) {
        PageHelper.startPage(ruleSet.getPageNo(), ruleSet.getPageSize());
        return engineRulesMapper.findAll(ruleSet);
    }

    @Override
    public void deleteAbsolute(Long id) {
        engineRulesMapper.deleteByPrimaryKey(id);
        deleteRedisCache(id,false);
    }

    @Override
    public EngineRules findAllByIdForDelete(Long id) {
        return findAllByIdForView(id);
    }

    @Override
    public EngineHistoryLog findAllByIdFromHistory(Approval approval) {
        return engineRulesMapper.findAllByIdFromHistory(approval);
    }

    @Override
    public EngineRules findAllByIdForView(Long id) {
        //从mysql获取
        EngineRules engineRules;
        try{
            engineRules = findConfigByRedis(id);
        } catch (Exception e){
            LogUtil.error("redis查找规则集",id.toString(),"",e);
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        Integer modifyFlag = (Integer) redisTemplate.opsForValue().get(RcsCache.RULESET_MODIFY.getHeader() +id);
        if(modifyFlag==null){
            modifyFlag = updateRulesModifyFlag(id);
        }
        engineRules.setModifyFlag(modifyFlag);
        return engineRules;
    }

    @Override
    public EngineRules reviewEngineRuleSet(EngineRules engineRules){

        if(engineRules == null){
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        if(StringUtils.isEmpty(engineRules.getParameters())){
            String params = engineInterService.getInterParams(engineRules.getId());
            engineRules.setParameters(params);
            engineRulesMapper.updateParams(engineRules.getId(), params);
        }

        if(engineRules.getRuleList() == null || engineRules.getRuleList().isEmpty()){
            List<EngineRule> ruleList = engineRulesMapper.findRuleByRulesId(engineRules.getId());
            List<Conditions> conditionsList = engineRulesMapper.findConditionByRulesId(engineRules.getId());
            List<EngineField> fieldList = engineRulesMapper.findFieldByRulesId(engineRules.getId());

            try {
                if(ruleList.isEmpty() || conditionsList.isEmpty() || fieldList.isEmpty()){
                    throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
                }
                for (EngineRule rule : ruleList) {
                    if(rule.getConditionRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    List<Conditions> conditions = new ArrayList<>();
                    for (Conditions condition : conditionsList) {
                        if(condition.getRuleId().longValue() == rule.getId()){
                            conditions.add(condition);
                        }
                    }
                    rule.setConditionsList(conditions);
                }

                for (Conditions conditions : conditionsList) {
                    List<EngineField> fields = new ArrayList<>();
                    if(conditions.getFieldRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    for (EngineField field : fieldList) {
                        if(field.getConditionId() != null && field.getConditionId().longValue() == conditions.getId()){
                            fields.add(field);
                        }
                    }
                    conditions.setFieldList(fields);
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
            }
            engineRules.setRuleList(ruleList);
        }
        return engineRules;
    }

    @Override
    public List<EngineRules> getActiveRules(Long uniqueCode) {
        return engineRulesMapper.getActiveRules(uniqueCode);
    }

    @Override
    public List<EngineRules> selectByName(String name,Long uniqueCode) throws RcsException {
        if (BlankUtil.isBlank(name) || BlankUtil.isBlank(uniqueCode)){
            return null;
        }
        return engineRulesMapper.selectByName(name,uniqueCode);
    }

    @Override
    public List<Map<String,Object>> getApiEventData() {
        return engineRulesMapper.getApiEventData();
    }

    @Override
    public EngineRulesOnline selectOnlineById(Long id) {
        return engineRulesOnlineMapper.findRulesById(id);
    }

    @Override
    public void updateEngineRulesOnline(EngineRulesOnline engineRules){
         engineRulesOnlineMapper.updateRuleSetVerify(engineRules);
        deleteRedisCache(engineRules.getId(),true);
    }

    @Override
    public void updateEngineRulesStatus(Long id) {
        engineRulesMapper.updateEngineRulesStatus(id);
        deleteRedisCache(id,true);
    }

    @Override
    public Long findRulesInUsed(Long id) {
        return engineRulesMapper.findRulesInUsed(id);
    }

//    /**
//     * 检测json表是否存在此规则集
//     * @param allByIdForView 规则实体
//     */
//    @Override
//    public void queryRuleSetHandle(EngineRules allByIdForView) {
//
//        Long engineRulesId = allByIdForView.getId();
//        Boolean engineRulesExit = engineParametersRecentMapper.queryEngineRulesExit(engineRulesId) > 0;
//        EngineParametersRecent epr = new EngineParametersRecent();
//        epr.setRulesId(engineRulesId);
//        epr.setRulesName(allByIdForView.getName());
//
//        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(allByIdForView);
//        epr.setRuleSet(jsonObject.toString());
//
//        epr.setActive(1);
//        epr.setAddUser(allByIdForView.getAddUser());
//        epr.setUniqueCode(allByIdForView.getUniqueCode());
//        epr.setCreateTime(TimeUtil.dqsj());
//        epr.setUpdateTime(TimeUtil.dqsj());
//
//        if (!engineRulesExit) {
//            //不存在则新增
//            engineParametersRecentMapper.add(epr);
//        } else {
//            //存在则更新
//            engineParametersRecentMapper.update(epr);
//        }
//        //保存日志
//        engineParametersRecentMapper.addLog(epr);
//    }

    @Override
    public RulesExcelUtil exportRulesToExcel(EngineRules rules, String fileName) {

        Map<String,String> rulesInfoMap = new HashMap<>();
        rulesInfoMap.put("businessName",sceneService.getBusinessNameById(rules.getBusinessId()));
        EngineScene engineScene = sceneService.getSceneById(rules.getSenceId());
        String sceneName = engineScene == null ? "" : engineScene.getName();
        rulesInfoMap.put("sceneName",sceneName);
        List<Map> fieldInfoList = fieldService.getFieldMappingInfo();
        return new RulesExcelUtil(rules,rulesInfoMap,fieldInfoList);
    }

    @Override
    public Long importRuleSet(MultipartFile file, String rulesName) throws Exception{

        List<Map> fieldInfoList = fieldService.getFieldMappingInfo();
        RulesExcelUtil util = new RulesExcelUtil();
        //从Excel解析数据
        String rulesJson = util.getRulesFromExcel(file,fieldInfoList);
        //校验补充数据
        rulesJson = supplementRuleSet(rulesJson);
        //导入到数据库
//        EngineRules engineRules = ruleTemplateController.importEngineRulesByJson(rulesJson,rulesName);

        return null;
    }

    @Override
    public void checkRules(EngineRules engineRules, SysUser user) {
        if (engineRules == null || !engineRules.getUniqueCode().equals(user.getUniqueCode())) {
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        if(engineRules.getSenceId()==null||engineRules.getBusinessId()==null){
            throw new RcsException(StatusCode.RULE_SENCE_ERROR.getMessage(), StatusCode.RULE_SENCE_ERROR.getCode());
        }
        if(engineRules.getRuleList()==null||engineRules.getRuleList().isEmpty()){
            throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
        } else {
            for (EngineRule engineRule : engineRules.getRuleList()) {
                if((engineRules.getMatchType()==0&&engineRule.getThreshold()==null)){
                    throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
                } else if(engineRules.getMatchType()==1&&engineRule.getLevel()==null){
                    throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
                }
                if(engineRule.getConditionsList()==null||engineRule.getConditionsList().isEmpty()){
                    throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
                } else {
                    for (Conditions conditions : engineRule.getConditionsList()) {
                        if(conditions.getFieldList()==null||conditions.getFieldList().isEmpty()){
                            throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
                        }
                    }
                }
            }
        }
    }

    /**
     * 校验、补充从Excel解析出来的规则集数据
     * @param ruleSetJson
     * @return
     */
    private String supplementRuleSet(String ruleSetJson){

        //将json串转为Object对象
        JSONObject jsonObject = JSONObject.parseObject(ruleSetJson);
        //json对象转换成java对象
        EngineRules engineRules = JSONObject.toJavaObject(jsonObject,EngineRules.class);
        //校验规则集
        String parameter = rulesJsonCheck(engineRules);
        engineRules.setParameters(parameter);
        return JSONObject.toJSONString(engineRules);
    }

    /**
     * 对规则集进行校验，并且返回规则集的入参
     * @param engineRules
     * @return
     */
    private String rulesJsonCheck(EngineRules engineRules){

        if(engineRules == null){
            throw new RcsException("当前规则集为空，不能导入！");
        }else if(engineRules.getRuleList() == null){
            throw new RcsException("当前规则集为空，不能导入！");
        }
        Map<String,String> parameter = new HashMap<>();
        List<EngineRule> ruleList = engineRules.getRuleList();
        for (EngineRule engineRule : ruleList) {
            if(engineRule.getConditionRelationship() == null || engineRule.getConditionRelationship() == ""){
                throw new RcsException("当前规则集条件关联关系为空，不能导入！");
            }
            String conditionRelationShip = engineRule.getConditionRelationship();
            List<Conditions> conditionsList = engineRule.getConditionsList();
            if(conditionsList == null){
                throw new RcsException("当前规则集条件为空，不能导入！");
            }
            for (Conditions conditions : conditionsList) {
                conditionRelationShip = conditionRelationShip.replace(conditions.getId().toString(),"true");
                if(conditions.getFieldRelationship() == null || "".equals(conditions.getFieldRelationship())){
                    throw new RcsException("当前规则集字段关联关系为空，不能导入！");
                }
                if(conditions.getFieldList() == null){
                    throw new RcsException("当前规则集字段为空，不能导入！");
                }
                String fieldRelationShip = conditions.getFieldRelationship();
                List<EngineField> engineFields = conditions.getFieldList();
                for (EngineField engineField : engineFields){
                    Integer fieldId = engineField.getFieldId();
                    //函数集字段处理
                    if (engineField.getFunctionSet() != null && engineField.getFunctionSet().size() > 0){
                        for (FunctionField functionField : engineField.getFunctionSet()){
                            EngineInter engineInter = engineInterService.getEngineInterByFieldId(Long.valueOf(functionField.getFieldId()));
                            if (engineInter == null){
                                throw new RcsException("函数集Sheet表中的字段key请勿随意修改！");
                            }
                            String paramsJson = engineInter.getParameters();
                            Map<String,String> params = JSONUtil.jsonToMap(paramsJson);
                            parameter.putAll(params);
                        }
                        continue;
                    }
                    //正常字段处理
                    EngineInter engineInter = engineInterService.getEngineInterByFieldId(fieldId.longValue());
                    if (engineInter == null){
                        throw new RcsException("规则集Sheet表中的字段key请勿随意修改！");
                    }
                    String paramsJson = engineInter.getParameters();
                    Map<String,String> params = JSONUtil.jsonToMap(paramsJson);
                    parameter.putAll(params);
                    fieldRelationShip = fieldRelationShip.replace(engineField.getId().toString(),"true");
                }
                checkRelationShipIsTrue(fieldRelationShip);
            }
            //对条件关联关系进行校验
            checkRelationShipIsTrue(conditionRelationShip);

        }
        return JSONUtil.beanToJson(parameter);
    }

    /**
     * 对表达式语法进行校验
     * @param relationShip
     * @return
     */
    private boolean checkRelationShipIsTrue(String relationShip){

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try{
            Object result = engine.eval(relationShip);
        }catch (ScriptException e){
            throw new RcsException("表达式语法错误，请检查后重试！");
        }
        return true;
    }

}
