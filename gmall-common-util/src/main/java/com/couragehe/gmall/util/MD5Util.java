package com.couragehe.gmall.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @PackageName:com.couragehe.gmall.util
 * @ClassName:MD5Util
 * @Description: MD5加密工具类
 * @Autor:CourageHe
 * @Date: 2020/1/18 23:27
 */
public class MD5Util {
    public static String md5(String text, String key)  {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text + key);
        System.out.println("MD5加密后的字符串为:" + md5str);
        return md5str;
    }

    // 不带秘钥加密
    public static String md52(String text)  {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text);
        System.out.println("MD52加密后的字符串为:" + md5str + "\t长度：" + md5str.length());
        return md5str;
    }

    /**
     * MD5验证方法
     *
     * @param text明文
     * @param key密钥
     * @param md5密文
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5str = md5(text, key);
        if (md5str.equalsIgnoreCase(md5)) {
            System.out.println("MD5验证通过");
            return true;
        }
        return false;
    }
}
