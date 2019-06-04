package com.geo.rcs.modules.approval.controller;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.ActionType;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.approval.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月29日 上午11:56
 */
@RestController
@RequestMapping("/appr")
public class ApprovalController extends BaseController {

    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private RuleSetService ruleSetService;

    private static Geo geo;

    private static Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    /**
     * 查询待审批/已审批列表 需要传入appStatus审批状态码（模糊，分页）
     *
     * @param request
     * @param response
     * @param approval
     */
    @RequestMapping("/list")
    @RequiresPermissions("api:appr:list")//权限管理
    public void getApprovalList(HttpServletRequest request, HttpServletResponse response, Approval approval) {
        try {
            //添加unique_code （客户唯一标识）
            approval.setUniqueCode(getUser().getUniqueCode());

            this.sendData(request, response, new PageInfo<>(approvalService.findByPage(approval)));

            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("查询待审批/已审批列表 需要传入appStatus审批状态码（模糊，分页）", approval.toString(), getUser().getName(), e);
        }
    }

    @RequestMapping("/approvaled")
    @RequiresPermissions("api:appr:list")//权限管理
    public void getUnApprovalList(HttpServletRequest request, HttpServletResponse response, Approval approval) {
        try {
            approval.setUniqueCode(getUser().getUniqueCode());

            this.sendData(request, response, new PageInfo<>(approvalService.findUnByPage(approval)));

            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("/approvaled", approval.toString(), getUser().getName(), e);

        }
    }

    /**
     * 提交审批
     */
    @RequestMapping("/addToApproval")
    public void addToApproval(HttpServletRequest request, HttpServletResponse response, @RequestBody EngineRules engineRules) {
        try {
            SysUser user = getUser();
            Approval approval = new Approval();
            approval.setUniqueCode(user.getUniqueCode());
            EngineRules engineRules1;
            try {
                engineRules1 = ruleSetService.findAllByIdForTest(engineRules.getId());
                ruleSetService.checkRules(engineRules1,user);
            } catch (RcsException e) {
                this.sendError(request, response, e.getMsg());
                return;
            }
            if (engineRules1.getVerify() == 1) {
                this.sendError(request, response, "规则集审核中，请等待审核结果后操作");
                return;
            } else {
                EngineScene byId = sceneService.findById(engineRules1.getSenceId());
                if (byId != null) {
                    approval.setScene(byId.getName());
                }
                approval.setActionType(engineRules.getActionType());
                if(engineRules.getThresholdMin()!=null&&!"".equals(engineRules.getThresholdMin())){
                    approval.setThresholdMin(Integer.valueOf(engineRules.getThresholdMin()));
                }
                if(engineRules.getThresholdMax()!=null&&!"".equals(engineRules.getThresholdMax())){
                    approval.setThresholdMax(Integer.valueOf(engineRules.getThresholdMax()));
                }
                approval.setBusinessId(engineRules1.getBusinessId());
                approval.setObjId(2);
                approval.setThreshold(engineRules1.getThreshold());
                approval.setOnlyId(engineRules1.getId());
                approval.setSubmitter(user.getName());
                approval.setSubTime(new Date());
                approval.setUniqueCode(user.getUniqueCode());
                approval.setDescription(engineRules1.getName());
                approval.setRecordId(engineRules.getHistoryLogId());
                approvalService.addToApproval(approval);
                this.sendOK(request, response);
                logger.info(LogUtil.operation("提交审批", engineRules.getName(), getUser().getName(), TimeUtil.dqsj(), "成功"));
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "提交审批失败！");
            LogUtil.error("提交审批", engineRules.toString(), getUser().getName(), e);

        }
    }

    /**
     * 获取审批信息
     */
    @RequestMapping("/getApprovalMsg")
    @RequiresPermissions("api:appr:detail")
    public Geo getApprovalMsg(Long id) {
        try {
            Approval approval = approvalService.selectById(id);
            if (approval.getObjId() == 2) {
                //删除待审批
                if (approval.getActionType() == 3 && approval.getAppStatus() == 0) {
                    EngineRules allByIdForDelete = ruleSetService.findAllByIdForDelete(approval.getOnlyId());
                    if (allByIdForDelete == null) {
                        geo = Geo.error("信息错误！");
                    }
                    approval.setRuleSetMap(allByIdForDelete);
                    geo = Geo.ok().put("datas", approval);

                }
                //删除已审批
                else if (approval.getActionType() == 3) {
                    EngineHistoryLog engineHistoryLog = ruleSetService.findAllByIdFromHistory(approval);
                    if (engineHistoryLog == null) {
                        geo = Geo.error("信息错误！");
                    }
                    JSONObject jsStr = JSONObject.parseObject(engineHistoryLog.getRulesMap());
                    EngineRules engineRules = (EngineRules) JSONObject.toJavaObject(jsStr, EngineRules.class);
                    approval.setRuleSetMap(engineRules);
                    geo = Geo.ok().put("datas", approval);

                }
                //修改已审批
                else if (approval.getActionType() == 4) {
                    EngineHistoryLog engineHistoryLog = ruleSetService.findAllByIdFromHistory(approval);

                    // 新建规则集部分，无历史记录情况查询最新记录
                    if (engineHistoryLog == null) {
                        EngineRules allByIdForTest = ruleSetService.findAllByIdForView(approval.getOnlyId());

                        if (allByIdForTest == null) {
                            geo = Geo.error("信息错误！");
                        }
                        approval.setRuleSetMap(allByIdForTest);
                        geo = Geo.ok().put("datas", approval);

                    } else {
                        JSONObject jsStr = JSONObject.parseObject(engineHistoryLog.getRulesMap());
                        EngineRules engineRules = (EngineRules) JSONObject.toJavaObject(jsStr, EngineRules.class);
                        approval.setRuleSetMap(engineRules);
                        geo = Geo.ok().put("datas", approval);
                    }

                } else if(approval.getActionType() == 5||approval.getActionType() == 6){
                    EngineRules allByIdForTest = ruleSetService.findAllById(approval.getOnlyId(),false);

                    if (allByIdForTest == null) {
                        geo = Geo.error("信息错误！");
                    }
                    approval.setRuleSetMap(allByIdForTest);
                    geo = Geo.ok().put("datas", approval);
                } else {
                    EngineRules allByIdForTest = ruleSetService.findAllByIdForView(approval.getOnlyId());

                    if (allByIdForTest == null) {
                        geo = Geo.error("信息错误！");
                    }
                    approval.setRuleSetMap(allByIdForTest);
                    geo = Geo.ok().put("datas", approval);
                }
            }
        } catch (RcsException e) {
            geo = Geo.error(e.getMsg());
            LogUtil.error("获取审批信息", id.toString(), getUser().getName(), e);

        }
        return geo;
    }

