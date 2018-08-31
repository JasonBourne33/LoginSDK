package com.example.mylibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.LoginBean;
import com.example.mylibrary2.R;
import com.example.mylibrary2.R2;
import com.example.util.SharedPreferencesUtil;
import com.example.util.UrlUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class BindEmailActivity extends AppCompatActivity {

    @BindView(R2.id.img_back)
    ImageView imgBack;
    @BindView(R2.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R2.id.img_phone)
    ImageView imgPhone;
    @BindView(R2.id.et_enterPhone)
    EditText etEnterPhone;
    @BindView(R2.id.tv_sms)
    TextView tvSms;
    @BindView(R2.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R2.id.img_sms)
    ImageView imgSms;
    @BindView(R2.id.et_sms)
    EditText etSms;
    @BindView(R2.id.rl_verify)
    RelativeLayout rlVerify;
    @BindView(R2.id.btn_save)
    Button btnSave;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);
        ButterKnife.bind(this);
        this.mContext = this;
    }

    @OnClick({R2.id.rl_back, R2.id.tv_sms, R2.id.btn_save})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.rl_back) {
            finish();
        } else if (v.getId() == R.id.tv_sms) {
            if (etEnterPhone.getText().toString().equals("")) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            } else {
                String strJson = getVerifytoJson();
                Log.e(TAG, "onViewClicked: strJson=== " + strJson);
                String finalUrl = UrlUtil.getQuestUrl(getVerifyUrl, strJson);
                getVerifyRequest(finalUrl);
            }
        } else if (v.getId() == R.id.btn_save) {
            if (etEnterPhone.getText().toString().equals("")) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            } else if (etSms.getText().toString().equals("")) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            } else {
                String strJson = bindEmailToJson();
                Log.e(TAG, "onViewClicked: strJson=== " + strJson);
                String finalUrl = UrlUtil.getQuestUrl(bindEmailUrl, strJson);
                bindEmailRequest(finalUrl);
            }
        }
    }

    public String getVerifyUrl = "http://apisdk.98gam.com/v1/public/get-verify";

    private String getVerifytoJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("account", etEnterPhone.getText().toString());
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
                        Message msg = new Message();
                        msg.what = GET_VERIFY_SUCCESS;
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


    public String bindEmailUrl = "http://apisdk.98gam.com/v1/member/bind-info";

    private String bindEmailToJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("email", etEnterPhone.getText().toString());
            jsonObj.put("code", etSms.getText().toString());
            jsonObj.put("scenario", "bindemail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    private void bindEmailRequest(String url) {
        String accessToken = SharedPreferencesUtil.getString(mContext, "access_token");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
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
//                    Gson gson = new Gson();
//                    LoginBean bean = gson.fromJson(jsonData, LoginBean.class);
                    JSONObject jsonObject = null;

                    Log.e(TAG, "bindEmailRequest jsonData=== " + jsonData);
                    jsonObject = new JSONObject(jsonData);
                    Log.e(TAG, "jsonObject=== " + jsonObject);
                    int ret = jsonObject.getInt("ret");
                    if (ret == 200) {
                        Message msg = new Message();
                        msg.what = REGIST_SUCCESS;
                        handler.sendMessage(msg);
                    } else if (ret == 10001) {
                        Message msg = new Message();
                        msg.what = REGIST_FAIL;
                        handler.sendMessage(msg);
                    } else if (ret == 10004) {
                        Message msg = new Message();
                        msg.what = NO_EMAIL_ADDRESS;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final int GET_VERIFY_SUCCESS = 1;
    public static final int REGIST_FAIL = 2;
    public static final int REGIST_SUCCESS = 3;
    public static final int NO_EMAIL_ADDRESS = 4;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_VERIFY_SUCCESS:
                    Toast.makeText(mContext, "获取成功", Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_FAIL:
                    Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_SUCCESS:
                    Toast.makeText(mContext, "绑定成功", Toast.LENGTH_SHORT).show();
                    finish();
                case NO_EMAIL_ADDRESS:
                    Toast.makeText(mContext, "邮箱地址不合法", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
