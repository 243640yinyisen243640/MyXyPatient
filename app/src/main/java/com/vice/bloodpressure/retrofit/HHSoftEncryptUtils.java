package com.vice.bloodpressure.retrofit;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @类说明 加密/解密工具类
 * @作者 hhsoft
 * @创建日期 2019/8/18 17:43
 */
public class HHSoftEncryptUtils {
    private static final String tag = HHSoftEncryptUtils.class.getName();
    // AES加密需要使用的变量
    private static final int AES_KEY_LENGTH = 16;
    private static final byte[] OIV = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10}; // 此处向量可自定义，请注意如果超过0x80请加(byte)强制转换
    private static final String AES_DEFAULT_KEY = "1862b0deb369e73a";

    /**
     * AES加密：使用默认Key的方式进行AES加密
     *
     * @param sourceString 源字符串
     * @return 加密过后的字符串，加密失败返回null
     */
    public static String encryptAES(String sourceString) {
        return encryptAESWithKey(sourceString, AES_DEFAULT_KEY);
    }

    /**
     * AES加密：使用自定义Key的方式进行AES加密
     *
     * @param sourceString 源字符串
     * @param key          加密的Key值
     * @return 加密过后的字符串，加密失败返回null
     */
    public static String encryptAESWithKey(String sourceString, String key) {
        String result = encryptAES(sourceString, key);
        if (!TextUtils.isEmpty(result)) {
            result = result.replace("+", "%2b");
            result = result.replace("\r\n", "").replace("\n", "");
        }
        return result;
    }

    /**
     * AES加密：使用自定义Key的方式进行AES加密
     *
     * @param sourceString 源字符串
     * @param key          加密的Key值
     * @return 加密过后的字符串，加密失败返回null
     */
    private static String encryptAES(String sourceString, String key) {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] keyBytesTruncated = new byte[AES_KEY_LENGTH];
            for (int i = 0; i < AES_KEY_LENGTH; i++) {
                if (i >= keyBytes.length) {
                    keyBytesTruncated[i] = 0x12;
                } else {
                    keyBytesTruncated[i] = keyBytes[i];
                }
            }
            Key ckey = new SecretKeySpec(keyBytesTruncated, "AES");
            Cipher cp = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(OIV);
            cp.init(1, ckey, iv);
            byte[] inputByteArray = sourceString.getBytes("UTF-8");
            byte[] cipherBytes = cp.doFinal(inputByteArray);
            String result = Base64.encodeToString(cipherBytes, Base64.NO_WRAP);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag, "encodeAES_P16", e);
        }
        return null;
    }

    /**
     * AES解密：使用默认Key的方式进行AES解密
     *
     * @param sourceString 源字符串
     * @return 解密过后的字符串，加密失败返回null
     */
    public static String decryptAES(String sourceString) {
        return decryptAESWithKey(sourceString, AES_DEFAULT_KEY);
    }

    /**
     * AES解密：使用自定义Key的方式进行AES解密
     *
     * @param sourceString 源字符串
     * @param key          解密的Key值
     * @return 解密过后的字符串，加密失败返回null
     */
    public static String decryptAESWithKey(String sourceString, String key) {
        if (TextUtils.isEmpty(sourceString)) {
            return null;
        }
        sourceString = sourceString.replace("%2b", "+");
        return decryptAES(sourceString, key);
    }

    /**
     * AES解密：使用自定义Key的方式进行AES解密
     *
     * @param sourceString 源字符串
     * @param key          解密的Key值
     * @return 解密过后的字符串，加密失败返回null
     */
    private static String decryptAES(String sourceString, String key) {
        try {
            byte[] cipherByte = Base64.decode(sourceString, Base64.NO_WRAP);
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] keyBytesTruncated = new byte[AES_KEY_LENGTH];
            for (int i = 0; i < AES_KEY_LENGTH; i++) {
                if (i >= keyBytes.length) {
                    keyBytesTruncated[i] = 0x12;
                } else {
                    keyBytesTruncated[i] = keyBytes[i];
                }
            }
            Key ckey = new SecretKeySpec(keyBytesTruncated, "AES");
            Cipher cp = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(OIV);
            cp.init(2, ckey, iv);
            byte[] decryptBytes = cp.doFinal(cipherByte);
            return new String(decryptBytes, "UTF-8").replace("", "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag, "decodeAES_P16", e);
        }
        return null;
    }

    /**
     * SHA加密-SHA-256
     *
     * @param sourceString 源字符串
     * @return 传入文本内容，返回 SHA-256 串
     */
    public static String encryptSHA256(final String sourceString) {
        return encryptSHA(sourceString, "SHA-256");
    }

    /**
     * SHA加密-SHA-512
     *
     * @param sourceString 源字符串
     * @return 传入文本内容，返回 SHA-512 串
     */
    public static String encryptSHA512(final String sourceString) {
        return encryptSHA(sourceString, "SHA-512");
    }

    /**
     * SHA加密
     *
     * @param sourceString 源字符串
     * @param shaType      加密类型
     * @return 返回 SHA加密字符串
     */
    private static String encryptSHA(String sourceString, String shaType) {
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if (sourceString != null && sourceString.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(shaType);
                // 传入要加密的字符串
                messageDigest.update(sourceString.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();
                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
