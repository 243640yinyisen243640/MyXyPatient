package com.vice.bloodpressure.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {
    public static long currentTimestamp() {
        Date currentDate = new Date();
        return currentDate.getTime();
    }

    /**
     * 获取当前时间字符串
     *
     * @param outFormat 字符串输出时的格式
     * @return
     */
    public static String currentDateString(String outFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(outFormat);
            String dateStr = format.format(new Date());
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }
    }

    /**
     * 把时间字符串转换成Date
     *
     * @param dateString 要转换的字符串
     * @param inFormat   符串的格式，例如yyyy-MM-dd HH：mm:ss
     * @return 果转换成功返回转换以后的date，转换失败的话返回null
     */
    public static Date convertStringToDate(String dateString, String inFormat) {
        SimpleDateFormat format = new SimpleDateFormat(inFormat);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (Exception e) {
        }
        return date;
    }
}
