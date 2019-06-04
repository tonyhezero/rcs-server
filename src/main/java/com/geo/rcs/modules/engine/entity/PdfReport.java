package com.geo.rcs.modules.engine.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2019/2/20  8:14
 **/
public class PdfReport implements Report{

    //报告编号
    private Long number;
    //报告生成时间
    private Date createTime;
    //姓名
    private String name;
    //手机号
    private String mobile;
    //身份证号
    private String idCard;
    //手机运营商
    private String isp;
    //性别
    private String gender;
    //年龄
    private int age;
    //报告结果
    private int status;
    //规则集信息
    private List<Map<String,Object>> rulesList;
    //其他进件信息
    private Map<String,String> otherInfo;
    //风险规则列表
    private List<String> hitRuleList;
    // 规则命中详情--规则集名称_规则名称_条件_字段_字段结果
    private List<Map<String, Object>> hitRuleDetail;
    //命中规则分类
    private TreeMap<String,Map<String,Object>> fieldInfo;
    //失败信息
    private String failMsg;
    //规则决策标记 0：规则集 1：决策集
    private Integer flag;
    //图片资源
    private String imgBase;

    public String getImgBase() {
        return imgBase;
    }

    public void setImgBase(String imgBase) {
        this.imgBase = imgBase;
    }


    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }


    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }


    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String cid) {
        this.idCard = cid;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Map<String, Object>> getRulesList() {
        return rulesList;
    }

    public void setRulesList(List<Map<String, Object>> rulesList) {
        this.rulesList = rulesList;
    }

    public Map<String, String> getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Map<String, String> otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<String> getHitRuleList() {
        return hitRuleList;
    }

    public void setHitRuleList(List<String> hitRuleList) {
        this.hitRuleList = hitRuleList;
    }

    public List<Map<String, Object>> getHitRuleDetail() {
        return hitRuleDetail;
    }

    public void setHitRuleDetail(List<Map<String, Object>> hitRuleDetail) {
        this.hitRuleDetail = hitRuleDetail;
    }

    public TreeMap<String, Map<String, Object>> getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(TreeMap<String, Map<String, Object>> fieldInfo) {
        this.fieldInfo = fieldInfo;
    }


}
