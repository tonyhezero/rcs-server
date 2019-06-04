package com.geo.rcs.modules.source.handler;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.engine.entity.Condition;
import com.geo.rcs.modules.engine.entity.Field;
import com.geo.rcs.modules.engine.entity.Rule;
import com.geo.rcs.modules.engine.entity.Rules;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.geo.rcs.modules.source.entity.InterStatusCode.ECLKEYS;

/**
 * 该模块主要功能：
 * 1、填充数据源数据到规则集配置
 * 2、保存数据源引擎置大数据平台
 *      日志存储格式：
 *          字段名"\001"字段类型"\001"接口名"\001"数据源"\001"字段结果值"\001"结果描述"\001"调用时间\n
 */


@Service
public class SourceFactory {
    private static Logger logger = Logger.getLogger(SourceFactory.class);
//    private static final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(ConstantThreadPool.TASK_QUEUE_SIZE.getParm());
//    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(ConstantThreadPool.CORE_POOL_SIZE.getParm(),ConstantThreadPool.MAXIMUM_POOL_SIZE.getParm(),ConstantThreadPool.KEEP_ALIVE_TIME.getParm(), ConstantThreadPool.TIME_UNIT.getTimeUtil(), queue);
//    private static final Integer MAX_TASK_NUM=ConstantThreadPool.MAX_TASK_NUM.getMaxTask();


    public static Map<String,String> rulesDataPackager(Rules rules, Map<String, Map> rulesData , Map<String,String> interMatchType) {
        List<Rule> ruleSet = rules.getRuleList();
        //日志存储
        StringBuilder messageLog = new StringBuilder();
        logger.info("【规则集】-字段解析rules："+rules.toString());
        if(rulesData != null && !rulesData.isEmpty()){
            logger.info("【规则集】-返回字段解析rules："+rulesData.toString());
            try{
                for (Rule rule: ruleSet) {
                    for (Condition con: rule.getConditionsList()) {
                        for (Field field:con.getFieldList()) {
                            parseFileValue(field,rulesData,messageLog,interMatchType,field.getStrategy());
                            if(field.getFunctionSet()!=null){
                                field.getFunctionSet().forEach(functionField -> parseFileValue(functionField,rulesData,messageLog,interMatchType,field.getStrategy()));
                            }
                        }
                    }
                }
                rules.setSourceData(rulesData);
            }catch (NullPointerException e){
                logger.error("【规则集】-字段解析异常",e);
                throw new RcsException(StatusCode.FIELD_ERROR.getMessage(), StatusCode.FIELD_ERROR.getCode());
            }
        }else{
            throw new RcsException(StatusCode.FIELD_ERROR.getMessage(), StatusCode.FIELD_ERROR.getCode());
        }

        Map<String,String> result = new HashMap<>(2);
        result.put("datasourceLog",messageLog.toString());
        result.put("rulesConfig",JSONObject.toJSONString(rules));

        return result;
    }

    /**
     * 为字段赋值
     * @param field
     * @param rulesData
     * @param messageLog
     * @param interMatchType
     */
    private static void parseFileValue(Field field, Map<String, Map> rulesData, StringBuilder messageLog, Map<String, String> interMatchType, int strategy){
        String fieldName = field.getFieldName();
        String fieldType = field.getFieldType();
        Map fieldValue = rulesData.get(fieldName);

        if(fieldValue != null && !fieldValue.isEmpty()){
            if(fieldValue.get("value")!=null){
                field.setValue(fieldValue.get("value").toString());
            }
            if(fieldValue.get("value") != null && ECLKEYS.contains(fieldValue.get("value").toString())){
                fieldValue.put("value","");
                field.setValue(fieldValue.get("value").toString());
            }
            if(fieldValue.get("value") != null && fieldType.toUpperCase().equals("DATE") ){
                field.setValue(FieldFormattor.DateFormattor(fieldValue.get("value").toString()));
                fieldValue.put("value", field.getValue());
            }else if(fieldValue.get("value") != null && fieldType.toUpperCase().equals("DATETIME")){
                field.setValue(FieldFormattor.DatetimeFormattor(fieldValue.get("value").toString()));
                fieldValue.put("value", field.getValue());
            }
            if(strategy==1 && (fieldValue.get("value") == null || fieldValue.get("value") == "")){
                field.setValue("0");
            }
            field.setValueDesc(fieldValue.get("valueDesc").toString());
            fieldValue.put("showName",field.getShowName());
            rulesData.put(fieldName,fieldValue);
        } else {
            if(strategy==1){
                field.setValue("0");
            } else {
                field.setValue("");
            }
            field.setValueDesc("源数据无该字段信息");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}

