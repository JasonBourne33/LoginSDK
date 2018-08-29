package com.example.util;


import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSAUtils {

    public static String data="hello world";
    public static String modulusString="95701876885335270857822974167577168764621211406341574477817778908798408856077334510496515211568839843884498881589280440763139683446418982307428928523091367233376499779842840789220784202847513854967218444344438545354682865713417516385450114501727182277555013890267914809715178404671863643421619292274848317157";
    public static String publicExponentString="65537";
    public static String privateExponentString="15118200884902819158506511612629910252530988627643229329521452996670429328272100404155979400725883072214721713247384231857130859555987849975263007110480563992945828011871526769689381461965107692102011772019212674436519765580328720044447875477151172925640047963361834004267745612848169871802590337012858580097";
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDiewqPxXT8Ow4iIwnX5F3lZBnBBBoorDq6x6kI4PEfATj6dCNdWb7+8NaCfvXNKKN9Ab/bCeAIM3fHgVO9lp1qt4kJf0d1cA0TqsbkFZmyiEi6oyUfFnPfwiGF2JQinVMkETORWrDQTRf0q7oaFWeAE275jBvVJwUAmOeUFNswdwIDAQAB";

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        //由n和e获取公钥
        PublicKey publicKey=getPublicKey64(modulusString, publicExponentString);

        //由n和d获取私钥
        PrivateKey privateKey=getPrivateKey64(modulusString, privateExponentString);

        //公钥加密
        String encrypted=encrypt(data, publicKey);
        System.out.println("加密后："+encrypted);

//        String newKey = Base64.encode(encrypted.getBytes(),Base64.def);
        String baseStr = new String(Base64.encode(encrypted.getBytes(), Base64.DEFAULT));
        String utfStr = URLEncoder.encode(baseStr, "UTF-8");
        System.out.println("utfStr=== " + utfStr);

        String decodeKey = URLDecoder.decode(utfStr, "UTF-8");
//        String decodeBaseStr = Base64Utils.decryptBASE64(decodeKey);

        //私钥解密
//        String decrypted=decrypt(decodeBaseStr,  privateKey);
//        System.out.println("解密后："+new String(decrypted));
    }

    //将base64编码后的公钥字符串转成PublicKey实例
    public static PublicKey getPublicKey64(String modulusStr, String exponentStr) throws Exception{
        BigInteger modulus=new BigInteger(modulusStr);
        BigInteger exponent=new BigInteger(exponentStr);
        RSAPublicKeySpec publicKeySpec=new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicKeySpec);
    }

    //将base64编码后的私钥字符串转成PrivateKey实例
    public static PrivateKey getPrivateKey64(String modulusStr, String exponentStr) throws Exception{
        BigInteger modulus=new BigInteger(modulusStr);
        BigInteger exponent=new BigInteger(exponentStr);
        RSAPrivateKeySpec privateKeySpec=new RSAPrivateKeySpec(modulus, exponent);
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(privateKeySpec);
    }

    /**
     * 使用getPublicKey得到公钥,返回类型为PublicKey
     * @param base64 String to PublicKey
     * @throws Exception
     */
//    public static PublicKey getPublicKey(String key) throws Exception {
//        byte[] keyBytes;
////        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
//        keyBytes = Base64.decode(key,Base64.DEFAULT);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(keySpec);
//        return publicKey;
//    }

    public static PublicKey getPublicKey(String publicKeyBase64)
            throws InvalidKeySpecException,NoSuchAlgorithmException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(Base64.decode(publicKeyBase64,Base64.DEFAULT));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
        return publicKey;
    }


    /**
     * 转换私钥
     * @param base64 String to PrivateKey
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
//        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        keyBytes = Base64.decode(key,Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    //公钥加密，并转换成十六进制字符串打印出来
    public static String encrypt(String content, PublicKey publicKey) throws Exception{
        Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");//java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

//        int splitLength=((RSAPublicKey)publicKey).getModulus().bitLength()/8-11;
//        byte[][] arrays=splitBytes(content.getBytes(), splitLength);
//        StringBuffer sb=new StringBuffer();
//        for(byte[] array : arrays){
//            sb.append(bytesToHexString(cipher.doFinal(array)));
//        }
        byte[] data = cipherDoFinal(cipher,content.getBytes(),64);
        return Base64.encodeToString(data,Base64.DEFAULT);
    }

    private static byte[] cipherDoFinal(Cipher cipher,byte[] srcBytes,int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if(segmentSize<=0)
            throw new RuntimeException("分段大小必须大于0");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }



    //私钥解密，并转换成十六进制字符串打印出来
    public static String decrypt(String content, PrivateKey privateKey) throws Exception{
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int splitLength=((RSAPrivateKey)privateKey).getModulus().bitLength()/8;
        byte[] contentBytes=hexString2Bytes(content);
        byte[][] arrays=splitBytes(contentBytes, splitLength);
        StringBuffer sb=new StringBuffer();
        for(byte[] array : arrays){
            sb.append(new String(cipher.doFinal(array)));
        }
        return sb.toString();
    }

    //拆分byte数组
    public static byte[][] splitBytes(byte[] bytes, int splitLength){
        int x; //商，数据拆分的组数，余数不为0时+1
        int y; //余数
        y=bytes.length%splitLength;
        if(y!=0){
            x=bytes.length/splitLength+1;
        }else{
            x=bytes.length/splitLength;
        }
        byte[][] arrays=new byte[x][];
        byte[] array;
        for(int i=0; i<x; i++){

            if(i==x-1 && bytes.length%splitLength!=0){
                array=new byte[bytes.length%splitLength];
                System.arraycopy(bytes, i*splitLength, array, 0, bytes.length%splitLength);
            }else{
                array=new byte[splitLength];
                System.arraycopy(bytes, i*splitLength, array, 0, splitLength);
            }
            arrays[i]=array;
        }
        return arrays;
    }

    //byte数组转十六进制字符串
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    //十六进制字符串转byte数组
    public static byte[] hexString2Bytes(String hex) {
        int len = (hex.length() / 2);
        hex=hex.toUpperCase();
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

}
