package com.example.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.PublicKey;
import java.security.SecureRandom;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static android.content.ContentValues.TAG;
import static com.example.util.RSAUtils.getPublicKey;

public class UrlUtil {
//    http://apisdk.98gam.com/v1/public/login?
// timestamp=1534990347&
// randomstr=McT4CaEA4eLqLn9sFi&
// data=H28DZaOBv8A%252FuelYHgRy56g8e7N05P22wmlUj2cRZK4zaL896LvXswFLbqTZU0FnrOXfbHVK%252B1pQ%250AeprLib0VndiV%252Bw7AkqAwtCgCX%252Bt14N4PW15UySWRTGY1cDaV%252B6wzPcqUR7QMs2L4i7YdY7qxHcXH%250AM4eX9YwBg23PKTsjvwc%253D%250A&
// signature=B6065B88149091FBECF496B475A032CC&appid=testappid
//    timestamp:10位时间搓
//    random:18位 随机String

    public static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDiewqPxXT8Ow4iIwnX5F3lZBnB\n" +
            "BBoorDq6x6kI4PEfATj6dCNdWb7+8NaCfvXNKKN9Ab/bCeAIM3fHgVO9lp1qt4kJ\n" +
            "f0d1cA0TqsbkFZmyiEi6oyUfFnPfwiGF2JQinVMkETORWrDQTRf0q7oaFWeAE275\n" +
            "jBvVJwUAmOeUFNswdwIDAQAB";

    public static String getQuestUrl(String url,String jsonStr) {
        long timeStamp = System.currentTimeMillis() / 1000;
        String method = url.substring(24,url.length());
        Log.e(TAG, "method=== "+method );
        String randomString = StringUtil.generateString(new SecureRandom(), 10);
//        String jsonStr = toJson();
//        Log.e(TAG, "getQuestUrl: jsonStr=== "+jsonStr );
        String dataStr = "";
        try {
            PublicKey publicKey = getPublicKey(publicKeyStr);
            String encrypted = RSAUtils.encrypt(jsonStr, publicKey);
            dataStr = URLEncoder.encode(encrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String signature = dataStr + method + randomString + timeStamp + "testappid";
        String finalSignature = MD5Utils.crypt(MD5Utils.SHA1(signature));
//        Log.e(TAG, "signature:=== " + signature);

        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("timestamp", String.valueOf(timeStamp));
        urlBuilder.addQueryParameter("randomstr", randomString);
        urlBuilder.addQueryParameter("data", dataStr);
        urlBuilder.addQueryParameter("signature", finalSignature);
        urlBuilder.addQueryParameter("appid", "testappid");
        String builderUrl = urlBuilder.build().toString();
        reqBuild.url(urlBuilder.build());
        Log.e(TAG, "builder=== "+builderUrl );
        return builderUrl;
    }

    

}
