package com.geo.rcs.modules.rule.business.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.modules.rule.business.service.BusinessService;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 11:46 2019/3/12
 */
@RestController
@RequestMapping("/rule/business")
public class BusinessController extends BaseController {
    @Autowired
    private BusinessService businessService;
    private static Logger logger = LogUtil.logger(LogFileName.API_LOG);

    /**
     * 业务类型列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:business:list")//权限管理
    public void getSceneList(HttpServletRequest request, HttpServletResponse response, BusinessType business) {
        try {
            //添加unique_code （客户唯一标识）
            business.setUniqueCode(getUser().getUniqueCode());
            this.sendDataWhereDateToString(request, response, new PageInfo<>(businessService.findByPage(business)));
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取业务类型列表",business.toString(),getUser().getName(),e);
        }
    }


    /**
     * 添加业务类型
     */
    @SysLog("添加业务类型")
    @RequestMapping("/save")
    @RequiresPermissions("rule:business:save")
    public void save(HttpServletRequest request, HttpServletResponse response, BusinessType business) {
        ValidatorUtils.validateEntity(business, AddGroup.class);
        ResultType resultType = ValidateNull.check(business, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            List<BusinessType> lists = businessService.queryBusinessName(business.getTypeName(),getUser().getUniqueCode());
            if (lists.size()>=1) {
                this.sendError(request, response, "该业务类型已存在！");
                return;
            }
            //添加unique_code （客户唯一标识）
            business.setUniqueCode(getUser().getUniqueCode());
            business.setAddUser(getUser().getName());
            businessService.save(business);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "添加业务类型失败！");
        }
    }

    /**
     * 获取业务类型信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        this.sendDataWhereDateToString(request, response,businessService.getBusinessById(id));
    }


    /**
     * 修改业务类型
     */
    @SysLog("修改业务类型")
    @RequestMapping("/confirmUpdate")
    @RequiresPermissions("rule:business:update")
    public void updateScene(@Validated BusinessType business, HttpServletRequest request, HttpServletResponse response){

        ResultType resultType = ValidateNull.check(business, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }

        try {
            List<BusinessType> lists = businessService.queryBusinessName(business.getTypeName(),getUser().getUniqueCode());
            if (lists.size() >= 1) {
                for (int i = 0; i < lists.size(); i++){
                    if (lists.get(i).getId() != (long)business.getId() ){
                        this.sendError(request, response, "该业务类型已存在！");
                        return;
                    }
                }

            }
            businessService.updateBusiness(business);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新业务类型失败！");
        }
    }


}
