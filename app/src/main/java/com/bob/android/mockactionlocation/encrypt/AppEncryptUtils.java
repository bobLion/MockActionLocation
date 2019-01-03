package com.bob.android.mockactionlocation.encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @package com.bob.android.mockactionlocation.encrypt
 * @fileName AppEncryptUtils
 * @Author Bob on 2019/1/3 9:51.
 * @Describe TODO
 */
public class AppEncryptUtils {
    public static String encodeParam(String key, String param) {
        return encodeParam(key,param,true);
    }

    public static String encodeParam(String key, String param, boolean urlEncoder) {
        String encode = AESEncrypt.encode(key,param);
        if (urlEncoder) {
            try {
                return URLEncoder.encode(encode, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("URLEncoder.encode出错", e);
            }
        }
        return encode;
    }

    public static String encodeKey(String key) {
        return encodeKey(key,true);
    }

    public static String encodeKey(String key, boolean urlEncoder) {
        try {
            String encode = RSAPublicKeyUtils.encodeKey(key);

            if (urlEncoder) {
                try {
                    return URLEncoder.encode(encode, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("URLEncoder.encode出错", e);
                }
            }
            return encode;
        } catch (Exception e) {
            throw new RuntimeException("RSA加密出错", e);
        }
    }
}
