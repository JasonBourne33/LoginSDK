package com.example.mylibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.example.util.SharedPreferencesUtil;
import com.example.util.StringUtil;
import com.example.util.UrlUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.example.mylibrary.TestSdk.mOnLoginListener;

public class OneKeyRegistDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private EditText etPwd;
    private EditText etAccount;
    private ImageView imgEye;
    private TestSdk.OnLoginListener listener;

    public OneKeyRegistDialog(@NonNull Context context, TestSdk.OnLoginListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_onekey_regist);
        TextView tvBackToLogin = findViewById(R.id.tv_backToLogin);
        ImageView imgClose = findViewById(R.id.img_close);

        imgEye = findViewById(R.id.img_eye);

        etAccount = findViewById(R.id.et_account);
        Button btnEnter = findViewById(R.id.btn_enter);

        etPwd = findViewById(R.id.et_pwd);
        etAccount.setText("980x_" + StringUtil.generateNumber(new SecureRandom(), 6));

        tvBackToLogin.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        imgEye.setOnClickListener(this);
        etAccount.setKeyListener(null);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_backToLogin) {
            LoginDialog loginDialog = new LoginDialog(mContext, mOnLoginListener);
            loginDialog.show();
            dismiss();
        } else if (i == R.id.img_close) {
            LoginDialog loginDialog = new LoginDialog(mContext, mOnLoginListener);
            loginDialog.show();
            dismiss();
        } else if (i == R.id.btn_enter) {
            String strPwd = etPwd.getText().toString();
            if (strPwd.equals("")) {
                Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
            } else {
                String strJson = toJson();
                String finalUrl = UrlUtil.getQuestUrl(rawUrl, strJson);
                OnKeyRegistRequest(finalUrl);
            }
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

    public String rawUrl = "http://apisdk.98gam.com/v1/public/register";
    public String method = "v1/public/register";

    private String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", etAccount.getText().toString());
            jsonObj.put("password", etPwd.getText().toString());
            jsonObj.put("scenario", "registerbyusername");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    private void OnKeyRegistRequest(String url) {
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
                        Log.e(TAG, "login success=== ");
                        Message msg = new Message();
                        msg.what = REGIST_SUCCESS;
                        handler.sendMessage(msg);
                    } else if (ret == 10001) {
                        Message msg = new Message();
                        msg.what = REGIST_FAIL;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final int REGIST_SUCCESS = 1;
    public static final int REGIST_FAIL = 2;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGIST_SUCCESS:
                    if (listener != null) {
                        Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.putString(mContext, "userName", etAccount.getText().toString());
                        SharedPreferencesUtil.putString(mContext, "userPwd", etPwd.getText().toString());
                        listener.onSuccess("success");
                        dismiss();
                    }
                    break;
                case REGIST_FAIL:
                    Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

//    public String loginUrl = "http://apisdk.98gam.com/v1/public/login";
//    public String longinMethod = "v1/public/login";
}
