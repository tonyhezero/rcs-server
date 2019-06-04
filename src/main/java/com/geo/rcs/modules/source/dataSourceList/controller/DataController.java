package com.geo.rcs.modules.source.dataSourceList.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.source.dataSourceList.entity.InterCorrelation;
import com.geo.rcs.modules.source.dataSourceList.service.SeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by geo on 2019/5/8.
 */
@RestController
@RequestMapping("/dataSource")
public class DataController extends BaseController {
    private static final String  GEO ="geo";
    private static final String  DATAPLATFORM ="dataPlatform";
    private static final String  GEOZXW ="geozxw";
    private static final String  PARAMETERCALC ="parameterCalc";
    private static final String  TONGDUN ="tongDun";
    @Autowired
    private EngineInterService engineInterService;
    @Autowired
    private SeparationService separationService;
    @RequestMapping(value="/showInterList",method = RequestMethod.POST)
    public Geo showInterList(){
        List<EngineInter> allInters=null;
        Map<String,List<Map>> interMap=new HashMap();
        List geoList=new ArrayList<>();
        List dataPlatformList=new ArrayList<>();
        List geozxwList=new ArrayList<>();
        List parameterCalc=new ArrayList<>();
        List tongDunList=new ArrayList<>();
        List wuxiList=new ArrayList<>();
        try {
            allInters = engineInterService.getAllInter();
            for (EngineInter engineInter:allInters) {
                switch (engineInter.getRequestType()){
                    case GEO:
                        geoList.add(engineInter);
                        interMap.put(GEO,geoList);
                        break;
                    case DATAPLATFORM:
                        dataPlatformList.add(engineInter);
                        interMap.put(DATAPLATFORM,dataPlatformList);
                        break;
                    case GEOZXW:
                        geozxwList.add(engineInter);
                        interMap.put(GEOZXW,geozxwList);
                        break;
                    case PARAMETERCALC:
                        parameterCalc.add(engineInter);
                        interMap.put(PARAMETERCALC,parameterCalc);
                        break;
                    case TONGDUN:
                        tongDunList.add(engineInter);
                        interMap.put(TONGDUN, tongDunList);
                        break;
                    case "wuxi":
                        wuxiList.add(engineInter);
                        interMap.put(TONGDUN, wuxiList);

                }
            }
        }catch (Exception e){
            return  Geo.ok().put("msg","查询失败").put("allInters",interMap);
        }
        return Geo.ok().put("msg","返回接口结果").put("allInters",interMap);

    }
    @RequestMapping("addInter")
    @Transactional



    public Geo updateDataSourceBy(@RequestBody String data) {
        Map<String,Object> dataMap = JSON.parseObject(data, Map.class);
        InterCorrelation interCorrelation = new InterCorrelation();
        List<String> interNames = (List)dataMap.get("permissionList");
        interCorrelation.setUniqueCode(Long.valueOf(dataMap.get("userId").toString()));
        try {
            separationService.deleteByEntity(interCorrelation);
            if(null!=interNames&&interNames.size()>0){
                for (String str : interNames) {
                    EngineInter engineInter = new EngineInter();
                    engineInter.setName(str);
                    EngineInter inter = engineInterService.queryByName(engineInter);
                    interCorrelation.setInterId(inter.getId());
                    separationService.insertSelective(interCorrelation);
                }
                return Geo.ok().put("msg", "添加接口成功");
            }else{
                return Geo.ok().put("msg", "请勾选接口");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Geo.error().put("msg", "添加接口失败");
        }
    }
    @RequestMapping("/showSelectdInter")
    public Geo showSelectedInter (String userId){
        InterCorrelation interCorrelation = new InterCorrelation();
        interCorrelation.setUniqueCode(Long.valueOf(userId));
        List<InterCorrelation> interCorrelations = separationService.selectByEntity(interCorrelation);
        List<String> geoList=new ArrayList<>();
        List<String> dataPlatformList=new ArrayList<>();
        List<String> geozxwList=new ArrayList<>();
        List<String> parameterCalc=new ArrayList<>();
        List<String> tongDunList=new ArrayList<>();
        Map<String,List<String>> interMap=new HashMap();
        for (InterCorrelation correlation:interCorrelations) {
            EngineInter engineInter = engineInterService.getInterById(correlation.getInterId());
            switch (engineInter.getRequestType()) {
                case GEO:
                    geoList.add(engineInter.getName());

                    interMap.put(GEO, geoList);
                    break;
                case DATAPLATFORM:
                    dataPlatformList.add(engineInter.getName());
                    interMap.put(DATAPLATFORM, dataPlatformList);
                    break;
                case GEOZXW:
                    geozxwList.add(engineInter.getName());
                    interMap.put(GEOZXW, geozxwList);
                    break;
                case PARAMETERCALC:
                    parameterCalc.add(engineInter.getName());
                    interMap.put(PARAMETERCALC, parameterCalc);
                    break;
                case TONGDUN:
                    tongDunList.add(engineInter.getName());
                    interMap.put(TONGDUN, tongDunList);
                    break;
            }
        }
        return  Geo.ok().put("msg","回显接口数据").put("allInters",interMap);
    }
}
