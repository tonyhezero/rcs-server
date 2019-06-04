package com.geo.rcs.modules.engine.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EngineCalculator {

    public static int Not(int res) {
        if(res==1){
            res = 0;
        }else{
            res = 1;
        }
        return res;
    }

    public static int compareDate( String startDate, String endDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return start.compareTo(end);
    }

    public static int compareDatetime( String startDate, String endDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return start.compareTo(end);
    }

    public static int diffToday( String recentDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date();
        Date end = sdf.parse(recentDate);

        long diff = today.getTime() - end.getTime();
        return (int)(diff/(1000*60*60*24));
    }

    public static Integer diffTodayTime( String recentDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date today = new Date();
        Date end = sdf.parse(recentDate);

        long diff = today.getTime() - end.getTime();
        return (int)(diff/(1000*60*60*24));
    }

    public static Long dayDiff(String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(date1==null||date2==null){
            return null;
        }
        long diff=0L;
        try {
            long d1 = formatter.parse(date1).getTime();
            long d2 = formatter.parse(date2).getTime();
            diff=(d1-d2)/(1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static Integer getMonthSpace(String date1, String date2) {
        if(date1==null||date2==null){
            return null;
        }
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getMonthDiff(sdf.parse(date1),sdf.parse(date2));
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2 ;
        if(day1 < day2) {
            monthInterval --;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }
}
