package com.me.house.common.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2018/8/4.
 */
public class HashUtils {

    private static final HashFunction FUNCTION = Hashing.md5();
    //加盐，加到字符串中一起进行md5 hashing，防止被暴力破解
    private static final String SALT = "biubiubiu";

    public static String encryptPassword(String passwd) {
        HashCode hashCode = FUNCTION.hashString(passwd + SALT, Charset.forName("UTF-8"));
        return hashCode.toString();
    }
}
