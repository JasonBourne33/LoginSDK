package com.example.mylibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import static com.example.mylibrary.TestSdk.mOnLoginListener;

public class FindBackPwdDialog extends Dialog {
    @BindView(R2.id.img_icon)
    ImageView imgIcon;
    @BindView(R2.id.view_center)
    View viewCenter;
    @BindView(R2.id.tv_findBackPwd)
    TextView tvFindBackPwd;
    @BindView(R2.id.img_close)
    ImageView imgClose;
    @BindView(R2.id.ll_head)
    RelativeLayout llHead;
    @BindView(R2.id.img_user)
    ImageView imgUser;
    @BindView(R2.id.et_account)
    EditText etAccount;
    @BindView(R2.id.img_verify)
    ImageView imgVerify;
    @BindView(R2.id.et_verify)
    EditText etVerify;
    @BindView(R2.id.tv_sms)
    TextView tvSms;
    @BindView(R2.id.img_pwd)
    ImageView imgPwd;
    @BindView(R2.id.et_pwd)
    EditText etPwd;
    @BindView(R2.id.img_eye)
    ImageView imgEye;
    @BindView(R2.id.btn_enter)
    Button btnEnter;
    private Context mContext;

    public FindBackPwdDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_findbackpwd);
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.img_close, R2.id.img_eye, R2.id.btn_enter, R2.id.tv_sms})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.img_close) {
            LoginDialog loginDialog = new LoginDialog(mContext, mOnLoginListener);
            loginDialog.show();
            dismiss();
        } else if (i == R.id.img_eye) {
            eyeImgClick();
        } else if (i == R.id.btn_enter) {
            findBack();
        } else if (i == R.id.tv_sms) {
            String strAccount=etAccount.getText().toString();
            Log.e(TAG, "strAccount=== "+strAccount );
            if(strAccount.equals("")){
                Toast.makeText(mContext,"请输入手机号/邮箱",Toast.LENGTH_SHORT).show();
            }else{
                String strJson = getVerifyToJson();
                Log.e(TAG, "strJson:=== "+strJson );
                String finalUrl = UrlUtil.getQuestUrl(rawUrl,strJson);
                getVerifyRequest(finalUrl);
            }
        }
    }

    private String strAccount;
    private String strPwd;
    private String strVerify;
    private void findBack() {
        strAccount = etAccount.getText().toString();
        strPwd = etPwd.getText().toString();
        strVerify = etVerify.getText().toString();
        if (strAccount.equals("")) {
            Toast.makeText(mContext, "请输入手机号/邮箱", Toast.LENGTH_SHORT).show();
        } else if (strPwd.equals("")) {
            Toast.makeText(mContext, "请输新入密码", Toast.LENGTH_SHORT).show();
        } else if (strPwd.length() < 6) {
            Toast.makeText(mContext, "密码必须大于6位", Toast.LENGTH_SHORT).show();
        } else if (strVerify.equals("")) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
        } else {
            String strJson = findBackToJson();
            String finalUrl = UrlUtil.getQuestUrl(registUrl, strJson);
            findBackRequest(finalUrl);
        }
    }
    private String registUrl = "http://apisdk.98gam.com/v1/public/forgot-password";
    private String registMethod = "v1/public/forgot-password";

    private String findBackToJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("account", strAccount);
            jsonObj.put("verify_code", strVerify);
            jsonObj.put("password", strPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }
    private void findBackRequest(String url) {
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

    public String rawUrl = "http://apisdk.98gam.com/v1/public/get-verify";
    public String method = "v1/public/get-verify";
    private String getVerifyToJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("account", etAccount.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }
    private void getVerifyRequest(String url){
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
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        String verifyCode = dataObj.getString("verifyCode");
                        Log.e(TAG, "success verifyCode=== "+verifyCode );
                        Message msg = new Message();
                        msg.what = GET_VERIFY_SUCCESS;
                        handler.sendMessage(msg);
                    } else if(ret == 10031){
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
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_VERIFY_SUCCESS:
                    Toast.makeText(mContext,"获取成功",Toast.LENGTH_SHORT).show();
                    break;
                case WRONG_EMAIL:
                    Toast.makeText(mContext,"不合法手机号/邮箱",Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_SUCCESS:
                    Toast.makeText(mContext,"修改成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void eyeImgClick() {
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
