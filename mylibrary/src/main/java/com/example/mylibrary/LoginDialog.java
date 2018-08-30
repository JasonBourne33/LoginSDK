package com.example.mylibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.util.UrlUtil;
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
    private String strUser;
    private String strPwd;

    public LoginDialog(@NonNull Context context, TestSdk.OnLoginListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    private static LoginDialog instance = null;

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
        this.setCanceledOnTouchOutside(false);
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

        etUser.setText(SharedPreferencesUtil.getString(mContext,"userName"));
        etPwd.setText(SharedPreferencesUtil.getString(mContext,"userPwd"));
        Log.e(TAG, "onCreate: LoginDialog=== " );
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
            dismiss();
        }
        if (i == R.id.tv_oneKeyRegist) {
            OneKeyRegistDialog oneKeyRegistDialog = new OneKeyRegistDialog(mContext);
            oneKeyRegistDialog.show();
            dismiss();
        }
        if (i == R.id.rl_register) {
//            RegisterDialog registerDialog = new RegisterDialog();
//            registerDialog.show(mFragmentManager,"dialog_fragment");
//            dismiss();
            Intent intent = new Intent(mContext, RegisterDialog.class);
            mContext.startActivity(intent);
            dismiss();
        }
        if (i == R.id.btn_enter) {
            strUser = etUser.getText().toString();
            strPwd = etPwd.getText().toString();
            if(strUser.equals("") || strPwd.equals("")){
                Toast.makeText(mContext,"请输入用户名，手机号，邮箱",Toast.LENGTH_SHORT).show();
            }else{
                String strJson = toJson();
                String finalUrl = UrlUtil.getQuestUrl(rawUrl,strJson);
                LoginRequest(finalUrl);
            }

        }
        if (i == R.id.img_eye) {
            eyeImageClick();
        }
    }
    private void eyeImageClick(){
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

    public String rawUrl = "http://apisdk.98gam.com/v1/public/login";
    public String method = "v1/public/login";
    private String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", etUser.getText().toString());
            jsonObj.put("password", etPwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
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
                        if (listener != null) {
                            listener.onSuccess("SUCCESS");
                            SharedPreferencesUtil.putString(mContext, "userName", etUser.getText().toString());
                            SharedPreferencesUtil.putString(mContext, "userPwd", etPwd.getText().toString());
                        }
                        Log.e(TAG, "login success=== " );

                    } else if(ret == 10001){
                        if (listener != null) {
                            listener.onFailed("FAIL");
                        }
                        Message msg = new Message();
                        msg.what = PWD_ERROR;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final int PWD_ERROR = 1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case PWD_ERROR:
                    Toast.makeText(mContext,"账号和密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                    etPwd.setText("");
                    break;
            }
        }
    };

    //testLogin
//    private void TestLoginRequest() {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://45.78.17.33/gameserver/user/login?uid=1")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String jsonData = response.body().string();
//                    Gson gson = new Gson();
//                    LoginBean bean = gson.fromJson(jsonData, LoginBean.class);
//                    Log.v("TAG", "" + bean.getErroe_code());
//                    JSONObject jsonObject = null;
//
//                    jsonObject = new JSONObject(jsonData);
//
//                    int erroe_code = jsonObject.getInt("erroe_code");
//                    if (erroe_code == 1) {
//                        if (listener != null) {
//                            listener.onSuccess("SUCCESS");
//                            SharedPreferencesUtil.putString(mContext, "userName", etUser.getText().toString());
//                            SharedPreferencesUtil.putString(mContext, "userPwd", etUser.getText().toString());
//                        }
//                    } else {
//                        if (listener != null) {
//                            listener.onFailed("FAIL");
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }


}
