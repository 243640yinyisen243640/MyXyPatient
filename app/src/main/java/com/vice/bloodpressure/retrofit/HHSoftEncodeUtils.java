package com.vice.bloodpressure.retrofit;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @类说明 编码/解码工具类
 * @作者 hhsoft
 * @创建日期 2019/8/18 17:44
 */
public class HHSoftEncodeUtils {
    public static final String TAG = "HHSoftEncodeUtils";

    /**
     * Base64编码：对字符串进行base64编码，默认的编方式是utf-8
     *
     * @param sourceString 源字符串
     * @return 编码过后的字符串
     */
    public static String encodeBase64(String sourceString) {
        return Base64.encodeToString(sourceString.getBytes(), Base64.NO_WRAP);
    }

    /**
     * Base64编码：对字符串进行自定义编码方式的base64编码
     *
     * @param sourceString 源字符串
     * @param charset      编码方式
     * @return 编码过后的字符串, 编码失败返回空字符串
     */
    public static String encodeBase64WithCharset(String sourceString, String charset) {
        try {
            byte[] encode = Base64.encode(sourceString.getBytes(charset), Base64.NO_WRAP);
            return new String(encode, charset);
        } catch (Exception e) {
            Log.i(TAG, "encodeBase64", e);
        }
        return "";
    }

    /**
     * Base64解码：对字符串进行base64解码，默认的编方式是utf-8
     *
     * @param sourceString 源字符串
     * @return 解码编码过后的字符串, 解码失败返回null
     */
    public static String decodeBase64(String sourceString) {
        return decodeBase64WithCharset(sourceString, "utf-8");
    }

    /**
     * ase64解码：对字符串进行自定义编码方式的base64解码
     *
     * @param sourceString 源字符串
     * @param charSet      编码方式
     * @return 解码编码过后的字符串, 解码失败返回空字符串
     */
    public static String decodeBase64WithCharset(String sourceString, String charSet) {
        try {
            byte[] decode = Base64.decode(sourceString.getBytes(charSet), Base64.DEFAULT);
            return new String(decode, charSet);
        } catch (Exception e) {
            Log.i(TAG, "decodeBase64", e);
        }
        return "";
    }

    /**
     * URL编码:对字符串进行URL编码
     *
     * @param sourceString 源字符串
     * @return 编码过后的字符串, 编码失败返回空字符串
     */
    public static String encodeURL(String sourceString) {
        try {
            return URLEncoder.encode(sourceString, "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "decodeAES_P16", e);
        }
        return "";
    }

    /**
     * URL解码:对字符串进行URL解码
     *
     * @param sourceString 源字符串
     * @return 解码编码过后的字符串, 解码失败返回空字符串
     */
    public static String decodeURL(String sourceString) {
        try {
            return URLDecoder.decode(sourceString, "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "decodeAES_P16", e);
        }
        return "";
    }

    /**
     * Unicode编码：对字符串进行Unicode编码
     *
     * @param sourceString 源字符串
     * @return 编码过后的字符串, 编码失败返回空字符串
     */
    public static String encodeUnicode(String sourceString) {
        if (!TextUtils.isEmpty(sourceString)) {
            char c;
            StringBuilder str = new StringBuilder();
            int intAsc;
            String strHex;
            for (int i = 0; i < sourceString.length(); i++) {
                c = sourceString.charAt(i);
                intAsc = (int) c;
                strHex = Integer.toHexString(intAsc);
                if (intAsc > 128)
                    str.append("\\u" + strHex);
                else // 低位在前面补00
                    str.append("\\u00" + strHex);
            }
            return str.toString();
        }
        return "";
    }

    /**
     * Unicode解码：对字符串进行Unicode解码
     *
     * @param sourceString 源字符串
     * @return 解码过后的字符串, 解码失败返回空字符串
     */
    public static String decodeUnicode(String sourceString) {
        if (!TextUtils.isEmpty(sourceString)) {
            StringBuffer retBuf = new StringBuffer();
            int maxLoop = sourceString.length();
            for (int i = 0; i < maxLoop; i++) {
                if (sourceString.charAt(i) == '\\') {
                    if ((i < maxLoop - 5) && ((sourceString.charAt(i + 1) == 'u') || (sourceString.charAt(i + 1) == 'U')))
                        try {
                            retBuf.append((char) Integer.parseInt(sourceString.substring(i + 2, i + 6), 16));
                            i += 5;
                        } catch (NumberFormatException localNumberFormatException) {
                            retBuf.append(sourceString.charAt(i));
                        }
                    else
                        retBuf.append(sourceString.charAt(i));
                } else {
                    retBuf.append(sourceString.charAt(i));
                }
            }
            return retBuf.toString();
        }
        return "";
    }

    /**
     * MD5编码：获取字符串的md5加密值，16位
     *
     * @param sourceString 源字符串
     * @return 编码过后的字符串, 编码失败返回null
     */
    public static String encodeMD5_16(String sourceString) {
        // 加密后的字符串
        String newstr = null;
        String result = encodeMD5_32(sourceString);
        if (!TextUtils.isEmpty(result)) {
            newstr = result.substring(8, 24);
        }
        return newstr;
    }

    /**
     * MD5编码：获取一个字符串加密过后的MD5值
     *
     * @param sourceString 源字符串
     * @return 编码过后的字符串, 编码失败返回null
     */
    public static String encodeMD5_32(String sourceString) {
        String s = null;
        // 用来将字节转换成16进制表示的字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] source = sourceString.getBytes();
            md.update(source);
            // MD5的计算结果是一个128位的长整数，用字节表示为16个字节
            byte[] tmp = md.digest();
            // 每个字节用16进制表示的话，使用2个字符(高4位一个,低4位一个)，所以表示成16进制需要32个字符
            char[] str = new char[16 * 2];
            int k = 0;// 转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 对MD5的每一个字节转换成16进制字符
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 对字节高4位进行16进制转换
                str[k++] = hexDigits[byte0 & 0xf]; // 对字节低4位进行16进制转换
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.i(TAG, "encodeMD5", e);
        }
        return s;
    }
}
