package com.geo.rcs.modules.rule.ruleset.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RecordRulesLogService;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author wp
 * @date Created in 18:03 2019/2/20
 */
@RestController
@RequestMapping("/rule/ruleSet/record")
public class RecordRulesLogController extends BaseController {

    @Autowired
    RecordRulesLogService recordRulesLogService;
    @Autowired
    private RuleSetService ruleSetService;


    @RequestMapping("/query")
    @RequiresPermissions("rule:record:list")
    public Geo query(Long ruleSetId, Integer actionType, String submitter, String startTime, String endTime, int pageNo, int pageSize) {
        Geo geo = Geo.ok();
        try {
            PageInfo<EngineHistoryLog> pageInfo = new PageInfo<>(recordRulesLogService.findOnlineByPage(getUser().getUniqueCode(),ruleSetId,actionType,submitter,startTime,endTime,pageNo,pageSize));
            geo.put("data", pageInfo);
        }catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
        }
        return geo;
    }

    @RequestMapping("/options")
    @RequiresPermissions("rule:ruleset:list")
    public Geo list() {
        Geo geo = Geo.ok();
        try {
            List<EngineHistoryLog> logs = recordRulesLogService.findAllOption();
            Map<String,Object> result = new HashMap<>(2);
            if(logs!=null&&!logs.isEmpty()){
                Map<String,Long> ruleSetMap = new HashMap<>();
                Set<String> submitterSet = new HashSet<>();
                for(EngineHistoryLog log:logs){
                    ruleSetMap.put(log.getRulesName(),log.getRuleSetId());
                    submitterSet.add(log.getSubmitter());
                }
                result.put("ruleSet",ruleSetMap);
                result.put("submitter",submitterSet);
            }
            geo.put("data", result);
        }catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
        }
        return geo;
    }

    /**
     * 恢复
     */
    @RequestMapping("/restore")
    @RequiresPermissions("rule:record:restore")
    public Geo restore(Long id){
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules!=null){
                return Geo.error(5000,"规则已恢复，请勿重复操作");
            } else {
                ruleSetService.updateEngineRulesStatus(id);
                return Geo.ok();
            }
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 查看版本记录
     */
    @RequestMapping("/viewHistoryLog")
    @RequiresPermissions("rule:record:detail")
    public Geo viewHistoryLog(Long id){
        try {
            EngineHistoryLog engineHistoryLog = recordRulesLogService.selectByPrimaryKey(id);
            if(engineHistoryLog==null){
                return Geo.error("暂无此记录信息");
            }
            return Geo.ok().put("logInfo",engineHistoryLog);
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 获取有版本记录的规则集名称
     */
    @RequestMapping("/getRecordedRules")
    public Geo getRecordedRules(Map<String,Object> map){
        try {
            map.put("userId",getUser().getUniqueCode());
            List<EngineRules> engineRulesList = recordRulesLogService.getRecordedRules(map);
            return Geo.ok().put("engineRulesList",engineRulesList);
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(), StatusCode.SERVER_ERROR.getMessage());
        }
    }

}
