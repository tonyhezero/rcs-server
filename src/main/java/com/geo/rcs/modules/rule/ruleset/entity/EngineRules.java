package com.geo.rcs.modules.rule.ruleset.entity;

import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.geo.rcs.modules.rule.entity.EngineRule;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author geo
 */
public class EngineRules implements Serializable {
    private static final long  serialVersionUID = -1L;

    /**
     * 编号
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 业务类型编号parameters
     */
    private Integer businessId;
    /**
     * 业务名字
     */
    private String businessName;
    /**
     * 场景编号
     */
    private Long senceId;
    /**
     * 匹配类型
     */
    private Integer matchType;
    /**
     * 阈值
     */
    private Integer threshold = 0;
    /**
     * 最小阈值
     */
    private String thresholdMin;
    /**
     * 最大阈值
     */
    private String thresholdMax;
    /**
     * 描述
     */
    private String describ;
    /**
     * 审核时间
     */
    private Integer verify;
    /**
     * 失效：激活状态（0：未激活，1：激活）
     */
    private Integer active;
    /**
     * 添加人
     */
    private String addUser;
    /**
     * 添加时间
     */
    private Date addTime;

    private Integer pageSize;

    private Integer pageNo;
    /**
     * 关联客户编号
     */
    private Long uniqueCode;
    /**
     * 参数
     */
    private String parameters;
    /**
     * 规则集合
     */
    private List<EngineRule> ruleList;
    private String ruleJson;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 场景名称
     */
    private String sceneName;
    /**
     * 白名单过滤
     */
    private String whiteFilter;
    /**
     * 黑名单过滤
     */
    private String blackFilter;

    private EventStatEntry eventStatEntry;
    /**
     * 命中结果
     */
    private String result;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 版本号
     */
    private String version;
    /**
     * 是否进行变更(0：未存在线上版本；1：未变更;2：已变更)
     */
    private Integer modifyFlag = 0;
    /**
     * 上线的审批状态
     */
    private Integer activeVerify;
    /**
     * 历史日志编号
     */
    private Long historyLogId;

    private Integer actionType;

    /**
     * 错误码
     */
    private List<String> errorCodes;

    /**
     * 接口名称
     */
    private String interName;

    public EngineRules(){}

    public EngineRules(EngineRulesOnline engineRules){
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public String getInterName() {
        return interName;
    }

    public void setInterName(String interName) {
        this.interName = interName;
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

    public List<EngineRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<EngineRule> ruleList) {
        this.ruleList = ruleList;
    }

    public String getRuleJson() {
        return ruleJson;
    }

    public void setRuleJson(String ruleJson) {
        this.ruleJson = ruleJson;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Integer modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public Integer getActiveVerify() {
        return activeVerify;
    }

    public void setActiveVerify(Integer activeVerify) {
        this.activeVerify = activeVerify;
    }

    public Long getHistoryLogId() {
        return historyLogId;
    }

    public void setHistoryLogId(Long historyLogId) {
        this.historyLogId = historyLogId;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "EngineRules{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", businessId=" + businessId +
                ", businessName='" + businessName + '\'' +
                ", senceId=" + senceId +
                ", matchType=" + matchType +
                ", threshold=" + threshold +
                ", thresholdMin='" + thresholdMin + '\'' +
                ", thresholdMax='" + thresholdMax + '\'' +
                ", describ='" + describ + '\'' +
                ", verify=" + verify +
                ", active=" + active +
                ", addUser='" + addUser + '\'' +
                ", addTime=" + addTime +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", uniqueCode=" + uniqueCode +
                ", parameters='" + parameters + '\'' +
                ", ruleList=" + ruleList +
                ", ruleJson='" + ruleJson + '\'' +
                ", typeName='" + typeName + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", whiteFilter='" + whiteFilter + '\'' +
                ", blackFilter='" + blackFilter + '\'' +
                ", eventStatEntry=" + eventStatEntry +
                ", result='" + result + '\'' +
                ", score=" + score +
                ", version='" + version + '\'' +
                ", modifyFlag=" + modifyFlag +
                ", activeVerify=" + activeVerify +
                ", historyLogId=" + historyLogId +
                ", actionType=" + actionType +
                ", errorCodes=" + errorCodes +
                '}';
    }
}