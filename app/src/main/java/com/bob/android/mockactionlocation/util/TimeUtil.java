package com.bob.android.mockactionlocation.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @package com.bob.android.mockactionlocation.util
 * @fileName TimeUtil
 * @Author Bob on 2018/12/21 18:15.
 * @Describe TODO
 */
public class TimeUtil {
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    /**
     * 两个时间段的间隔
     *
     * @param startTime
     * @param endTime
     * @return 返回天数
     */
    public static long spaceTime(String startTime, String endTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long days = 0;
        try {
            Date d1 = df.parse(startTime);
            Date d2 = df.parse(endTime);
            long diff = d2.getTime() - d1.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 两个时间段的间隔
     *
     * @param startTime
     * @param endTime
     * @return 返回天数
     */
    public static boolean isSpaceTime(DateFormat df, String startTime, String endTime) {
        try {
            Date startData = df.parse(startTime);
            Date endData = df.parse(endTime);
            if (endData.getTime() < startData.getTime()) {
                return false;
            }
            int days = (int) (endData.getTime() - startData.getTime());
            if (days > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 两个时间段的间隔长度
     *
     * @param startTime
     * @param endTime
     * @return 返回天数
     */
    public static boolean timeInterval(DateFormat df, String startTime, String endTime) {
        boolean days = false;
        try {
            Date d1 = df.parse(startTime);
            Date d2 = df.parse(endTime);
            long diff = d2.getTime() - d1.getTime();
            if (diff <0) {//结束时间小于开始时间
                days = false;
            } else {
                if (diff - (1000 * 60 * 60 * 24) < 0) {//如果小于一天
                    days = true;
                }else {
                    days = false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static String getDateTime(String dateTime){
        Date date  = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = format1.format(date);
        return dateTime;
    }
}
