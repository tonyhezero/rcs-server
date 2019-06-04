package com.geo.rcs.modules.rule.ruleset.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.redis.IDGenerator;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRulesOnline;
import com.geo.rcs.modules.rule.ruleset.service.RecordRulesLogService;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.geo.rcs.modules.rule.util.RulesExcelUtil;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:53
 */
@RestController
@RequestMapping("/rule/ruleSet")
public class RuleSetController extends BaseController {

    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private PatchDataService patchDataService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EngineRuleService engineRuleService;
    @Autowired
    private RecordRulesLogService recordRulesLogService;
    @Autowired
    private IDGenerator idGenerator;

    @RequestMapping("/query")
    public Geo queryRuleSetList(EngineRules ruleSet) {
        Geo geo = Geo.ok();
        try {
            //获取今日时间范围
            DateUtils.DateRange dateRange = DateUtils.getTodayRange();

            //设置查询参数
            Map<String, Object> map = new HashMap<>();
            map.put("startTime", DateUtils.format(dateRange.getStart(), "yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", DateUtils.format(dateRange.getEnd(), "yyyy-MM-dd HH:mm:ss"));
            map.put("userId", getUser().getUniqueCode());
            map.put("senceId", ruleSet.getSenceId());
            map.put("businessId", ruleSet.getBusinessId());

            //获取昨日进件总量
            Map<String,Object> stat = new HashMap<>();
            List<EventStatEntry> list = eventService.yesterdayEventStat(map);
            if(list != null && !list.isEmpty()){
                stat = JSONUtil.beanToMap(list.get(0));
            }

            //获取所有规则集
            ruleSet.setUniqueCode(getUser().getUniqueCode());
            PageInfo<EngineRules> pageInfo = new PageInfo<>(ruleSetService.findByPage(ruleSet));

            //根据规则集id获取昨日对应进件量
            List<EngineRules> ruleSetList = pageInfo.getList();
            List<Long> rulesIdList = new ArrayList<>();
            if(ruleSetList != null && !ruleSetList.isEmpty()){
                for (EngineRules rules : ruleSetList) {
                    rulesIdList.add(rules.getId());
                }
                map.put("rulesIdList", rulesIdList);
                List<EventStatEntry> listEntry = eventService.yesterdayEventStat(map);
                if(listEntry != null && !listEntry.isEmpty()){
                    for (EngineRules rules : ruleSetList) {
                        for (EventStatEntry entry : listEntry) {
                            if(rules.getId().equals(entry.getRulesId())){
                                rules.setEventStatEntry(entry);
                            }
                        }
                    }
                }
            }
            pageInfo.setList(ruleSetList);

            stat.put("ruleSetCount", pageInfo.getTotal());
            geo.put("stat", stat);
            geo.put("data", pageInfo);
        }catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
        }

        return geo;
    }

