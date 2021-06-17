package com.vice.bloodpressure.utils;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建者 田汉鑫
 * 创建时间 2016/3/21
 * 数据转换功能集合
 */
public class DataConvert {

    public static String[] weekDays = {"00", "01", "02", "03", "04", "05", "06"};

    /**
     * 将整型值转化为HEX字符串
     *
     * @param value       待转化整型值
     * @param bytesLength HEX数据长度，只能为1或2或4
     * @return 转化后HEX字符串-正确 null-错误
     */
    public static String toHexString(int value, int bytesLength) {
        if (bytesLength != 1 && bytesLength != 2 && bytesLength != 4) {
            return null;
        }
        int mask = 0;
        switch (bytesLength) {
            case 1:
                mask = 0x000000FF;
                break;
            case 2:
                mask = 0x0000FFFF;
                break;
            case 4:
                mask = 0xFFFFFFFF;
                break;
        }

        int length = 2 * bytesLength;

        String ret = Integer.toHexString(value & mask);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (length - ret.length()); i++) {
            sb.append("0");
        }
        sb.append(ret);
        return sb.toString().toUpperCase();
    }

    /**
     * 将byte值转化为HEX字符串
     *
     * @param value 带转化值
     * @return 转化后的HEX字符串-正确 null-错误
     */
    public static String toHexString(byte value) {
        String tmp = Integer.toHexString(value & 0xFF);
        /*
        为保证返回的字符串能够正确转化为byte数组
        保证字符串长度为2的倍数
         */
        return tmp.length() % 2 == 1 ? "0" + tmp : tmp;
    }

    public static String toHexString(byte[] values) {
        return toHexString(values, 0, values.length);
    }

    /**
     * 获取指定长度的字符串
     *
     * @param oldString 转化之前字符串
     * @param length    长度
     * @return 固定长度的字符串
     */
    public static String getString(String oldString, int length) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < length - oldString.length(); i++) {
            stringBuffer.append("0");
        }
        stringBuffer.append(oldString);
        return stringBuffer.toString();
    }

    /**
     * 将byte数组转化为HEX字符串
     *
     * @param value  带转化byte数组
     * @param offset 数组开始位置
     * @param length 解析长度
     * @return 转化后的字符串-正确 null-错误
     */
    public static String toHexString(byte[] value, int offset, int length) {
        /*
        确保带转化数组有效
        确保传入的数组位置和长度信息没有越界
         */
        if (value == null || offset < 0 || length < 0 || offset + length > value.length) {
            return null;
        }

        StringBuilder retBuffer = new StringBuilder();

        for (int i = offset; i < offset + length; i++) {
            retBuffer.append(toHexString(value[i]));
        }

        return retBuffer.toString();
    }

    /**
     * 将16进制字符串转换成byte数组
     *
     * @param value 待转换字符串
     * @return 转换完成的字符串-正确 null-错误
     */
    public static byte[] toBytes(String value) {
        // 确保传入数据有效
        if (value == null || value.trim().length() <= 0) {
            return null;
        }
        value = value.trim();
        // 确保有效数据长度有效（为2的倍数）
        if (value.length() % 2 != 0) {
            return null;
        }


        byte[] ret = new byte[value.length() / 2];
        for (int i = 0; i < ret.length; i++) {
            try {
                /*
                前面的逻辑已经保证了字符串长度是byte数组长度的2倍
                这里没有进行数组越界的判断
                 */
                String tmp = value.substring(i * 2, i * 2 + 2);
                int a = Integer.parseInt(tmp, 16);
                int b = a & 0xFF;
                byte c = (byte) b;
                ret[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
            } catch (Exception e) {
                return null;
            }
        }
        return ret;
    }

    /**
     * 把byte[]数组转换成16进制字符串
     *
     * @param b byte
     * @return String
     */
    public static String byte2HexStr(byte[] b) {
        String stmp ;
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            stmp = Integer.toHexString(value & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
//            sb.append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * 获取一段字符串的长度的HEX表示
     * 例如输入“010203405006”,1返回“06”，输入“010203040506”，2返回“0006”
     *
     * @param value       待计算字符串
     * @param bytesLength HEX长度
     * @return 长度的HEX表示
     */
    public static String getHexLength(String value, int bytesLength) {
        if (value == null) {
            return null;
        }
        if (value.trim().length() % 2 == 1) {
            return null;
        }
        return toHexString(value.trim().length() / 2, bytesLength);
    }

    /**
     * 获取一段字符串的和校验值
     *
     * @param value 待计算字符串
     * @return 校验字符串-成功 null-失败
     */
    public static String getSumValue(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() == 0 || value.length() % 2 == 1) {
            return null;
        }
        int checkValue = 0;
        /*
        每两个字符当作一个byte值进行加法计算
        如果解析错误，则看作计算校验值错误
         */
        for (int i = 0; i < value.length() - 1; i += 2) {
            try {
                checkValue += Integer.parseInt(value.substring(i, i + 2), 16);
            } catch (Exception e) {
                return null;
            }
        }

        return DataConvert.toHexString(checkValue, 1);
    }

    /**
     * 获取处理后的定长字符串
     *
     * @param source  原数据
     * @param length  处理长度，原数据超过length截取length长度
     * @param addLeft true-长度不足时在左侧补0 false-长度不足时在右侧不0
     * @return 处理后的字符串
     */
    public static String getFixedStringWithChar(String source, int length, boolean addLeft, char addChar) {
        if (length <= 0) { // 如果length不合法，返回长度为0的字符串
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (source == null) { // 如果原数据为空，则返回全0字符串
            for (int i = 0; i < length; i++) {
                sb.append(addChar);
            }
        } else if (source.length() < length) { // 根据左补字符或右补字符
            if (addLeft) {
                for (int i = 0; i < length - source.length(); i++) {
                    sb.append(addChar);
                }
                sb.append(source);
            } else {
                sb.append(source);
                for (int i = 0; i < length - source.length(); i++) {
                    sb.append(addChar);
                }
            }
        } else { // 超过长度的字节截取固定长度
            sb.append(source, 0, length);
        }
        return sb.toString();
    }

    /**
     * 返回在HEX字符串代表的数字固定数位上的值
     *
     * @param hexString HEX字符串
     * @param mask      固定数位掩码
     * @return 固定数位上的值
     * @throws DataConvertException 数据转换时发生错误
     */
    public static int getMaskedValue(String hexString, int mask) throws DataConvertException {
        int ret;
        try {
            ret = Integer.parseInt(hexString, 16) & mask;
        } catch (Exception ex) {
            throw new DataConvertException(ex.getMessage());
        }
        return ret;
    }

    /**
     * HEX字符串依照byte数组方式部分倒序
     *
     * @param source 输入的原字符串
     * @param start  倒序开始位置
     * @param end    倒序结束位置
     * @return 倒序后的字符串
     */
    public static String strReverse(String source, int start, int end) {
        if (start > end || source == null || source.length() % 2 == 1 || end > source.length() ||
                start % 2 == 1 || end % 2 == 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(source, 0, start);
        for (int i = end; i > start; i -= 2) {
            sb.append(source, i - 2, i);
        }
        sb.append(source.substring(end));

        return sb.toString();
    }

    /**
     * HEX字符串依照byte数组方式全部增加一个值
     *
     * @param source   源字符串
     * @param addValue 每个byte增加的值
     * @return 返回的字符串
     */
    public static String stringHexAddEach(String source, byte addValue) {
        if (source == null || source.length() % 2 == 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.length(); i += 2) {
            try {
                int tmp = Integer.parseInt(source.substring(i, i + 2), 16);
                String tmpStr = DataConvert.toHexString(tmp + addValue, 1);
                if (tmpStr != null) {
                    sb.append(tmpStr);
                } else {
                    return null;
                }
            } catch (Exception ex) { // 如果获取HEX值错误，则数据不符合规定，返回空值
                return null;
            }
        }
        return sb.toString();
    }

    /**
     * HEX字符串依照byte数组方式全部增加一个值
     *
     * @param source     源字符串
     * @param minusValue 每个byte增加的值
     * @return 返回的字符串
     */
    public static String stringHexMinusEach(String source, byte minusValue) {
        if (source == null || source.length() % 2 == 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.length(); i += 2) {
            try {
                int tmp = Integer.parseInt(source.substring(i, i + 2), 16);
                if (tmp < 0x33) {
                    tmp += 0x100;
                }
                String tmpStr = DataConvert.toHexString(tmp - minusValue, 1);
                if (tmpStr != null) {
                    sb.append(tmpStr);
                } else {
                    return null;
                }
            } catch (Exception ex) { // 如果获取HEX值错误，则数据不符合规定，返回空值
                return null;
            }
        }
        return sb.toString();
    }

    /**
     * 转换波特率为P645规约规定的值
     *
     * @param baud 波特率字符串
     * @return null-字符串错误 其它-波特率HEX字符串
     */
    public static String getBaudValue(String baud) {
        baud = baud.trim();
        switch (baud) {
            case "600":
                return "02";
            case "1200":
                return "04";
            case "2400":
                return "08";
            case "4800":
                return "10";
            case "9600":
                return "20";
            case "19200":
                return "40";
            default:
                return null;
        }
    }

    /**
     * 根据传入的格式字符串返回当前时间字符串
     *
     * @param formatString 格式字符串，如：yyyymmddhhMMss
     * @return 当前时间格式化输入
     */
    public static String getCurrentTimeString(String formatString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(formatString);
        return df.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期是本周第几天
     *
     * @return 周日-00， 周一-01 ...
     */
    public static String getWeekOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 将HEX数组转换为中文字符
     *
     * @param hexString HEX数组
     * @return 中文字符
     */
    public static String hexStringToChinese(String hexString) {
        byte[] hexBytes = toBytes(hexString);

        // unicode 需要两个字节一组,出现单字节时需要补位
        if (hexBytes!=null&&hexBytes.length % 2 != 0){
            byte[] byte_end = new byte[]{0x00};
            hexBytes = mergeArray(hexBytes,byte_end);
        }else{
            return "";
        }
        try {
            return new String(hexBytes, 0, hexBytes.length, "UnicodeLittleUnmarked").trim();
        } catch (Exception ex) {
            return "";
        }
    }
    /**
     * 将两个byte[] 合并,
     * @param bytes byte
     * @param byte_end byte
     * @return bytes + byte_end 的结果
     */
    private static byte[] mergeArray(byte[] bytes,  byte[] byte_end) {

        int byteLen1=bytes.length;
        int byteLen2=byte_end.length;
        bytes= Arrays.copyOf(bytes,byteLen1+ byteLen2);//扩容
        System.arraycopy(byte_end, 0, bytes, byteLen1,byteLen2 );
        return bytes;
    }
    /**
     * 根据传入的时间格式和时间字符串获取时间
     *
     * @param dateString 时间字符串
     * @param format     时间格式串
     * @return 时间实例 错误返回当前时间
     */
    public static Date getDateInstance(String dateString, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
        try {
            return simpleFormat.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Date();
        }
    }

    /**
     * 获取两个时间的日期差
     *
     * @param dateString1 日期1
     * @param dateString2 日期2
     * @param format      时间格式串
     * @return 0-同一天 <0-日期1在日期2之前 >0-日期1在日期2之后
     */
    public static int getDateBetween(String dateString1, String dateString2, String format) {
        long date1 = getDateInstance(dateString1, format).getTime() / (24 * 3600);
        long date2 = getDateInstance(dateString2, format).getTime() / (24 * 3600);
        return (int) (date1 - date2);
    }

    public static class DataConvertException extends Exception {
        public DataConvertException(String message) {
            super(message);
        }
    }

    /**
     * byte数组中的数据转为Int数据
     *
     * @param b      byte数组(低字节在前，高字节在后)
     * @param offset 转换开始的位置
     * @param len    byte数据的个数
     * @return  Int数据
     */
    public static int byteToInt(byte[] b, int offset, int len) {

        int mask = 0xff;
        int temp ;
        int n = 0;
        for (int i = offset + len - 1; i >= offset; i--) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }


    /**
     * 将IP地址转化为byte数组
     *
     * @param ipStr 输入的ip地址
     * @return 16进制ip地址（4字节）
     */
    public static String toHexStringIP(String ipStr) {
        String[] p = "\\.".split(ipStr);
        byte[] pArgu = new byte[4];

        for (int i = 0; i < p.length; i++) {
            pArgu[i] = (byte) ((Integer.parseInt(p[i])));
        }
        String ipHexString = toHexString(pArgu, 0, pArgu.length);
        return strReverse(ipHexString, 0, ipHexString.length());
    }

    /**
     * 转化ip格式
     *
     * @param ip 转字符
     * @return ip字符
     */
    public static String toStringIp(String ip) {
        StringBuilder s_ip = new StringBuilder();
        byte[] ips = toBytes(ip);
        for (byte b : ips) {
            s_ip.append(b & 0x000000ff).append(".");
        }
        return s_ip.substring(0, s_ip.length() - 1);
    }

    /**
     * 将APN地址转化为16进制字符串
     *
     * @param value 输入的字符串
     * @return 16进制字符串（16字节）
     */
    public static String toHexString(String value) {
        byte[] apnTemp = new byte[16];
        System.arraycopy(value.getBytes(), 0, apnTemp,
                apnTemp.length - value.length(), value.length());
        String apnHexString = toHexString(apnTemp, 0, apnTemp.length);
        return strReverse(apnHexString, 0, apnHexString.length());
    }

    public static String getMeterData(String meteData) {
        String meteDataReverse = strReverse(meteData, 0, meteData.length());
        String data1 = meteDataReverse != null ? meteDataReverse.substring(0, 6) : null;
        String data2 = meteDataReverse != null ? meteDataReverse.substring(6, meteData.length()) : null;
        Pattern pattern = Pattern.compile("[1-9].*");
        Matcher matcherData1 = null;
        if (data1 != null) {
            matcherData1 = pattern.matcher(data1);
        }
        String group = null;
        if (matcherData1 != null) {
            if (matcherData1.find()) {
                group = matcherData1.group(0);
            } else {
                group = "0";
            }
        }
        String finalData = group + "." + data2;
        finalData = finalData.replaceAll("0+?$", "");
        finalData = finalData.replaceAll("[.]$", "");
        return finalData;
    }

    /**
     * 16进制字符串转二进制字符串
     *
     * @param  hexString 16进制字符
     * @return 二进制字符串
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        StringBuilder bString = new StringBuilder();
        String tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString.append(tmp.substring(tmp.length() - 4));
        }
        StringBuilder stringBuffer = new StringBuilder();
        if (bString.length() < 8) {
            for (int i = 0; i < 8 - bString.length(); i++) {
                stringBuffer.append("0");
            }
        }
        stringBuffer.append(bString);
        return stringBuffer.toString();
    }


    /**
     * 将一个字符串分割成指定长度的字符串集合
     * @param data  数据源
     * @param length  分割的字符串长度
     * @return 集合
     */
    public static List<String> getStringList(String data,int length){
        List<String> strList = new ArrayList<>();
        while (data.length()>=length){
            strList.add(data.substring(0,length));
            data=data.substring(length);
        }
        if (data.length()>0){
            strList.add(data);
        }
        return  strList;
    }


    /**
     * Cursor对象转数据集合
     * @param c 对象
     * @param clazz object
     * @return list
     */
    public static List cursor2VOList(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        List list = new LinkedList();
        Object obj;
        try {
            while (c.moveToNext()) {
                obj = setValues2Fields(c, clazz);

                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR @：cursor2VOList");
            return null;
        } finally {
            c.close();
        }
    }

    private static Object setValues2Fields(Cursor c, Class clazz)
            throws Exception {

        String[] columnNames = c.getColumnNames();// 字段数组
        //init a instance from the VO`s class
        Object obj = clazz.newInstance();
        //return a field array from obj`s ALL(include private exclude inherite(from father)) field
        Field[] fields = clazz.getDeclaredFields();

        for (Field _field : fields)
        {
            //field`s type
            Class<?> typeClass = _field.getType();// 属性类型
            for (String columnName : columnNames) {
                typeClass = getBasicClass(typeClass);
                //if typeclass is basic class ,package.if not,no change
                boolean isBasicType = isBasicType(typeClass);

                if (isBasicType) {
                    if (columnName.equalsIgnoreCase(_field.getName())) {// 是基本类型
                        String _str = c.getString(c.getColumnIndex(columnName));
                        if (_str == null) {
                            break;
                        }
                        //if value is null,make it to ""
                        //use the constructor to init a attribute instance by the value
                        Constructor<?> cons = typeClass
                                .getConstructor(String.class);
                        Object attribute = cons.newInstance(_str);
                        _field.setAccessible(true);
                        //give the obj the attr
                        _field.set(obj, attribute);
                        break;
                    }
                } else {
                    Object obj2 = setValues2Fields(c, typeClass);// 递归
                    _field.set(obj, obj2);
                    break;
                }

            }
        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass 类型
     * @return class
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        return typeClass.equals(Integer.class) || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class);
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<>();
    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }
    /**
     * 16进制的字符串转化为utf-8格式的字符串
     * @param hexStr 16进制的字符串
     * @return utf-8格式的字符串
     */
    public static String hexStrToUTF8(String hexStr) {
        byte[] baKeyword = new byte[hexStr.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(hexStr.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            hexStr = new String(baKeyword, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return hexStr;
    }

}
