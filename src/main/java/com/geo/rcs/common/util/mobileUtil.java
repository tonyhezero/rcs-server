package com.geo.rcs.common.util;



import java.util.List;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2019/2/25  15:04
 **/
public class mobileUtil {

    //电信号码段：133,153,177,180,181,189
    private static final List<Integer> dx = java.util.Arrays.asList(133,153,177,180,181,189);
    //移动号码段：134,135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188
    private static final List<Integer> yd = java.util.Arrays.asList(134,135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188);
    //联通号码段：130,131,132,156,186,176,174,145
    private static final List<Integer> lt = java.util.Arrays.asList(130,131,132,156,186,176,174,145);

    /**
     * 根据手机号获取运营商
     * @param mobile
     * @return
     */
    public static String getIsp(String mobile){

        if (mobile == null || mobile.length() != 11){
            return "";
        }
        String mobileTag = mobile.substring(0,3);
        if (dx.contains(Integer.valueOf(mobileTag))){
            return "电信";
        }
        if (yd.contains(Integer.valueOf(mobileTag))){
            return "移动";
        }
        if (lt.contains(Integer.valueOf(mobileTag))){
            return "联通";
        }
        return "";
    }

}
