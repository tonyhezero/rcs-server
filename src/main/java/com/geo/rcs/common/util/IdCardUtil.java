package com.geo.rcs.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2019/2/20  20:40
 **/
public class IdCardUtil {

    //根据身份证号获取年龄
    public static int getAge(String idCard){

        if (idCard.length() != 18 && idCard.length() != 15){
            return 0;   //错误数据
        }
        String year = idCard.substring(6, 10);
        String month = idCard.substring(10, 12);
        String day = idCard.substring(12, 14);

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

        return age;
    }

    //根据身份证号获取性别
    public static String getGender(String idCard){

        if (idCard.length() != 18 && idCard.length() != 15){
            return "";   //错误数据
        }
        String gender = Integer.valueOf(idCard.substring(16, 17)) % 2 == 0 ? "女" : "男";
        return gender;
    }


}
