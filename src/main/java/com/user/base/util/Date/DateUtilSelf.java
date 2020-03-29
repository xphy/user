package com.user.base.util.Date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/23 17:46
 * @description：日期工具类
 * @modified By：
 * @version: 1$
 */
public class DateUtilSelf {
    /**
     * @description: 获得两个时间之差
     * @param endDate
     * @param nowDate
     * @return: java.lang.Long
     * @author: Andy
     * @time: 2020/3/19 10:47
     */
    public static Long getDateLong(Date endDate, Date nowDate) {
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        return  diff;
    }
    /**
     * @description: 获取当前系统时间
     * @param
     * @return: java.lang.String
     * @author: Andy
     * @time: 2020/3/19 10:48
     */
    public static String getNowDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    /**
     * @description: Long转String
     * @param dateLong
     * @return: java.lang.String
     * @author: Andy
     * @time: 2020/3/19 10:46
     */
    public static String getStringByLong(long dateLong) {
        long nd = 1000 * 24 * 60 * 60;
//        if(dateLong < nd){
//            dateLong = dateLong +nd;
//        }
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 计算差多少天
        long day = dateLong / nd;
        // 计算差多少小时
        long hour = dateLong % nd / nh;
        // 计算差多少分钟
        long min = dateLong % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = dateLong % nd % nh % nm / ns;

        String dateStr = day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        return dateStr;
    }

    public static String addYear(String  year) {
        String s = year.split("-")[0];
        int endYear = Integer.parseInt(s)+ 1;
        return endYear+"-01-01";
    }
    public static String addMonth(String month) {
        String year = month.split("-")[0];
        String mon = month.split("-")[1];
        int endYear = Integer.parseInt(year);
        int endMonth = Integer.parseInt(mon)+ 1;
        if(endMonth > 12 ){
            endMonth = endMonth- 12;
            endYear++;
        }
        return endYear+"-"+endMonth+"-01";
    }
}
