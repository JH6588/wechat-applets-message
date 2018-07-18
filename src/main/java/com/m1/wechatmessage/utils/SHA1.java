/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.m1.wechatmessage.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 */
public class SHA1 {

    public static String getString2SHA1(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

        messageDigest.update(data.getBytes("UTF8"));
        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }


    public static String getString2SHA1(String... data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Collections.sort(Arrays.asList(data));
        String dataStr = String.join("", data);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.update(dataStr.getBytes("UTF8"));
        return DatatypeConverter.printHexBinary(messageDigest.digest()).toLowerCase();
    }

}
