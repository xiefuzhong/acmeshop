package com.acme.acmemall.utils;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期处理
 *
 * @author admin
 * @date
 */
public class DateUtils {
    // 日志
    private static final Logger logger = Logger.getLogger(DateUtils.class);

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 无分隔符日期格式 "yyyyMMddHHmmssSSS"
     */
    public static String DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 日期转换格式数组
    public static String[][] regularExp = new String[][]{

            // 默认格式
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))\\s+([0,1]\\d|[2][0-3]|\\d):([0-5]\\d|\\d):([0-5]\\d|\\d)",
                    DATE_TIME_PATTERN},
            // 仅日期格式 年月日
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))",
                    DATE_PATTERN},
            //  带毫秒格式
            {"\\d{4}((([0][1,3-9]|[1][0-2]|[1-9])([0-2]\\d|[3][0,1]|[1-9]))|((02|2)(([1-9])|[0-2]\\d)))([0,1]\\d|[2][0-3])([0-5]\\d|\\d)([0-5]\\d|\\d)\\d{1,3}",
                    DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS}
    };

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df.format(date);
        }
        return null;
    }

    public static String timeToStr(Long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        if (time.toString().length() < 13) {
            time = time * 1000L;
        }
        Date date = new Date(time);
        String value = dateFormat.format(date);
        return value;
    }

    public static String timeToUtcDate(Long time, String pattern) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String dateStr = timeToStr(time, pattern);
        try {
            Date date1 = dateFormat.parse(dateStr);
            calendar.setTime(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    public static long strToTime(String timeStr) {
        Date time = strToDate(timeStr);
        return time.getTime() / 1000;
    }


    /**
     * 转换为时间类型格式
     *
     * @param strDate 日期
     * @return
     */
    public static Date strToDate(String strDate) {
        try {
            String strType = getDateFormat(strDate);
            SimpleDateFormat sf = new SimpleDateFormat(strType);
            return new Date((sf.parse(strDate).getTime()));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据传入的日期格式字符串，获取日期的格式
     *
     * @return 秒
     */
    public static String getDateFormat(String date_str) {
        String style = null;
        if (StringUtils.isEmpty(date_str)) {
            return null;
        }
        boolean b = false;
        for (int i = 0; i < regularExp.length; i++) {
            b = date_str.matches(regularExp[i][0]);
            if (b) {
                style = regularExp[i][1];
            }
        }
        if (StringUtils.isEmpty(style)) {
            logger.info("date_str:" + date_str);
            logger.info("日期格式获取出错，未识别的日期格式");
        }
        return style;
    }

    /**
     * 将字符串类型的转换成Date类型
     *
     * @param dateStr
     *            字符串类型的日期 yyyy-MM-dd
     * @return Date类型的日期
     * @throws ParseException
     */
    public static Date convertStringToDate(String dateStr) {
        // 返回的日期
        Date resultDate = null;
        try {
            // 日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            resultDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public static String compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return DATE1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return DATE2;
            } else {
                return DATE1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return DATE1;
    }


    /**
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String currentDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return dateFormat.format(new Date());
    }

    public static void main(String[] args) {
    	 String d= compare_date("2995-11-12 15:21", "1999-12-11 09:59");
         System.out.println("i=="+d);
    }


}
