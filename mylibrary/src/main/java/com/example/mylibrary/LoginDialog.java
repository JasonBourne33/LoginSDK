package com.example.mylibrary;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.LoginBean;
import com.example.mylibrary2.R;
import com.example.util.MD5Utils;
import com.example.util.RSAUtils;
import com.example.util.SharedPreferencesUtil;
import com.example.util.StringUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.security.SecureRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.example.util.RSAUtils.getPublicKey;


public class LoginDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TestSdk.OnLoginListener listener;

    private EditText etUser;
    private EditText etPwd;
    private ImageView imgEye;
    private ImageView imgClose;
    private Button btnEnter;
    private TextView tvOneKeyRegist;
    private TextView tvForgetPwd;

    public LoginDialog(@NonNull Context context, TestSdk.OnLoginListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    public LoginDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void setLoginState(Boolean isLogin);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login);
        etUser = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        imgEye = findViewById(R.id.img_eye);
        imgClose = findViewById(R.id.img_close);
        btnEnter = findViewById(R.id.btn_enter);
        tvForgetPwd = findViewById(R.id.tv_forgetPwd);

        tvOneKeyRegist = findViewById(R.id.tv_oneKeyRegist);
        Button btnEnter = findViewById(R.id.btn_enter);
        RelativeLayout rlRegister = findViewById(R.id.rl_register);
        btnEnter.setOnClickListener(this);
        imgEye.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        rlRegister.setOnClickListener(this);
        tvOneKeyRegist.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);

//        Log.e(TAG, "System.currentTimeMillis():=== "+System.currentTimeMillis()/1000);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_close) {
            dismiss();
        }
        if (i == R.id.tv_forgetPwd) {
            FindBackPwdDialog findBackPwdDialog = new FindBackPwdDialog(mContext);
            findBackPwdDialog.show();
        }
        if (i == R.id.tv_oneKeyRegist) {
            OneKeyRegistDialog oneKeyRegistDialog = new OneKeyRegistDialog(mContext);
            oneKeyRegistDialog.show();
        }
        if (i == R.id.rl_register) {
//            RegisterDialog registerDialog = new RegisterDialog();
//            registerDialog.show(mFragmentManager,"dialog_fragment");
//            dismiss();
            Intent intent = new Intent(mContext, RegisterDialog.class);
            mContext.startActivity(intent);
        }
        if (i == R.id.btn_enter) {
            String finalUrl = getQuestUrl(rawUrl);
            LoginRequest(finalUrl);
        }
        if (i == R.id.img_eye) {

            if (etPwd.getInputType() == 129) {
                //true ,show the pwd
                etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imgEye.setImageResource(R.drawable.game_sdk_pwd_eye_icon);
            } else {
                //hide the pwd
                etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgEye.setImageResource(R.drawable.game_sdk_hide_pwd_icon);
            }
        }
    }

    private void LoginRequest(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
//                    Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Gson gson = new Gson();
                    LoginBean bean = gson.fromJson(jsonData, LoginBean.class);
                    JSONObject jsonObject = null;

                    jsonObject = new JSONObject(jsonData);
                    Log.e(TAG, "jsonObject=== "+jsonObject );
                    int ret = jsonObject.getInt("ret");
                    if (ret == 200) {
                        Log.e(TAG, "login success=== " );
                    } else {
                        Log.e(TAG, "login fail=== " );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDiewqPxXT8Ow4iIwnX5F3lZBnB\n" +
            "BBoorDq6x6kI4PEfATj6dCNdWb7+8NaCfvXNKKN9Ab/bCeAIM3fHgVO9lp1qt4kJ\n" +
            "f0d1cA0TqsbkFZmyiEi6oyUfFnPfwiGF2JQinVMkETORWrDQTRf0q7oaFWeAE275\n" +
            "jBvVJwUAmOeUFNswdwIDAQAB";
    public String rawUrl = "http://apisdk.98gam.com/v1/public/login";
    public String method = "v1/public/login";
    private String getQuestUrl(String url) {
        long timeStamp = System.currentTimeMillis() / 1000;
        String randomString = StringUtil.generateString(new SecureRandom(), 10);
        String jsonStr = toJson();
        String dataStr = "";
        try {
            PublicKey publicKey = getPublicKey(publicKeyStr);
            String encrypted = RSAUtils.encrypt(jsonStr, publicKey);
            //String baseStr = new String(Base64.encode(encrypted.getBytes(), Base64.DEFAULT));
            dataStr = URLEncoder.encode(encrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String signature = dataStr + method + randomString + timeStamp + "testappid";
        String finalSignature = MD5Utils.crypt(MD5Utils.SHA1(signature));
        Log.e(TAG, "signature:=== " + signature);

        //不建议用+号拼接，还要 replace("%","%25")
//        String returnUrl = url + "?timestamp=" + timeStamp + "&randomstr=" + randomString +
//                "&data=" + dataStr + "&signature=" + finalSignature + "&appid=testappid";
//        returnUrl = returnUrl.replace("%","%25");

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
    private String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", "qwerty");
            jsonObj.put("password", "qwerty");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    //testLogin
    private void TestLoginRequest() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://45.78.17.33/gameserver/user/login?uid=1")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Gson gson = new Gson();
                    LoginBean bean = gson.fromJson(jsonData, LoginBean.class);
                    Log.v("TAG", "" + bean.getErroe_code());
                    JSONObject jsonObject = null;

                    jsonObject = new JSONObject(jsonData);

                    int erroe_code = jsonObject.getInt("erroe_code");
                    if (erroe_code == 1) {
                        if (listener != null) {
                            listener.onSuccess("SUCCESS");
                            SharedPreferencesUtil.putString(mContext, "userName", etUser.getText().toString());
                            SharedPreferencesUtil.putString(mContext, "userPwd", etUser.getText().toString());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailed("FAIL");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
