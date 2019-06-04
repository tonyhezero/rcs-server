package com.geo.rcs.modules.source.verified.client.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.constant.ConfigConstant;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.source.verified.client.entity.VerfiedLog;
import com.geo.rcs.modules.source.verified.client.service.DataSourceVerfiedService;
import com.geo.rcs.modules.source.verified.client.service.VerfiedLogService;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by geo on 2019/3/25.
 */
@RestController
@RequestMapping(value = "/verified")
public class VerfiedController  extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(VerfiedController.class);
    @Autowired
    private EngineInterService engineInterService;
    @Autowired
    private VerfiedLogService verfiedLogService;
    @RequestMapping(value = "/getDataSource", method = RequestMethod.POST)
    public Geo getVerifiedData(@RequestBody String params){

        VerfiedLog verfiedLog = new VerfiedLog();
        Map<String,Object> map = JSON.parseObject(params, Map.class);
        String interName= map.get("name").toString();
        String parameters = map.get("parameters").toString();
        Map<String,String> param = JSON.parseObject(parameters, Map.class);
        Long id = ((SysUser) SecurityUtils.getSubject().getPrincipal()).getUniqueCode();
        verfiedLog.setUser_id(id);
        verfiedLog.setType("1");
        verfiedLog.setCid(param.get("cid"));
        verfiedLog.setIdnumber(param.get("idNumber"));
        verfiedLog.setRealname(param.get("realName"));
        verfiedLog.setName(interName);

        return Geo.ok().put("msg","返回结果").put("data",null);

    }

    /**
     * 获取接口名称目前只有B7/A3/A4/B8/B9接口
     * @return
     */
    @RequestMapping(value = "/getInterNames", method = RequestMethod.GET)
    public Geo getInterName(){
        Map<String,Object> interNameMaps=new HashMap<>();
        interNameMaps.put("B7","手机号身份证姓名验证");
        interNameMaps.put("A3","手机号在网时长");
        interNameMaps.put("A4","手机在网状态");
        interNameMaps.put("B8","工作地验证new");
        interNameMaps.put("B9","休息地验证new");
        return Geo.ok().put("msg","返回接口名称").put("interNameMaps",interNameMaps);

    }

    /**
     * 获取接口参数
     * @param data
     * @return
     */
    @RequestMapping(value = "/getIntertParameter", method = RequestMethod.POST)
    public Geo getParameterByInterName(@RequestBody String data){
        Map<String,String> map = JSON.parseObject(data, Map.class);
        EngineInter engineInter=new EngineInter();
        if(!StringUtils.isEmpty(map.get("name"))){
            engineInter.setName(map.get("name"));
            engineInter= engineInterService.queryByName(engineInter);
            if(null!=engineInter){
                return  Geo.ok().put("msg","返回参数类型").put("parameter",engineInter.getParameters());
            }else{
                return  Geo.ok().put("msg","返回参数类型").put("parameter","未查到");
            }
        }else{
            throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());
        }
    }
    @RequestMapping(value = "/getVerfiedLogs", method = RequestMethod.POST)
    public Geo getVerfiedLogs(@RequestBody String data){
        VerfiedLog verfiedLog = JSON.parseObject(data, VerfiedLog.class);
        verfiedLog.setType("1");
        verfiedLog.setUser_id(((SysUser) SecurityUtils.getSubject().getPrincipal()).getUniqueCode());
        if(verfiedLog.getStartTime()!=null&&verfiedLog.getStartTime().length()>0&&verfiedLog.getEndTime()!=null&&verfiedLog.getEndTime().length()>0){
            if (!DateUtils.compareTo(verfiedLog.getStartTime(),verfiedLog.getEndTime())){
                return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能小于开始时间");
            }
        }
        if(!StringUtils.isEmpty(verfiedLog.getPageNo())&&!StringUtils.isEmpty(verfiedLog.getPageSize())){
            PageHelper.startPage(Integer.parseInt(verfiedLog.getPageNo()),Integer.parseInt(verfiedLog.getPageSize()));
        }else{
            PageHelper.startPage(1,10)  ;
        }
        List<VerfiedLog> verfiedLogList = verfiedLogService.selectAll(verfiedLog);
        PageInfo< VerfiedLog> pageInfo = new PageInfo<>(verfiedLogList);
        if(null!= verfiedLogList&& verfiedLogList.size()>0){
            return  Geo.ok().put("msg","返回参数结果").put("verfiedLogList", pageInfo);
        }else{
            return  Geo.ok().put("msg","返回参数结果").put("verfiedLogList",pageInfo);
        }
    }
    /**
     * 进件管理 进件列表导出 excel
     *
     * @param request
     * @param response
     * @param  verfiedLog
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    public Geo exportExcel(HttpServletRequest request, HttpServletResponse response, String exportType, Long[] ids, VerfiedLog verfiedLog) {
        try {
            Page<Map<String, Object>> byPageAll = null;
            if ("array".equalsIgnoreCase(exportType)) {
                byPageAll =verfiedLogService.findByPageIds(ids);
            } else {
                if ("all".equalsIgnoreCase(exportType)) {
                    verfiedLog = new VerfiedLog();
                    byPageAll = verfiedLogService.findByPageAll(verfiedLog);
                } else if ("condition".equalsIgnoreCase(exportType)) {
                    byPageAll = verfiedLogService.findByPageAll(verfiedLog);

                } else {
                    return Geo.error(StatusCode.PARAMS_ERROR.getMessage());
                }
            }
            if (byPageAll != null && byPageAll.size() > 0) {
                if (byPageAll.size() > ConfigConstant.EXCEL_EXPORT_ROWS_MAX) {
                    return Geo.error("数据量太大,请重新筛选!");
                }
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.addAll(byPageAll);
                // 状态码,规则结果,规则评分,返回结果
                String fileName = "一键效验统计_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
                String[] headers = new String[]{"序号", "查询时间", "姓名", "身份证号",  "手机号", "请求状态", "验证结果"};
                String[] props = new String[]{"rowNum", "queryTime", "realName", "idNumber", "cid", "requestStatus", "result"};
                ExportExcel excel = new ExportExcel("一键效验统计", headers, 1).setDataList2(mapList, props);
                try {
                    excel.write(request,response, fileName).dispose();
                    return Geo.ok(StatusCode.SUCCESS.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
                }
            } else {
                return Geo.ok("暂无可导出的数据!");
            }
        } catch (Exception e) {
            LogUtil.error("一键效验件列表,导出Exceol失败!", verfiedLogService.toString(), getUser().getName(), e);
            return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());

        }
    }

}
