package com.geo.rcs.modules.source.verified.client.entity;

import java.util.Date;

public class VerfiedLog  extends VerfiedLogBase{
    private Long id;

    private Date  querytime;

    private String startTime;

    private String endTime;

    private Integer verfiedtype;

    private String realname;

    private String cid;

    private String idnumber;

    private String requeststatus;

    private String result;

    private String parameters;

    private  String pageSize;

    private String pageNo;

    private String name;

    private Long user_id;

    private Long rule_id;

    private Long responseTime;

    private String ruleName;

    private String channel;
    //1.一键校验，不是等于1调用日志
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getQuerytime() {
        return querytime;
    }

    public void setQuerytime(Date querytime) {
        this.querytime = querytime;
    }

    public Integer getVerfiedtype() {
        return verfiedtype;
    }

    public void setVerfiedtype(Integer verfiedtype) {
        this.verfiedtype = verfiedtype;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(String requeststatus) {
        this.requeststatus = requeststatus == null ? null : requeststatus.trim();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters == null ? null : parameters.trim();
    }


    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getRule_id() {
        return rule_id;
    }

    public void setRule_id(Long rule_id) {
        this.rule_id = rule_id;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VerfiedLog{" +
                "id=" + id +
                ", querytime=" + querytime +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", verfiedtype=" + verfiedtype +
                ", realname='" + realname + '\'' +
                ", cid='" + cid + '\'' +
                ", idnumber='" + idnumber + '\'' +
                ", requeststatus='" + requeststatus + '\'' +
                ", result='" + result + '\'' +
                ", parameters='" + parameters + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", pageNo='" + pageNo + '\'' +
                ", name='" + name + '\'' +
                ", user_id=" + user_id +
                ", rule_id=" + rule_id +
                ", responseTime=" + responseTime +
                ", ruleName='" + ruleName + '\'' +
                ", channel='" + channel + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}