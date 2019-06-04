package com.geo.rcs.modules.approval.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.dao.ApprovalMapper;
import com.geo.rcs.modules.approval.dao.PatchDataMapper;
import com.geo.rcs.modules.approval.entity.ActionType;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.ObjectType;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesOnlineMapper;
import com.geo.rcs.modules.rule.ruleset.dao.RecordRulesLogMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRulesOnline;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.approval.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月29日 上午11:56
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApprovalMapper approvalMapper;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private PatchDataMapper patchDataMapper;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private RecordRulesLogMapper recordRulesLogMapper;
    @Autowired
    private EngineRulesOnlineMapper engineRulesOnlineMapper;

    @Override
    public Page<Approval> findByPage(Approval approval) {
        PageHelper.startPage(approval.getPageNo(), approval.getPageSize());
        return approvalMapper.findByPage(approval);
    }

    @Override
    public Approval getApprovalById(Long id) {
        return approvalMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加至审批列表
     *
     * @param approval 审核内容
     * @throws RcsException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToApproval(Approval approval) throws RcsException {
        approvalMapper.insertSelective(approval);
        if (approval.getObjId() != 5) {
            ruleSetService.updateEngineRulesVerify(approval);
        }
        //恢复
        if (approval.getRecordId() != null && approval.getObjId() == 2) {
            EngineHistoryLog engineHistoryLog = new EngineHistoryLog();
            engineHistoryLog.setAppStatus(1);
            engineHistoryLog.setId(approval.getRecordId());
            engineHistoryLog.setApprovalId(approval.getId());
            recordRulesLogMapper.updateAppStatus(engineHistoryLog);
        }
    }

    /**
     * 规则集审批
     *
     * @param approval 审批信息
     * @throws ServiceException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAppStatus(Approval approval){
        //规则集
        if (approval.getObjId() == 2) {
            if(approval.getActionType() != 7 && approval.getAppStatus() == 1){
                insertLog(approval);
            }
            approvalRuleSet(approval);
        }
        approvalMapper.updateAppStatus(approval);
    }

    /**
     * 审批规则集
     * @param approval 审批记录
     */
    private void approvalRuleSet(Approval approval){
        PatchData patchData = new PatchData();
        patchData.setVerify(2);
        patchData.setOnlyId(approval.getOnlyId());
        //审核通过
        if (approval.getAppStatus() == 2 && approval.getActionType() != 7 ) {
            patchData.setVerify(3);
            if(approval.getActionType() == 5 || approval.getActionType() == 6){
                EngineRulesOnline engineRulesOnline = new EngineRulesOnline();
                engineRulesOnline.setId(approval.getOnlyId());
                engineRulesOnline.setVerify(3);
                engineRulesOnline.setActive(null);
                ruleSetService.updateEngineRulesOnline(engineRulesOnline);
                return;
            }
        //添加
        } else if (approval.getActionType() == 1) {
            EngineRulesOnline engineRulesOnline = new EngineRulesOnline();
            engineRulesOnline.setId(approval.getOnlyId());
            engineRulesOnlineMapper.insertSelective(engineRulesOnline);
            publicRuleSet(approval);
        //修改
        } else if (approval.getActionType() == 2) {
            patchData = patchDataMapper.selectByOnlyId(approval);
            patchData.setVerify(2);
            patchData.setOnlyId(approval.getOnlyId());
            patchDataMapper.deleteByPrimaryKey(patchData.getId());
            publicRuleSet(approval);
        //删除
        } else if (approval.getActionType() == 3) {
            Long rulesId = engineRulesMapper.findRulesInUsed(approval.getOnlyId());
            if(rulesId != null){
                patchData.setVerify(3);
                approval.setAppStatus(4);
                approvalMapper.updateAppStatus(approval);
            } else {
                ruleSetService.deleteAbsolute(approval.getOnlyId());
                ruleSetService.deleteRedisCache(approval.getOnlyId(),true);
            }
        //手动提交审批
        } else if (approval.getActionType() == 4) {
            publicRuleSet(approval);
        //上线
        } else if(approval.getActionType() == 5){
            EngineRulesOnline engineRulesOnline = new EngineRulesOnline();
            engineRulesOnline.setVerify(2);
            engineRulesOnline.setActive(1);
            engineRulesOnline.setId(approval.getOnlyId());
            ruleSetService.updateEngineRulesOnline(engineRulesOnline);
            ruleSetService.deleteRedisCache(approval.getOnlyId(),true);
            return;
        //下线
        } else if(approval.getActionType() == 6){
            EngineRulesOnline engineRulesOnline = new EngineRulesOnline();
            engineRulesOnline.setVerify(2);
            engineRulesOnline.setActive(0);
            engineRulesOnline.setId(approval.getOnlyId());
            ruleSetService.updateEngineRulesOnline(engineRulesOnline);
            ruleSetService.deleteRedisCache(approval.getOnlyId(),true);
            return;
        }//版本恢复通过
        else if(approval.getActionType() == 7 && approval.getAppStatus() == 1){
            patchData.setActive(0);
            patchData.setStatus(0);
            ruleSetService.updateRulesSelect(patchData);
            EngineHistoryLog engineHistoryLog = new EngineHistoryLog();
            engineHistoryLog.setId(approval.getRecordId());
            engineHistoryLog.setAppStatus(2);
            recordRulesLogMapper.updateAppStatus(engineHistoryLog);
            return;
        }
        //版本恢复不通过
        else if(approval.getActionType() == 7 && approval.getAppStatus() == 2){
            patchData.setActive(0);
            ruleSetService.updateRulesSelect(patchData);
            EngineHistoryLog engineHistoryLog = new EngineHistoryLog();
            engineHistoryLog.setId(approval.getRecordId());
            engineHistoryLog.setAppStatus(3);
            recordRulesLogMapper.updateAppStatus(engineHistoryLog);
            return;
        }
        ruleSetService.updateRulesSelect(patchData);
        ruleSetService.deleteRedisCache(approval.getOnlyId(),false);
        return;
    }

    private void insertLog(Approval approval){
        EngineHistoryLog engineHistoryLog = new EngineHistoryLog();
        EngineHistoryLog log = recordRulesLogMapper.getNearPublishLog(approval.getOnlyId());
        engineHistoryLog.setActionType(approval.getActionType());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        //上下线版本号保持不变
        if(approval.getActionType() == 5 || approval.getActionType() == 6){
            engineHistoryLog.setVersion(log == null?sdf.format(new Date()):log.getVersion());
            engineHistoryLog.setLastVersion(log==null?null:log.getLastVersion());
        }
        else{
            engineHistoryLog.setVersion(sdf.format(new Date()));
            engineHistoryLog.setLastVersion(log==null?null:log.getVersion());
        }

        engineHistoryLog.setRecordTime(approval.getSubTime());
        engineHistoryLog.setUniqueCode(approval.getUniqueCode());
        EngineRules allById;
        if(approval.getActionType() == 5 || approval.getActionType() == 6){
            allById = ruleSetService.findAllById(approval.getOnlyId(),false);
        } else {
            allById = ruleSetService.findAllByIdForView(approval.getOnlyId());
        }
        allById.setRuleJson(null);
        String rulesTemplate = JSON.toJSONString(allById);
        engineHistoryLog.setRulesMap(rulesTemplate);
        engineHistoryLog.setRuleSetId(approval.getOnlyId());
        engineHistoryLog.setRulesName(allById.getName());
        engineHistoryLog.setApprovalId(approval.getId());
        engineHistoryLog.setSubmitter(approval.getSubmitter());
        try {
            recordRulesLogMapper.insertBySelective(engineHistoryLog);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发布到线上规则库,同时更新使用到此规则集的决策集进件参数
     * @param approval 审核内容
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publicRuleSet(Approval approval){
        EngineRules engineRules = ruleSetService.findAllByIdForView(approval.getOnlyId());
        EngineRulesOnline engineRulesOnline = new EngineRulesOnline(engineRules);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        engineRulesOnline.setVersion(sdf.format(new Date()));
        EngineRulesOnline old = engineRulesOnlineMapper.findRulesById(approval.getOnlyId());
        if(old == null){
            engineRulesOnlineMapper.insertSelective(engineRulesOnline);
        } else {
            engineRulesOnlineMapper.updateRuleSet(engineRulesOnline);
        }
        ruleSetService.deleteRedisCache(approval.getOnlyId(),true);
    }

    @Override
    public Approval selectById(Long id) {
        return approvalMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ObjectType> getObjList() {
        return approvalMapper.getObjList();
    }

    @Override
    public List<ActionType> getActionList() {
        return approvalMapper.getActionList();
    }

    @Override
    public Page<Approval> findUnByPage(Approval approval) {
        PageHelper.startPage(approval.getPageNo(), approval.getPageSize());
        return approvalMapper.findUnByPage(approval);
    }

    @Override
    public void deleteToApproval(Approval approval) {
        approvalMapper.insertSelective(approval);
        ruleSetService.updateEngineRulesVerify(approval);
    }

    @Override
    public void publicToApproval(Approval approval) {
        approvalMapper.insertSelective(approval);
    }

    @Override
    public void cancelApproval(Long id) {
        Approval approval = approvalMapper.selectByPrimaryKey(id);
        if(approval!=null && approval.getAppStatus() == 0){
            if(approval.getActionType() == 5 || approval.getActionType() ==6){
                Integer appStatus = approvalMapper.findNearStatus(approval.getOnlyId(),new Integer[]{5,6});
                EngineRulesOnline engineRulesOnline = new EngineRulesOnline();
                engineRulesOnline.setActive(null);
                engineRulesOnline.setId(approval.getOnlyId());
                if(appStatus!=null && appStatus == 1){
                    engineRulesOnline.setVerify(2);
                } else if(appStatus!=null && appStatus == 2) {
                    engineRulesOnline.setVerify(3);
                } else {
                    engineRulesOnline.setVerify(0);
                }
                ruleSetService.updateEngineRulesOnline(engineRulesOnline);
            } else {
                Integer appStatus = approvalMapper.findNearStatus(approval.getOnlyId(),new Integer[]{1,2,3,4});
                PatchData patchData = new PatchData();
                patchData.setOnlyId(approval.getOnlyId());
                if(appStatus!=null && appStatus == 1){
                    patchData.setVerify(2);
                } else if(appStatus!=null && appStatus == 2) {
                    patchData.setVerify(3);
                } else {
                    patchData.setVerify(0);
                }
                ruleSetService.updateRulesSelect(patchData);
            }
            approval.setAppStatus(5);
            approvalMapper.updateAppStatus(approval);
        } else {
            throw new RcsException("审批记录已被审批，不能撤回", 5000);
        }
    }

    @Override
    public void deleteDecisionToApproval(Approval approval) {
        approvalMapper.insertSelective(approval);
    }

    @Override
    public void deleteByPrimaryKey(Approval approval) {
        approvalMapper.deleteByPrimaryKey(approval.getId());
        //规则集
        if(approval.getObjId() == 2){
            EngineHistoryLog engineHistoryLog = new EngineHistoryLog();
            engineHistoryLog.setAppStatus(0);
            engineHistoryLog.setId(approval.getRecordId());
            recordRulesLogMapper.updateAppStatus(engineHistoryLog);
            PatchData patchData = new PatchData();
            patchData.setVerify(0);
            patchData.setOnlyId(approval.getOnlyId());
            ruleSetService.updateRulesSelect(patchData);
        }
    }
}