    /**
     * 前台查询规则管理列表（模糊，分页）
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("/list")
    public void getRuleSetList(HttpServletRequest request, HttpServletResponse response) {
        try {
            //添加unique_code （客户唯一标识）
            this.sendData(request, response, new PageInfo<>(ruleSetService.getRulesList(getUser().getUniqueCode())));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 后台查询所有规则管理列表（模糊，分页）
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param ruleSet 规则集信息
     */
    @RequestMapping("/allList")
    public void allList(HttpServletRequest request, HttpServletResponse response, EngineRules ruleSet) {
        try {
            //添加unique_code （客户唯一标识）
            ruleSet.setUniqueCode(getGeoUser().getUniqueCode());

            this.sendData(request, response, new com.geo.rcs.modules.sys.entity.PageInfo<>(ruleSetService.findAllByPage(ruleSet)));

            this.sendOK(request, response);

        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 根据用户获取规则集（包括规则，条件，字段） json形式返回
     */
    @RequestMapping("/getRuleSet")
    public void getRuleSet(HttpServletRequest request, HttpServletResponse response, Long id, Integer type){
        try {
            EngineRules engineRules;
            if(type==null||type == 0){
                engineRules = ruleSetService.findAllByIdForView(id);
            } else {
                engineRules = ruleSetService.findAllById(id,false);
            }
            engineRules.setRuleJson(null);
            this.sendData(request, response,engineRules);
        } catch (RcsException e){
            this.sendData(request, response,null);
        }catch (Exception e) {
            e.printStackTrace();
            this.sendError(request, response, "获取规则集失败！");
        }
    }

    /**
     * 添加规则集时先纳入审批
     * @param engineRules 规则集信息
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("/toApprovalAdd")
    public void toApprovalAdd(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){
        try {
            engineRules.setUniqueCode(getUserId());
            engineRules.setAddUser(getUser().getName());
            engineRules.setAddTime(new Date());
            engineRules.setVerify(0);
            ruleSetService.addEngineRules(engineRules);
            EngineRules engineRules1 = ruleSetService.selectById(engineRules.getId());
            EngineScene byId = sceneService.findById(engineRules1.getSenceId());
            Approval approval = new Approval();
            approval.setActionType(1);
            if(engineRules.getThresholdMin()!=null&&!"".equals(engineRules.getThresholdMin())){
                approval.setThresholdMin(Integer.valueOf(engineRules.getThresholdMin()));
            }
            if(engineRules.getThresholdMax()!=null&&!"".equals(engineRules.getThresholdMax())){
                approval.setThresholdMax(Integer.valueOf(engineRules.getThresholdMax()));
            }
            approval.setScene(byId.getName());
            approval.setBusinessId(engineRules.getBusinessId());
            approval.setObjId(2);
            approval.setThreshold(engineRules.getThreshold());
            approval.setOnlyId(engineRules1.getId());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(engineRules.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后添加规则集
     */
    @RequestMapping("/confirmAdd")
    public void addRules(@Validated EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){
        String describ = request.getParameter("describ");
        setDescrib(describ,engineRules);
        try {
            //查询该规则集是否存在
            List<EngineRules> list = ruleSetService.selectByName(engineRules.getName(),getUser().getUniqueCode());
            if (list.size() >= 1){
                this.sendError(request, response, "规则集名已存在！");
                return;
            }
            engineRules.setUniqueCode(getUser().getUniqueCode());
            engineRules.setAddUser(getUser().getName());
            engineRules.setAddTime(new Date());
            engineRules.setRuleJson(JSON.toJSONString(engineRules.getRuleList()==null?new ArrayList<>():engineRules.getRuleList()));
            EngineRules engineRules1 = ruleSetService.addEngineRules(engineRules);
            this.sendData(request,response,engineRules1);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新场景失败！");
        }
    }

    private void setDescrib(String describ,EngineRules engineRules){
        try {
            if(describ != null) {
                String decode = URLDecoder.decode(describ, "utf-8");
                engineRules.setDescrib(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改规则集时先纳入审批
     * @param patchData 审批信息
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("/toApprovalUpdate")
    public void toApprovalUpdate(PatchData patchData, HttpServletRequest request, HttpServletResponse response){
        ResultType resultType = ValidateNull.check(patchData, NotNull.RequestType.UPDATE);

        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            patchData.setActionId(2);
            patchData.setObjId(2);
            patchDataService.insertSelective(patchData);
            EngineScene byId = sceneService.findById(patchData.getSceneId());
            Approval approval = new Approval();
            approval.setActionType(2);
            approval.setScene(byId.getName());
            approval.setBusinessId(patchData.getBusinessId());
            approval.setObjId(2);
            approval.setThresholdMin(patchData.getThresholdMin());
            approval.setThresholdMax(patchData.getThresholdMax());
            approval.setThreshold(patchData.getThreshold());
            approval.setOnlyId(patchData.getOnlyId());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(patchData.getDescribtion());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 修改规则集(需要查看此规则集激活状态)
     */
    @RequestMapping("/confirmUpdate")
    public void updateRules(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){

        String describ = request.getParameter("describ");
        setDescrib(describ,engineRules);
        ResultType resultType = ValidateNull.check(engineRules, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }

        try {
            EngineRules engineRules1 = ruleSetService.selectById(engineRules.getId());
            if(engineRules1.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            }
            else {
                List<EngineRules> lists = ruleSetService.selectByName(engineRules.getName(),getUser().getUniqueCode());
                if (lists != null) {
                    for (EngineRules list : lists) {
                        if (list.getId() != (long) engineRules.getId()) {
                            this.sendError(request, response, "规则集名字重复！");
                            return;
                        }
                    }
                }
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "更新规则集失败！");
        }
    }
    /**
     * 修改规则集激活状态
     */
    @RequestMapping("/updateActive")
    public void updateActive(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){
        try {
            EngineRulesOnline engineRules1 = ruleSetService.selectOnlineById(engineRules.getId());
            if(engineRules1 == null){
                this.sendError(request, response,"该规则集尚未发布通过，无法上线");
                return;
            }
            if(engineRules1.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
                return;
            }
            if(engineRules.getActive().equals(engineRules1.getActive())){
                this.sendErrAc(request, response,"此规则集已上线,不能进行此操作");
                return;
            }
            request.getSession().setAttribute("engineRules", engineRules);
            Approval approval = new Approval();
            EngineScene byId = sceneService.findById(engineRules1.getSenceId());
            if(engineRules.getActive()==0){
                approval.setActionType(6);
            } else {
                approval.setActionType(5);
            }
            approval.setObjId(2);
            if(!ValidateNull.isNull(byId)){
                approval.setScene(byId.getName());
            }
            approval.setBusinessId(engineRules1.getBusinessId());
            if(engineRules.getThresholdMin()!=null&&!"".equals(engineRules.getThresholdMin())){
                approval.setThresholdMin(Integer.valueOf(engineRules.getThresholdMin()));
            }
            if(engineRules.getThresholdMax()!=null&&!"".equals(engineRules.getThresholdMax())){
                approval.setThresholdMax(Integer.valueOf(engineRules.getThresholdMax()));
            }
            approval.setOnlyId(engineRules1.getId());
            approval.setDescription(engineRules1.getName());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUser().getUniqueCode());
            approvalService.publicToApproval(approval);
            engineRules1.setVerify(1);
            ruleSetService.updateEngineRulesOnline(engineRules1);
            this.sendOK(request, response);
        } catch (RcsException e) {
            this.sendError(request, response, "上线规则集失败！");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除规则集时先纳入审批
     * @param id id
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("/toApprovalDelete")
    public void toApprovalDelete(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            Long ruleId = ruleSetService.findRulesInUsed(id);
            if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            } else if(ruleId != null){
                this.sendErrAc(request, response,"此规则集正在使用中,不能进行此操作");
            }else {
                engineRules.setVerify(1);
                request.getSession().setAttribute("engineRules", engineRules);
                Approval approval = new Approval();
                EngineScene byId = sceneService.findById(engineRules.getSenceId());
                approval.setActionType(3);
                approval.setObjId(2);
                if(!ValidateNull.isNull(byId)){
                    approval.setScene(byId.getName());
                }
                approval.setBusinessId(engineRules.getBusinessId());
                if(engineRules.getThresholdMin()!=null&&!"".equals(engineRules.getThresholdMin())){
                    approval.setThresholdMin(Integer.valueOf(engineRules.getThresholdMin()));
                }
                if(engineRules.getThresholdMax()!=null&&!"".equals(engineRules.getThresholdMax())){
                    approval.setThresholdMax(Integer.valueOf(engineRules.getThresholdMax()));
                }
                approval.setOnlyId(id);
                approval.setDescription(engineRules.getName());
                approval.setSubmitter(getUser().getName());
                approval.setSubTime(new Date());
                approval.setUniqueCode(getUser().getUniqueCode());
                ruleSetService.updateEngineRulesVerify(approval);
                approvalService.deleteToApproval(approval);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后删除规则集
     */
    @RequestMapping("/confirmDelete")
    public void deleteRules(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }else {
                ruleSetService.delete(id);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }

    }
    /**
     * 删除规则集所有关联
     */
    @RequestMapping("/deleteAbsolute")
    public void deleteAbsolute(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }else {
                ruleSetService.deleteAbsolute(id);
                this.sendOK(request, response);
            }
        } catch (RcsException e) {
            this.sendError(request, response, "删除失败！");
        }

    }
    /**
     * 获取所属规则集列表
     */
    @RequestMapping("/getRulesType")
    public void getBusType(HttpServletRequest request, HttpServletResponse response){
        try {
            this.sendData(request, response,ruleSetService.getRulesList(getUser().getUniqueCode()));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取业务类型列表失败！");
        }
    }
    /**
     * 获取规则集信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
            return;
        }
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            this.sendData(request, response, engineRules);
        } catch (RcsException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取规则集以及规则信息，用于回显数据
     */
    @RequestMapping("/getRuleSetAndRuleInfo")
    public void getRuleSetAndRuleInfo(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        try {
            List<EngineRules> engineRules = engineRuleService.selectRulesById(id);
            if(engineRules.size()  == 0){
                this.sendData(request, response,ruleSetService.selectById(id));
            }else {
                this.sendData(request, response, ruleSetService.getRuleSetAndRuleInfo(id));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看规则集历史变更记录
     */
    @RequestMapping("/queryRulesLog")
    public Geo queryRulesLog(@Validated EngineHistoryLog engineHistoryLog){
        try {
            if(engineHistoryLog.getRuleSetId() != null){
                engineHistoryLog.setUniqueCode(getUser().getUniqueCode());
                return Geo.ok().put("data",new PageInfo<>(recordRulesLogService.getRecordById(engineHistoryLog)));
            }
            else{
                return Geo.error(StatusCode.RULES_NOTFOUND_ERROR.getCode(), StatusCode.RULES_NOTFOUND_ERROR.getMessage());
            }

        } catch (NumberFormatException e) {
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage());
        } catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }

    }
    /**
     * 删除规则集历史变更记录
     */
    @RequestMapping("/deleteRulesLog")
    public Geo deleteRulesLog(Long logId){
        try {
            recordRulesLogService.deleteById(logId);
            return Geo.ok();
        } catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }

    }

    /**
     * 删除规则集历史变更记录
     */
    @RequestMapping("/getActiveRules")
    public Geo getActiveRules(){
        try {
            List<EngineRules> engineRulesList = ruleSetService.getActiveRules(getUser().getUniqueCode());
            return Geo.ok().put("engineRulesList",engineRulesList);
        } catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }

    }

    /**
     * 获取测试数据
     */
    @RequestMapping("/getApiEventData")
    public Geo getApiEventData(){
        try {
            return Geo.ok().put("engineRulesList",ruleSetService.getApiEventData());
        } catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }

    }

    /**
     * 将配置表中激活规则集发布上线
     */
    @RequestMapping(value = "/publishAllRuleSet")
    public Geo publishAllRuleSet() {
        return Geo.ok();
    }

    /**
     * 修改规则集
     */
    @RequestMapping(value = "/updateRuleSet")
    public void updateRuleSet(@RequestBody String ruleSetJson, HttpServletRequest request, HttpServletResponse response){
        try {
            System.out.println(ruleSetJson);
            EngineRules ruleSet = JSONObject.parseObject(ruleSetJson,EngineRules.class);
            EngineRules engineRules = ruleSetService.findAllByIdForView(ruleSet.getId());
            if (engineRules.getVerify() == 1) {
                this.sendError(request, response, "规则集审核中，请等待审核结果后操作");
                return;
            }
            if(!engineRules.getName().equals(ruleSet.getName())){
                //查询该规则集是否存在
                List<EngineRules> list = ruleSetService.selectByName(ruleSet.getName(),getUser().getUniqueCode());
                if (list.size() >= 1){
                    this.sendError(request, response, "规则集名已存在！");
                    return;
                }
            }
            if(checkId(ruleSet,engineRules)){
                ruleSet.setRuleJson(JSON.toJSONString(ruleSet.getRuleList(), SerializerFeature.WriteNonStringValueAsString));
                ruleSetService.updateEngineRules(ruleSet);
                this.sendOK(request, response);
            } else {
                this.sendErrAc(request, response,"规则集校验id失败,不能进行此操作");
            }
        } catch (Exception e) {
            this.sendError(request, response, "修改规则集失败！");
        }

    }

    /**
     * 规则集-回溯
     */
    @RequestMapping(value = "/remount")
    public void remount(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            EngineHistoryLog log = recordRulesLogService.selectByPrimaryKey(id);
            if(log!=null){
                EngineRules ruleSet = JSONObject.parseObject(log.getRulesMap(),EngineRules.class);
                ruleSet.setRuleJson(JSON.toJSONString(ruleSet.getRuleList()));
                ruleSetService.updateEngineRules(ruleSet);
                this.sendOK(request, response);
            }
        } catch (Exception e) {
            this.sendError(request, response, "规则集回溯失败！");
        }

    }

    /**
     * 为规则集构建id及关系
     * @param ruleSet
     * @param engineRules
     * @return
     */
    private boolean checkId(EngineRules ruleSet, EngineRules engineRules){
        Map<Long,EngineRule> ruleIds = new HashMap<>();
        Map<Long,Conditions> conditionIds = new HashMap<>();
        Map<Long,EngineField> fieldIds = new HashMap<>();

        if (engineRules.getRuleList()!=null&&!engineRules.getRuleList().isEmpty()) {
            for (EngineRule engineRule : engineRules.getRuleList()) {
                if (engineRule.getConditionsList()!=null&&!engineRule.getConditionsList().isEmpty()) {
                    for (Conditions conditions : engineRule.getConditionsList()) {
                        if (conditions.getFieldList()!=null&&!conditions.getFieldList().isEmpty()) {
                            for (EngineField engineField : conditions.getFieldList()) {
                                fieldIds.put(engineField.getId(),engineField);
                            }
                        }
                        conditionIds.put(conditions.getId(),conditions);
                    }
                }
                ruleIds.put(engineRule.getId(),engineRule);
            }
        }

        if (ruleSet.getRuleList()!=null&&!ruleSet.getRuleList().isEmpty()) {
            Map<Long,EngineRule> ruleMap = new HashMap<>();
            Map<Long,Conditions> conditionsMap = new HashMap<>();
            Set<Long> fieldSet = new HashSet<>();
            for (EngineRule engineRule : ruleSet.getRuleList()) {
                if(engineRule.getId()==null){
                    engineRule.setId(idGenerator.getId("RCS3:RULESET:RULE"));
                    engineRule.setAddTime(new Date());
                    engineRule.setAddUser(getUser().getName());
                    engineRule.setActive(1);
                } else if(ruleIds.keySet().contains(engineRule.getId())){
                    engineRule.setAddTime(ruleIds.get(engineRule.getId()).getAddTime());
                } else {
                    return false;
                }
                if (engineRule.getConditionsList()!=null&&!engineRule.getConditionsList().isEmpty()) {
                    for (Conditions conditions : engineRule.getConditionsList()) {
                        if(conditions.getId()==null){
                            conditions.setId(idGenerator.getId("RCS3:RULESET:CONDITIONS"));
                            conditions.setAddTime(new Date());
                            conditions.setAddUser(getUser().getName());
                        } else if(conditionIds.keySet().contains(conditions.getId())){
                            conditions.setAddTime(conditionIds.get(conditions.getId()).getAddTime());
                        } else {
                            return false;
                        }
                        if (conditions.getFieldList()!=null&&!conditions.getFieldList().isEmpty()) {
                            for (EngineField engineField : conditions.getFieldList()) {
                                if(engineField.getId()==null){
                                    engineField.setId(idGenerator.getId("RCS3:RULESET:FIELDS"));
                                    engineField.setAddTime(new Date());
                                    engineField.setAddUser(getUser().getName());
                                } else if(fieldIds.keySet().contains(engineField.getId())){
                                    engineField.setAddTime(fieldIds.get(engineField.getId()).getAddTime());
                                } else {
                                    return false;
                                }
                                if(conditionsMap.keySet().contains(conditions.getId())){
                                    Long id = idGenerator.getId("RCS3:RULESET:FIELDS");
                                    engineField.setAddTime(new Date());
                                    engineField.setAddUser(getUser().getName());
                                    String fieldRelationship = conditions.getFieldRelationship().replaceAll(engineField.getId().toString(), id.toString());
                                    conditions.setFieldRelationship(fieldRelationship);
                                    engineField.setId(id);
                                } else if(!fieldSet.add(engineField.getId())){
                                    engineField.setId(idGenerator.getId("RCS3:RULESET:FIELDS"));
                                    engineField.setAddTime(new Date());
                                    engineField.setAddUser(getUser().getName());
                                }
                                if(engineField.getFunctionSet()!=null){
                                    engineField.getFunctionSet().forEach(functionField -> functionField.setFunctionId(engineField.getId()));
                                }
                            }
                        }
                        if(ruleMap.keySet().contains(engineRule.getId())){
                            Long id = idGenerator.getId("RCS3:RULESET:CONDITIONS");
                            conditions.setAddTime(new Date());
                            conditions.setAddUser(getUser().getName());
                            String conditionRelationship = engineRule.getConditionRelationship().replaceAll(conditions.getId().toString(), id.toString());
                            engineRule.setConditionRelationship(conditionRelationship);
                            conditions.setId(id);
                        } else if(conditionsMap.keySet().contains(conditions.getId())){
                            conditions.setId(idGenerator.getId("RCS3:RULESET:CONDITIONS"));
                            conditions.setAddTime(new Date());
                            conditions.setAddUser(getUser().getName());
                        }
                        conditionsMap.put(conditions.getId(),conditions);
                    }
                }
                if(ruleMap.keySet().contains(engineRule.getId())){
                    engineRule.setAddTime(new Date());
                    engineRule.setAddUser(getUser().getName());
                    engineRule.setId(idGenerator.getId("RCS3:RULESET:RULE"));
                }
                ruleMap.put(engineRule.getId(),engineRule);
            }
        }
        return true;
    }

    /**
     * 将规则集导出到Excel中
     * @param rulesId
     * @param type      type = 0或者null 导出的是线上规则集  type = 1导出本地规则集
     * @param request
     * @param response
     */
    @SysLog("导出规则集到Excel")
    @RequestMapping(value = "/exportToExcel",method = RequestMethod.POST)
    @ResponseBody
    public Geo exportRuleSetToExcel(Long rulesId, Integer type, HttpServletRequest request, HttpServletResponse response){

        if (rulesId == null){
            return Geo.error("导出规则集失败,请输入规则集id");
        }
        try {
            EngineRules rules;
            // 本地版本
            if (type == null || type == 0) {
                rules = ruleSetService.findAllByIdForView(rulesId);
            } else {  // 线上版本
                rules = ruleSetService.findAllById(rulesId,false);
            }

            if (rules == null){
                return Geo.error("导出规则集失败,未查询到该规则集信息");
            }
            String fileName = "规则集_"+rules.getName() + DateUtils.format(new Date(),"yyyyMMddHHmmss") + ".xlsx";
            RulesExcelUtil rulesExcelUtil = ruleSetService.exportRulesToExcel(rules,fileName);
            rulesExcelUtil.write(request,response,fileName).dispose();
            System.out.println("导出规则集成功");
            return Geo.ok();
        } catch(Exception e){
            e.printStackTrace();
            return Geo.error("导出规则集失败，"+e.getMessage());
        }
    }

    @SysLog("Excel导入规则集")
    @ResponseBody
    @RequestMapping("/importRuleSet")
    public Geo importRuleSet(String name, MultipartFile file){

        Long rulesId = 0L;
        try{
            if (!BlankUtil.isBlank(file)) {
                rulesId = ruleSetService.importRuleSet(file, name);
            }
            return Geo.ok("成功导入规则集",rulesId.toString());
        }catch(Exception e){
            e.printStackTrace();
            if (e instanceof RcsException){
                return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), "导入规则集失败！"+e.getMessage());
            }
            return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), "导入规则集失败！");
        }

    }


}
