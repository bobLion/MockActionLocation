package com.bob.android.mockactionlocation.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @package com.bob.android.mockactionlocation.encrypt
 * @fileName AESEncrypt
 * @Author Bob on 2019/1/3 9:49.
 * @Describe TODO
 */
public class AESEncrypt {

    private static final String defaultCharset = "UTF-8";
    private static final String VIPARA = "sailingAESvipara";
    private static final String key_chars = "abcdefghijklmnopqrstuvwxyz1234567890+-/ABCDEFGHIJKLMNOPQRSTUVWXYZ=";

    /**
     *
     * @param key
     *            加密key--16位
     * @param content
     *            加密内容
     * @return
     */
    public static String encode(String key, String content) {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        try {
            return Base64.encode(cipher.doFinal(content.getBytes(defaultCharset)));
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            throw new RuntimeException("加密过程出现错误", e);
        }
    }
    /**
     *
     * @param key
     *            加密key--16位
     * @param content
     *            加密内容
     * @return
     */
    public static String decode(String key, String content) {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        try {
            return new String(cipher.doFinal(Base64.decode(content.toCharArray())), defaultCharset);
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            throw new RuntimeException("解密过程出现错误", e);
        }
    }

    /**
     * 获取加密算法
     *
     * @param
     *
     * @param cipherMode
     *            加密/解密
     * @return
     */
    private static Cipher getCipher(String keyStr, int cipherMode) {
        Cipher cipher = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, key, zeroIv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("初始化AES加密算法出错", e);
        }
        return cipher;
    }
    /**
     * 生成16位key
     * @return
     */
    public static String randomKey() {
        StringBuilder keyBuilder = new StringBuilder();
        Random randomIndex = new Random();
        for(int i=0; i<16; i++) {
            keyBuilder.append(key_chars.charAt(randomIndex.nextInt(key_chars.length())));
        }
        return keyBuilder.toString();
    }
}