    /**
     * 审批
     */
    @RequestMapping(value = "/accraditation", method = RequestMethod.POST)
    @RequiresPermissions("api:appr:accraditation")
    public void accreditation(HttpServletRequest request, HttpServletResponse response, Approval approval) {

        Approval app = approvalService.selectById(approval.getId());
        if (app != null && app.getAppStatus()!=4) {
            ResultType resultType = ValidateNull.check(approval, NotNull.RequestType.UPDATE);
            if (ResultType.FAILD.equals(resultType)) {
                this.sendError(request, response, resultType.getMsg());
                return;
            }
            try {
                app.setAppPerson(getUser().getName());
                app.setAppTime(new Date());
                app.setAppStatus(approval.getAppStatus());
                app.setRemarks(approval.getRemarks());
                // type 0:规则集 1:决策集
                if (approval.getType() == 0) {
                    approvalService.updateAppStatus(app);
                    this.sendOK(request, response);
                }
                if (approval.getAppStatus() == 2) {
                    logger.info(LogUtil.operation("审批", app.getDescription(), getUser().getName(), TimeUtil.dqsj(), "不通过"));
                } else if(approval.getAppStatus() == 4){
                    logger.info(LogUtil.operation("审批", app.getDescription(), getUser().getName(), TimeUtil.dqsj(), "失效"));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 40001);
                    jsonObject.put("msg", "决策集正在使用中，无法删除");
                    sendJson(request,response, jsonObject.toJSONString());
                } else {
                    logger.info(LogUtil.operation("审批", app.getDescription(), getUser().getName(), TimeUtil.dqsj(), "通过"));
                }
            } catch (RcsException e){
                this.sendError(request, response,e.getMsg());
            }
            catch (Exception e) {
                this.sendError(request, response, "审批失败！");
                LogUtil.error("审批", approval.toString(), getUser().getName(), e);
            }
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 40002);
            jsonObject.put("msg", "审批已被撤回！");
            sendJson(request,response, jsonObject.toJSONString());
        }
    }

    /**
     * 撤销审批
     */
    @RequestMapping(value = "/cancelApproval", method = RequestMethod.POST)
    @RequiresPermissions("api:appr:accraditation")
    public void cancelApproval(HttpServletRequest request, HttpServletResponse response,Long id) {
        try {
            approvalService.cancelApproval(id);
        } catch (Exception e) {
            this.sendError(request, response, "撤销失败！");
            LogUtil.error("【撤销审批】 id：", id.toString(), getUser().getName(), e);
        }
    }

    /**
     * 已审批查看
     */
    @RequestMapping("/viewApproval")
    @RequiresPermissions("api:appr:view")
    public void viewApproved(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, approvalService.getApprovalById(id));
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("已审批查看", id.toString(), getUser().getName(), e);

        }
    }

    /**
     * 获取下拉框列表
     */
    @RequestMapping("/getTypeList")
    public void getTypeList(HttpServletRequest request, HttpServletResponse response) {
        try {
            ArrayList arrayList = new ArrayList<List>();
            List<BusinessType> busType = sceneService.getBusType(getUser().getUniqueCode());
            List<ActionType> actionList = approvalService.getActionList();
            arrayList.add(busType);
            arrayList.add(actionList);
            this.sendData(request, response, arrayList);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取下拉框列表", "无参数", getUser().getName(), e);

        }
    }

    /**
     * 撤销审批
     */
    @RequestMapping("/revocation")
    public void revocation(HttpServletRequest request, HttpServletResponse response,@RequestBody Approval approval) {
        try {
            approvalService.deleteByPrimaryKey(approval);
            this.sendOK(request,response);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取下拉框列表", "无参数", getUser().getName(), e);

        }
    }
}
