package com.geo.rcs.modules.rule.ruleset.service;

import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRulesOnline;
import com.geo.rcs.modules.rule.util.RulesExcelUtil;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:49
 */
public interface RuleSetService {
    Page<EngineRules> findByPage(EngineRules ruleSet) throws ServiceException;

    Page<EngineRules> findOnlineByPage(EngineRules ruleSet) throws ServiceException;

    EngineRules findAllById(Long id, boolean unActive);

    EngineRules findAllByIdForTest(Long id);

    EngineRules selectById(Long id) throws RcsException;

    void delete(Long id) throws ServiceException;

    void updateEngineRules(EngineRules engineRules) throws ServiceException;

    void updateRuleSetParams(Long id);

    void deleteRedisCache(Long id, boolean isOnline);

    EngineRules addEngineRules(EngineRules engineRules) throws ServiceException;

    List<EngineRules> getRulesList(Long userId)  throws ServiceException;

    void updateEngineRulesVerify(Approval approval);

    void updateRulesSelect(PatchData patchData);

    EngineRules getRuleSetAndRuleInfo(Long id) throws ServiceException;

    void updateEngineRulesNo(EngineRules engineRules);

    Page<EngineRules> findAllByPage(EngineRules ruleSet) throws ServiceException;

    Page<EngineRules> findAll(EngineRules ruleSet) throws ServiceException;

    void deleteAbsolute(Long id);

    EngineRules findAllByIdForDelete(Long id);

    EngineHistoryLog findAllByIdFromHistory(Approval approval);

    EngineRules findAllByIdForView(Long id);

    EngineRules reviewEngineRuleSet(EngineRules engineRules) throws RcsException;

    List<EngineRules> getActiveRules(Long uniqueCode);

    List<EngineRules> selectByName(String name, Long uniqueCode) throws RcsException;

    List<Map<String,Object>> getApiEventData();

    EngineRulesOnline selectOnlineById(Long id);

    void updateEngineRulesOnline(EngineRulesOnline engineRules);

    void updateEngineRulesStatus(Long id);

    Long findRulesInUsed(Long id);



//    /**
//     * 检测json表是否存在此规则集
//     * @param allByIdForView 规则实体
//     * @return Boolean
//     */
//    void queryRuleSetHandle(EngineRules allByIdForView);

    /**
     * 导出规则集到Excel
     * @param rules
     * @return
     */
    RulesExcelUtil exportRulesToExcel(EngineRules rules, String fileName) throws IOException;

    /**
     * 从文件导入规则集
     * @param file
     */
    Long importRuleSet(MultipartFile file, String rulesName) throws Exception;


    void checkRules(EngineRules rules, SysUser user);
}
