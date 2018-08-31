package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.LoginBean;
import com.example.mylibrary.LoginDialog;
import com.example.mylibrary.RegisterDialog;
import com.example.mylibrary.TestSdk;
import com.example.mylibrary2.R;
import com.example.mylibrary2.R2;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.example.util.RSAUtils.getPublicKey;

@SuppressLint("ValidFragment")
public class RegistByAccountFragment extends Fragment implements View.OnClickListener{

    private static final int USER_EXIST = 1;
    private static final int REGIST_SUCCESS  = 2;
    private Context mContext;
    private TestSdk.OnLoginListener listener;
    private String strAccount;
    private String strPwd;
    private String strPwdAgain;

    private EditText etAccount;
    private EditText etPwd;
    private EditText etPwdAgain;

    @SuppressLint("ValidFragment")
    public RegistByAccountFragment(Context context) {
        this.mContext = context;
        listener = LoginDialog.getListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registbyaccount, container, false);
        Button btnEnter = rootView.findViewById(R.id.btn_enter);
        etAccount = rootView.findViewById(R.id.et_account);
        etPwd = rootView.findViewById(R.id.et_pwd);
        etPwdAgain = rootView.findViewById(R.id.et_pwdAgain);
        TextView tvBackToLogin = rootView.findViewById(R.id.tv_backToLogin);
        ImageView imgEnter = rootView.findViewById(R.id.img_enter);

        tvBackToLogin.setOnClickListener(this);
        imgEnter.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        return rootView;
    }


    private String message;

    private void regist() {
        strAccount = etAccount.getText().toString();
        strPwd = etPwd.getText().toString();
        strPwdAgain = etPwdAgain.getText().toString();
        Log.e(TAG, "strAccount:=== "+strAccount );
        if(strAccount.equals("")){
            Toast.makeText(mContext,"请输入用户名",Toast.LENGTH_SHORT).show();
        }else if(strPwd.equals("")){
            Toast.makeText(mContext,"请输入密码",Toast.LENGTH_SHORT).show();
        }else if(strPwd.length()<6){
            Toast.makeText(mContext,"密码必须大于6位",Toast.LENGTH_SHORT).show();
        }else if(strPwdAgain.equals(strPwd)){
            String strJson = toJson();
            String finalUrl = UrlUtil.getQuestUrl(rawUrl,strJson);
            registRequest(finalUrl);
        }else {
            Toast.makeText(mContext,"密码不一致",Toast.LENGTH_SHORT).show();
        }
    }

    public String rawUrl = "http://apisdk.98gam.com/v1/public/register";
    public String method = "v1/public/register";
    private String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", strAccount);
            jsonObj.put("password", strPwd);
            jsonObj.put("scenario", "registerbyusername");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USER_EXIST:
                    Toast.makeText(mContext, "账号已被注册", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
                case REGIST_SUCCESS:
                    if (listener != null) {
                        Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.putString(mContext, "userName", etAccount.getText().toString());
                        SharedPreferencesUtil.putString(mContext, "userPwd", etPwd.getText().toString());
                        listener.onSuccess("success");
                        getActivity().finish();
                    }else {
                        Log.e(TAG, "listener:=== "+listener );
                    }
                    break;
            }
        }
    };
    private void registRequest(String url){
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
                        Message message = new Message();
                        message.what = REGIST_SUCCESS;
                        handler.sendMessage(message);
                    } else if(ret == 10001){
                        Message message = new Message();
                        message.what = USER_EXIST;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.et_account) {
        } else if (i == R.id.et_pwd) {
        } else if (i == R.id.et_sms) {
        } else if (i == R.id.tv_backToLogin) {
            getActivity().finish();
        } else if (i == R.id.btn_enter) {
            regist();
        }
    }

    //    private void regist() {
//        OkHttpClient client = new OkHttpClient();
////        RequestBody body = RequestBody.create(user)
//        RequestBody body = new FormBody.Builder()
//                .add("username", "test")
//                .add("password", "test")
//                .build();
//        Request request = new Request.Builder()
//                .post(body)
//                .url("http://45.78.17.33/gameserver/user/register")
//                .build();
////                    Response response = client.newCall(request).execute();
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
//                    Log.e("TAG", "bean.getErroe_code()=== " + bean.getErroe_code());
//
//                    message = bean.getMessage().toString();
//                    JSONObject jsonObject = null;
//                    jsonObject = new JSONObject(jsonData);
//                    int erroe_code = jsonObject.getInt("erroe_code");
//                    if (erroe_code == 1) {
//
//                    } else {
//                        Message message = new Message();
//                        message.what = USER_EXIST;
//                        handler.sendMessage(message);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
