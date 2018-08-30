package com.example.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.example.mylibrary2.R;
import com.example.util.UrlUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.example.mylibrary.TestSdk.mOnLoginListener;

@SuppressLint("ValidFragment")
public class RegistByPhoneFragment extends Fragment implements View.OnClickListener {

    private EditText etAccount;
    private Context mContext;
    private ImageView imgEye;

    @SuppressLint("ValidFragment")
    public RegistByPhoneFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registbyphone, container, false);
        TextView tvSms = rootView.findViewById(R.id.tv_sms);
        TextView tvBackToLogin = rootView.findViewById(R.id.tv_backToLogin);
        Button btnEnter = rootView.findViewById(R.id.btn_enter);
        imgEye = rootView.findViewById(R.id.img_eye);
        etSms = rootView.findViewById(R.id.et_sms);
        etPwd = rootView.findViewById(R.id.et_pwd);
        etAccount = rootView.findViewById(R.id.et_account);
        tvSms.setOnClickListener(this);
        tvBackToLogin.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        imgEye.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_sms) {
            String strAccount = etAccount.getText().toString();
            if (strAccount.equals("")) {
                Toast.makeText(mContext, "请输入手机号/邮箱", Toast.LENGTH_SHORT).show();
            } else {
                String strJson = getVerifyToJson();
                String finalUrl = UrlUtil.getQuestUrl(rawUrl, strJson);
                getVerifyRequest(finalUrl);
            }
        } else if (i == R.id.tv_backToLogin) {
            getActivity().finish();
        } else if (i == R.id.btn_enter) {
            regist();
        } else if (i == R.id.img_eye) {
            eyeImageClick();
        }
    }

    private void eyeImageClick() {
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

    private String strAccount;
    private String strPwd;
    private String strVerify;
    private EditText etPwd;
    private EditText etSms;

    private void regist() {
            strAccount = etAccount.getText().toString();
        strPwd = etPwd.getText().toString();
        strVerify = etSms.getText().toString();
        Log.e(TAG, "strAccount:=== " + strAccount);
        if (strAccount.equals("")) {
            Toast.makeText(mContext, "请输入手机号/邮箱", Toast.LENGTH_SHORT).show();
        } else if (strPwd.equals("")) {
            Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
        } else if (strPwd.length() < 6) {
            Toast.makeText(mContext, "密码必须大于6位", Toast.LENGTH_SHORT).show();
        } else if (strVerify.equals("")) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
        } else {
            String strJson = registToJson();
            String finalUrl = UrlUtil.getQuestUrl(registUrl, strJson);
            registRequest(finalUrl);
        }
    }
    private String registUrl = "http://apisdk.98gam.com/v1/public/register";
    private String registMethod = "v1/public/register";

    private String registToJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("tel", strAccount);
            jsonObj.put("password", strPwd);
            jsonObj.put("code", strVerify);
            jsonObj.put("scenario", "registerbytel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    private void registRequest(String url) {
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
                    Log.e(TAG, "jsonObject=== " + jsonObject);
                    int ret = jsonObject.getInt("ret");
                    if (ret == 200) {
                        Message message = new Message();
                        message.what = REGIST_SUCCESS;
                        handler.sendMessage(message);
                    } else if (ret == 10001) {
//                        Message message = new Message();
//                        message.what = USER_EXIST;
//                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String rawUrl = "http://apisdk.98gam.com/v1/public/get-verify";
    private String method = "v1/public/get-verify";

    private String getVerifyToJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("account", etAccount.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    private void getVerifyRequest(String url) {
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
                    Log.e(TAG, "jsonObject=== " + jsonObject);
                    int ret = jsonObject.getInt("ret");
                    if (ret == 200) {
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        String verifyCode = dataObj.getString("verifyCode");
                        Log.e(TAG, "success verifyCode=== " + verifyCode);
                        Message msg = new Message();
                        msg.what = GET_VERIFY_SUCCESS;
                        handler.sendMessage(msg);
                    } else if (ret == 10031) {
                        Message msg = new Message();
                        msg.what = WRONG_EMAIL;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final int GET_VERIFY_SUCCESS = 1;
    public static final int WRONG_EMAIL = 2;
    public static final int REGIST_SUCCESS = 3;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_VERIFY_SUCCESS:
                    Toast.makeText(mContext, "获取成功", Toast.LENGTH_SHORT).show();
                    break;
                case WRONG_EMAIL:
                    Toast.makeText(mContext, "不合法手机号/邮箱", Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_SUCCESS:
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
//                    LoginDialog loginDialog = new LoginDialog(mContext, mOnLoginListener);
//                    loginDialog.show();
//                    getActivity().finish();
                    break;
            }
        }
    };
}
