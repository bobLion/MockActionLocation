package com.bob.android.mockactionlocation.encrypt;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @package com.bob.android.mockactionlocation.encrypt
 * @fileName RSAPublicKeyUtils
 * @Author Bob on 2019/1/3 9:51.
 * @Describe TODO
 */
public class RSAPublicKeyUtils {


    private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI4l45HJaBrgTCti2+fMsmEJOAh/R6/oqqhZIsrj7EnhfU41G5l+ogiOwk+nkxO+JoTx2LPq1YB7+830GYScp00167DIo82UK8lvwVDBpTpdh5DSZTX4FRkeWOzK5GQJADhXDhNgJPo29YJ2XO4eSURMR6uKWkAVIeJvTgLC1gowIDAQAB";

    private static RSAPublicKey publicKey = null;

    /**
     * 根据默认的秘钥解密请求参数中的key的值
     * @Title: decodeKey
     * @param encryptKey
     * @return
     * String
     * @author YaoWei
     * @throws Exception
     */
    public static String decodeKey(String encryptKey) throws Exception{
        byte[] cipherData = decrypt(getRSAPrivateKey(), Base64.decode(encryptKey.toCharArray()));
        return new String(cipherData);
    }
    /**
     * 公钥加密
     * @param key 需要加密信息
     * @return
     * @throws Exception
     */
    public static String encodeKey(String key) throws Exception {
        byte[] cipherData = encrypt(getRSAPrivateKey(), key.getBytes());
        return Base64.encode(cipherData);
    }

    private static RSAPublicKey getRSAPrivateKey() {
        if(publicKey == null) {
            try {
                publicKey = loadPublicKeyByStr(DEFAULT_PUBLIC_KEY);
            } catch (Exception e) {
                throw new RuntimeException("生成RSAPublicKey时出现错误" , e);
            }
        }
        return publicKey;
    }

    /**
     * 公钥解密过程
     *
     * @param publicKey
     *            公钥
     * @param cipherData
     *            密文数据
     * @return 明文
     * @throws Exception
     *             解密过程中的异常信息
     */
    private static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查", e);
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法", e);
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏", e);
        }
    }

    /**
     * 公钥加密过程
     *
     * @param publicKey
     *            公钥
     * @param plainTextData
     *            明文数据
     * @return
     * @throws Exception
     *             加密过程中的异常信息
     */
    private static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
            throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查", e);
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法", e);
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏", e);
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     */
    private static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr.toCharArray());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public static void main(String[] args){
        String text = "大桥下有过一群鸭,我和你心连心,都是北京人";
        try {
            String encodeKey = RSAPublicKeyUtils.encodeKey(text);
            System.out.println(encodeKey);
            String decodeKey = RSAPrivateKeyUtils.decodeKey(encodeKey);
            System.out.println(decodeKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
