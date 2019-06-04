package com.geo.rcs.modules.rule.ruleset.entity;

import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.event.vo.EventStatEntry;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wp
 * @date Created in 11:23 2019/2/18
 */
public class EngineRulesOnline implements Serializable {
    //编号
    private Long id;
    //名称
    private String name;
    //业务类型编号
    private Integer businessId;
    //场景编号
    private Long senceId;
    //匹配类型
    private Integer matchType;
    //阈值
    private Integer threshold;
    //最小阈值
    private String thresholdMin;
    //最大阈值
    private String thresholdMax;
    //描述
    private String describ;
    //审核时间
    private Integer verify;
    //上线状态（0：未上线，1：上线）
    private Integer active;
    //添加人
    private String addUser;
    //添加时间
    private Date addTime;

    private Integer pageSize;

    private Integer pageNo;
    //关联客户编号
    private Long uniqueCode;
    //参数
    private String parameters;
    //
    private String rulesJSON;
    //类型名称
    private String typeName;
    //场景名称
    private String sceneName;
    //白名单过滤
    private String whiteFilter;
    //黑名单过滤
    private String blackFilter;
    //
    private EventStatEntry eventStatEntry;
    //命中结果
    private String result;
    //分数
    private Integer score;
    //版本号
    private String version;

    public EngineRulesOnline(){}

    public EngineRulesOnline(EngineRules engineRules){
        this.id = engineRules.getId();
        this.active = engineRules.getActive();
        this.addTime = engineRules.getAddTime();
        this.addUser = engineRules.getAddUser();
        this.blackFilter = engineRules.getBlackFilter();
        this.businessId = engineRules.getBusinessId();
        this.describ = engineRules.getDescrib();
        this.eventStatEntry = engineRules.getEventStatEntry();
        this.matchType = engineRules.getMatchType();
        this.name = engineRules.getName();
        this.pageNo = engineRules.getPageNo();
        this.pageSize = engineRules.getPageSize();
        this.parameters = engineRules.getParameters();
        this.result = engineRules.getResult();
        this.sceneName = engineRules.getSceneName();
        this.score = engineRules.getScore();
        this.senceId = engineRules.getSenceId();
        this.threshold = engineRules.getThreshold();
        this.thresholdMax = engineRules.getThresholdMax();
        this.thresholdMin = engineRules.getThresholdMin();
        this.typeName = engineRules.getTypeName();
        this.uniqueCode = engineRules.getUniqueCode();
        this.verify = engineRules.getVerify();
        this.whiteFilter = engineRules.getWhiteFilter();
        this.rulesJSON = JSONUtil.beanToJson(engineRules.getRuleList());
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Long getSenceId() {
        return senceId;
    }

    public void setSenceId(Long senceId) {
        this.senceId = senceId;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getThresholdMin() {
        return thresholdMin;
    }

    public void setThresholdMin(String thresholdMin) {
        this.thresholdMin = thresholdMin;
    }

    public String getThresholdMax() {
        return thresholdMax;
    }

    public void setThresholdMax(String thresholdMax) {
        this.thresholdMax = thresholdMax;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getRulesJSON() {
        return rulesJSON;
    }

    public void setRulesJSON(String rulesJSON) {
        this.rulesJSON = rulesJSON;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getWhiteFilter() {
        return whiteFilter;
    }

    public void setWhiteFilter(String whiteFilter) {
        this.whiteFilter = whiteFilter;
    }

    public String getBlackFilter() {
        return blackFilter;
    }

    public void setBlackFilter(String blackFilter) {
        this.blackFilter = blackFilter;
    }

    public EventStatEntry getEventStatEntry() {
        return eventStatEntry;
    }

    public void setEventStatEntry(EventStatEntry eventStatEntry) {
        this.eventStatEntry = eventStatEntry;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "EngineRulesOnline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", businessId=" + businessId +
                ", senceId=" + senceId +
                ", matchType=" + matchType +
                ", threshold=" + threshold +
                ", thresholdMin=" + thresholdMin +
                ", thresholdMax=" + thresholdMax +
                ", describ='" + describ + '\'' +
                ", verify=" + verify +
                ", active=" + active +
                ", addUser='" + addUser + '\'' +
                ", addTime=" + addTime +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", uniqueCode=" + uniqueCode +
                ", parameters='" + parameters + '\'' +
                ", rulesJSON='" + rulesJSON + '\'' +
                ", typeName='" + typeName + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", whiteFilter='" + whiteFilter + '\'' +
                ", blackFilter='" + blackFilter + '\'' +
                ", eventStatEntry=" + eventStatEntry +
                ", result='" + result + '\'' +
                ", score=" + score +
                '}';
    }
}
