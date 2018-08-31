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
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class ChangePwdActivity extends AppCompatActivity {

    @BindView(R2.id.img_back)
    ImageView imgBack;
    @BindView(R2.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R2.id.img_sms)
    ImageView imgSms;
    @BindView(R2.id.et_oldPwd)
    EditText etOldPwd;
    @BindView(R2.id.rl_oldPwd)
    RelativeLayout rlOldPwd;
    @BindView(R2.id.img_newPwd)
    ImageView imgNewPwd;
    @BindView(R2.id.et_newPwd)
    EditText etNewPwd;
    @BindView(R2.id.rl_newPwd)
    RelativeLayout rlNewPwd;
    @BindView(R2.id.img_pwdAgain)
    ImageView imgPwdAgain;
    @BindView(R2.id.et_pwdAgain)
    EditText etPwdAgain;
    @BindView(R2.id.rl_pwdAgain)
    RelativeLayout rlPwdAgain;
    @BindView(R2.id.btn_save)
    Button btnSave;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R2.id.et_oldPwd, R2.id.et_newPwd, R2.id.et_pwdAgain, R2.id.btn_save, R2.id.rl_back})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.et_oldPwd) {
        } else if (i == R.id.et_newPwd) {
        } else if (i == R.id.et_pwdAgain) {
        } else if (i == R.id.btn_save) {

            if (etOldPwd.getText().toString().equals("")) {
                Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
            } else if (etNewPwd.getText().toString().equals("")) {
                Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            } else if (etPwdAgain.getText().toString().equals("")) {
                Toast.makeText(this, "请输入验证密码", Toast.LENGTH_SHORT).show();
            } else {
                String strJson = toJson();
                Log.e(TAG, "onViewClicked: strJson=== "+strJson );
                String finalUrl = UrlUtil.getQuestUrl(rawUrl, strJson);
                OnKeyRegistRequest(finalUrl);
            }
        } else if (i == R.id.rl_back) {
            finish();
        }
    }

    public String rawUrl = "http://apisdk.98gam.com/v1/member/change-password";
    private String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("oldpassword", etOldPwd.getText().toString());
            jsonObj.put("newpassword", etNewPwd.getText().toString());
            jsonObj.put("repassword", etPwdAgain.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    private void OnKeyRegistRequest(String url) {
        String accessToken = SharedPreferencesUtil.getString(mContext,"access_token");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization","Bearer "+accessToken)
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
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.putString(mContext, "userPwd", etNewPwd.getText().toString());
                    finish();
                    break;
                case REGIST_FAIL:
                    Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
