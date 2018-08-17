package com.example.mylibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mylibrary2.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private TestSdk.OnLoginListener listener;

    private EditText etUser;
    private EditText etPwd;
    private ImageView imgEye;
    private ImageView imgClose;
    private Button btnEnter;

    public LoginDialog(@NonNull Context context, FragmentManager fragmentManager, TestSdk.OnLoginListener listener) {
        super(context);
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
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
        Button btnEnter = findViewById(R.id.btn_enter);
        RelativeLayout rlRegister = findViewById(R.id.rl_register);
        btnEnter.setOnClickListener(this);
        imgEye.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        rlRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_close) {
            dismiss();
        }
        if (i == R.id.rl_register) {
            RegisterDialog registerDialog = new RegisterDialog(mContext,mFragmentManager);
            registerDialog.show();
//            dismiss();
        }
        if (i == R.id.btn_enter) {
            Log.e("e", "btn enter=== ");
            LoginRequest();
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

    private void LoginRequest() {
        Log.e("e", "LoginRequest=== ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://45.78.17.33/gameserver/user/login?uid=1")
//                            .url("http://10.0.2.2/get_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    Log.e("e", "jsonData is=== " + jsonData);

                    int erroe_code = jsonObject.getInt("erroe_code");
                    Log.e("e", "erroe_code is=== " + erroe_code);
                    if (erroe_code == 1) {
                        if (listener != null) {
                            listener.onSuccess("SUCCESS");
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailed("FAIL");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
