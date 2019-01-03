package com.bob.android.mockactionlocation.encrypt;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @package com.bob.android.mockactionlocation.encrypt
 * @fileName RSAPrivateKeyUtils
 * @Author Bob on 2019/1/3 9:52.
 * @Describe TODO
 */
public class RSAPrivateKeyUtils {

    private static final String DEFAULT_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALWzziCuoy62JT8v5VCoCfdzKmfR7CwZu+tcNXjFAWhx5Q1V53PjozNpCY2dzV4m8mpzSi1tWLDZjwBSO2L8cqIX1+GCvtXZjENy+F+PhkHB9eruo6PCstTOYUiCS8+sMpaz50bg5uMPrCw0dJFNd6M6aZo5gpZsybC98DuwgRj7AgMBAAECgYEAhGYSSso16bGbjmFODOmE6aQnQCM8nh3aSnOUGv4iOoLTTlZMIqu6nZlp9qEHphVnCp4bbOKCf8rnYmqJD2W3+Jq03P1HSteLF/rQYPu6elbLTUGuzxfxog1d5ZztmW7CsLGvoiHujQaOwsoawbDt1LTKvLNmb348RSV5X7yrUMECQQD7eMmA+8pQ6lr80o9dBdmc0iSKDMz0bIbfE0FmvxxpaNmlDt689mWNqN36eUpmDUmv92zd1tuy+lYuber+VoqxAkEAuPlmoLKWM94iSJ8u25sU8wHk9ze5Ncb2vcKvNRe8eAUr3SYeN5lkuWmi27g0IdGRlw6/ZX8XLLm7SBmtLi1xawJBAOzJiYPok1Kpuc9CPTgsYAIoZbuP3hVxc3Xt3bFv1sLyta2UWOu5X1qKXaqSNxns/SuyYVJqFCltS2UgnxuqRaECQDLuUOi8Y4kih7QRSHilKc6DXgUH7dI112iGe3GV+InfeqVowncyoD5BLNtsy8XoCztZjy+mH75r4XpHv5xwXMMCQG1p6fXmwdRrO0axhxBAgTulmI5Mx0CIsXSrbrJg89wDtNEP3u1S+rLayEbONB9jMlYh1eE1jgcFCJWMfEGvYEA=";

    private static RSAPrivateKey privateKey = null;

    /**
     * 根据默认的秘钥解密请求参中的key的值
     * @param encryptContents 加密内容
     * @return
     * String
     * @author YaoWei
     * @throws Exception
     */
    public static String decodeKey(String encryptContents) throws Exception{
        byte[] cipherData = decrypt(getRSAPrivateKey(), Base64.decode(encryptContents.toCharArray()));
        return new String(cipherData);
    }

    /**
     * 解密IOS传的请求参
     * @Title: decodeKeyForIOS
     * @param encryptContents 加密内容
     * @return
     * String
     * @author YaoWei
     * @throws Exception
     */
    public static String decodeKeyForIOS(String encryptContents) throws Exception{
        return decodeKey(encryptContents);
    }
    /**
     * 加密
     * @param contents 加密内容
     * @return
     * @throws Exception
     */
    public static String encodeKey(String contents) throws Exception {
        byte[] cipherData = encrypt(getRSAPrivateKey(), contents.getBytes());
        return Base64.encode(cipherData);
    }

    /**
     * 私钥解密过程
     *
     * @param privateKey
     *            私钥
     * @param cipherData
     *            密文数据
     * @return 明文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查", e);
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法", e);
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏", e);
        }
    }
    /**
     * 私钥加密过程
     *
     * @param privateKey
     *            私钥
     * @param plainTextData
     *            明文数据
     * @return
     * @throws Exception
     *             加密过程中的异常信息
     */
    private static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData)
            throws Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查", e);
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法", e);
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏", e);
        }
    }

    private static RSAPrivateKey getRSAPrivateKey() {
        if(privateKey == null) {
            try {
                privateKey = loadPrivateKeyByStr(DEFAULT_PRIVATE_KEY);
            } catch (Exception e) {
                throw new RuntimeException("生成RSAPrivateKey时出现错误" , e);
            }
        }
        return privateKey;
    }

    private static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr.toCharArray());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空", e);
        }
    }
}
