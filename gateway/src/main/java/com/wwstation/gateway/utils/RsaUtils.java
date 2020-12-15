package com.wwstation.gateway.utils;

import com.wwstation.common.components.RedisExpireTime;
import com.wwstation.common.exceptions.AuthAsserts;
import com.wwstation.gateway.utils.redis.RedisUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author william
 * rsa 加解密工具类
 * 工具包主要含有如下工具：
 * 生成密钥对
 * 获取公钥，私钥功能
 * 公钥加解密
 * 私钥加解密
 * 在鉴权中，前端请求加密公钥，后端将保存密钥对，使用uuid保存在redis中
 * 此uuid将作为今后jwt加解密的uuid
 * @Date: 2020-12-02 17:19
 */
@Component
public class RsaUtils {
    @Autowired
    RedisUtils redisUtils;
    /** */
    /**
     * 加密算法RSA
     */
    public final String KEY_ALGORITHM = "RSA";

    /** */
    /**
     * 签名算法
     */
    public final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /** */
    /**
     * 获取公钥的key
     */
    private final String PUBLIC_KEY = "publicKey";

    /** */
    /**
     * 获取私钥的key
     */
    private final String PRIVATE_KEY = "privateKey";

    /** */
    /**
     * RSA最大加密明文大小
     */
    private final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private final int MAX_DECRYPT_BLOCK = 128;

    /** */
    /**
     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
     */
    private final int INITIALIZE_LENGTH = 1024;

    /** */
    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    private Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /** */
    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /** */
    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /** */
    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /** */
    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /** */
    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** */
    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** */
    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    private String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /** */
    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    private String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * java端公钥加密
     */
    public String encryptedDataOnJava(String data, String PUBLICKEY) {
        try {
            data = Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), PUBLICKEY));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    /**
     * java端私钥解密
     */
    public String decryptDataOnJava(String data, String PRIVATEKEY) {
        String temp = "";
        try {
            byte[] rs = Base64.decodeBase64(data);
            temp = new String(decryptByPrivateKey(rs, PRIVATEKEY), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 生成新的keyPair并且存入redis，有效期为15分钟
     *
     * @return
     */
    public Map<String, String> generateNewKeyPair(HttpServletRequest request) {
        //拼装加密token
        String encryptToken = request.getRemoteAddr().replace(":", ".") + "-"
                + request.getRemotePort() + "-"
                + UUID.randomUUID().toString();
        String publicKey;
        Map<String, Object> keyPair;

        //生成密钥对
        try {
            Map<String, Object> stringObjectMap = genKeyPair();
            publicKey = getPublicKey(stringObjectMap);
            String privateKey = getPrivateKey(stringObjectMap);
            keyPair = new HashMap<>(2);
            keyPair.put("publicKey", publicKey);
            keyPair.put("privateKey", privateKey);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //写入redis
        if (!CollectionUtils.isEmpty(keyPair)) {
            redisUtils.putMap("RsaTokenPair:" + encryptToken, keyPair, RedisExpireTime.MIN_15);
        } else {
            //todo 丢出全局异常
        }

        //返回公钥和token
        Map<String, String> result = new HashMap<>();
        result.put("token", encryptToken);
        result.put("publicKey", publicKey);
        return result;
    }

    /**
     * 校验token并获取解密后数据
     *
     * @param rsaToken
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public String verifyEncryptToken(String rsaToken, String encryptedData) {
        //从redis获取rsaToken对应的私钥并解密
        Map<String, String> map = redisUtils.getMap("RsaTokenPair:" + rsaToken);
        if (CollectionUtils.isEmpty(map)) {
            AuthAsserts.fail("临时加密token不存在或已过期，请重新请求加密token");
        }

        String decryptedData = decryptDataOnJava(encryptedData, map.get("privateKey"));
        return decryptedData;
    }

    public void main(String[] args) throws Exception {
//        originTestPair();
        Map<String, Object> stringObjectMap = genKeyPair();
        String privateKey = getPrivateKey(stringObjectMap);
        String publicKey = getPublicKey(stringObjectMap);
        System.out.println("==========私鑰加密公鑰解密============");
        String bytes = encryptedDataOnJava("this is a test", privateKey);
        System.out.println("加密结果：" + bytes);
        System.out.println("解密结果：" + (decryptDataOnJava(bytes, publicKey)));
        System.out.println("===================================");
        System.out.println("==========公钥加密私钥解密============");
        byte[] bytes1 = encryptByPublicKey("this is a test".getBytes(), publicKey);
        System.out.println("加密结果：" + new String(bytes1, "utf-8"));
        System.out.println("解密结果：" + new String(decryptByPrivateKey(bytes1, privateKey), "utf-8"));
        System.out.println("===================================");

    }

    private void originTestPair() throws Exception {
        Map<String, Object> stringObjectMap = genKeyPair();
        String privateKey = getPrivateKey(stringObjectMap);
        String publicKey = getPublicKey(stringObjectMap);
        System.out.println("==========私鑰加密公鑰解密============");
        byte[] bytes = encryptByPrivateKey("this is a test".getBytes(), privateKey);
        System.out.println("加密结果：" + new String(bytes, "utf-8"));
        System.out.println("解密结果：" + new String(decryptByPublicKey(bytes, publicKey), "utf-8"));
        System.out.println("===================================");
        System.out.println("==========公钥加密私钥解密============");
        byte[] bytes1 = encryptByPublicKey("this is a test".getBytes(), publicKey);
        System.out.println("加密结果：" + new String(bytes1, "utf-8"));
        System.out.println("解密结果：" + new String(decryptByPrivateKey(bytes1, privateKey), "utf-8"));
        System.out.println("===================================");
    }


}
