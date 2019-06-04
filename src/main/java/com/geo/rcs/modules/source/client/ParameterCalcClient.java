package com.geo.rcs.modules.source.client;

import com.alibaba.druid.support.json.JSONUtils;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.JSONUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/12/5  19:03
 **/

public class ParameterCalcClient {


    /**入参接口*/
    private static final String INPUT_IDNUMBER = "INPUT_IDNUMBER";

    private static Map<String,String> BIRTH_PROVICE = new HashMap<>();

    private static final Log log = LogFactory.getLog(ParameterCalcClient.class);



    static{
        BIRTH_PROVICE.put("11","北京市");
        BIRTH_PROVICE.put("12","天津市");
        BIRTH_PROVICE.put("13","河北省");
        BIRTH_PROVICE.put("14","山西省");
        BIRTH_PROVICE.put("15","内蒙古自治区");
        BIRTH_PROVICE.put("21","辽宁省");
        BIRTH_PROVICE.put("22","吉林省");
        BIRTH_PROVICE.put("23","黑龙江省");
        BIRTH_PROVICE.put("31","上海市");
        BIRTH_PROVICE.put("32","江苏省");
        BIRTH_PROVICE.put("33","浙江省");
        BIRTH_PROVICE.put("34","安徽省");
        BIRTH_PROVICE.put("35","福建省");
        BIRTH_PROVICE.put("36","江西省");
        BIRTH_PROVICE.put("37","山东省");
        BIRTH_PROVICE.put("41","河南省");
        BIRTH_PROVICE.put("42","湖北省");
        BIRTH_PROVICE.put("43","湖南省");
        BIRTH_PROVICE.put("44","广东省");
        BIRTH_PROVICE.put("45","广西壮族自治区");
        BIRTH_PROVICE.put("46","海南省");
        BIRTH_PROVICE.put("50","重庆市");
        BIRTH_PROVICE.put("51","四川省");
        BIRTH_PROVICE.put("52","贵州省");
        BIRTH_PROVICE.put("53","云南省");
        BIRTH_PROVICE.put("54","西藏自治区");
        BIRTH_PROVICE.put("61","陕西省");
        BIRTH_PROVICE.put("62","甘肃省");
        BIRTH_PROVICE.put("63","青海省");
        BIRTH_PROVICE.put("64","宁夏回族自治区");
        BIRTH_PROVICE.put("65","新疆维吾尔自治区");
        BIRTH_PROVICE.put("71","台湾省");
        BIRTH_PROVICE.put("81","香港特别行政区");
        BIRTH_PROVICE.put("82","澳门特别行政区");

    }

    /**
     * 解析
     * @param inner
     * @param parameters
     * @return
     */
    public String getData(String inner,String parameters){

        Map<String,Object> result = new HashedMap();
        Map<String,Object> map = JSONUtil.jsonToMap(parameters);
        result.putAll(getDataForINPUT_IDNUMBER((String)map.get("idNumber")));

        return JSONUtils.toJSONString(result);
    }

    /**
     * 原参数返回
     * @param inner
     * @param parameters
     * @param fieldName
     * @return
     */
    public String getData(String inner,String parameters,String fieldName){

        Map<String,Object> result = new HashedMap();
        Map<String,Object> map = JSONUtil.jsonToMap(parameters);

        if (fieldName.equalsIgnoreCase("inputAccountName")){
            result.put(fieldName,map.get("realName"));
        }else if(fieldName.equalsIgnoreCase("inputAccountMobile")){
            result.put(fieldName,map.get("cid"));
        }else{
            result.put(fieldName,map.get(fieldName));
        }


        return JSONUtils.toJSONString(result);
    }

    /**
     * 设备指纹接口
     * @return
     */
    public String getSBZWInter(){

        Map<String,Object> map = new HashedMap();
        Random random = new Random();
        List<String> fieldNameList = Arrays.asList("sdk_1h",
                "sdk_5h",
                "sdk_1d",
                "sdk_7d",
                "sdk_1m",
                "sdk_time",
                "sdk_1h-login",
                "sdk_5h_login",
                "sdk_1d_login",
                "sdk_7d_login",
                "sdk_1m_login",
                "sdk_1h_register",
                "sdk_5h_register",
                "sdk_1d_register",
                "sdk_7d_register",
                "sdk_4m_register",
                "sdk_1h_ip",
                "sdk_5h_ip",
                "sdk_1d_ip",
                "sdk_7d_ip",
                "sdk_4m_ip",
                "sdk_7d_applicant",
                "sdk_30d_applicant",
                "sdk_1h_city",
                "sdk_mobile",
                "sdk_hit",
                "sdk_is_entrance",
                "sdk_is_vps",
                "sdk_is_agency",
                "sdk_is_vpn",
                "sdk_is_crawler",
                "sdk_is_search_crawler",
                "sdk_is_hitlibrary",
                "sdk_is_dns",
                "sdk_is_mail",
                "sdk_is_spite",
                "sdk_ip_score");
        for (String fieldName : fieldNameList) {
            if (fieldName.indexOf("is") != -1) {
                map.put(fieldName, random.nextInt(2));
            } else if (fieldName.indexOf("1h") != -1) {
                map.put(fieldName, random.nextInt(15));
            } else if (fieldName.indexOf("5h") != -1) {
                map.put(fieldName, random.nextInt(30));
            } else if (fieldName.indexOf("1d") != -1) {
                map.put(fieldName, random.nextInt(45));
            } else if (fieldName.indexOf("7d") != -1) {
                map.put(fieldName, random.nextInt(60));
            } else if (fieldName.indexOf("7d") != -1) {
                map.put(fieldName, random.nextInt(75));
            } else if (fieldName.indexOf("1m") != -1) {
                map.put(fieldName, random.nextInt(100));
            } else {
                map.put(fieldName, random.nextInt(100));
            }
        }

        return JSONUtils.toJSONString(map);
    }


    /**
     * 获取入参接口INPUT_IDNUMBER接口数据：身份证
     * @param idNumber
     * @return
     */
    public Map<String, Object> getDataForINPUT_IDNUMBER(String idNumber) throws RcsException {

        Map<String,Object> result = new HashMap<>();

        if (idNumber.length() != 18){
            throw new RcsException("身份证号码错误");
        }

        String year = idNumber.substring(6, 10);
        String month = idNumber.substring(10, 12);
        String day = idNumber.substring(12, 14);
        String birthday =  year + month + day;
        result.put("inputIdNumberBirthday",birthday);

        int age = 0;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar birth = Calendar.getInstance();
        birth.set(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(day));
        if (birth.after(now)){
            age = 0;
        }else{
            age = now.get(Calendar.YEAR)-birth.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)){
                age--;
            }
        }
        result.put("inputIdNumberAge",age);

        String gender = Integer.valueOf(idNumber.substring(16, 17)) % 2 == 0 ? "女" : "男";
        result.put("inputIdNumberGender",gender);

        String birthProvice = BIRTH_PROVICE.get(idNumber.substring(0,2));
        result.put("inputIdNumberBirthProvice",birthProvice);

        result.put("inputIdNumber",idNumber);

        return result;
    }


}
