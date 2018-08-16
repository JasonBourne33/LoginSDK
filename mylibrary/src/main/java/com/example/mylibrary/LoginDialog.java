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


public class LoginDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private FragmentManager mFragmentManager;

    private EditText etUser;
    private EditText etPwd;
    private ImageView imgEye;
    private ImageView imgClose;
    private Button btnEnter;
    public LoginDialog(@NonNull Context context, FragmentManager fragmentManager) {
        super(context);
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
    }

    public LoginDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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
            String strUser = etUser.getText().toString();
            String strPwd = etPwd.getText().toString();
            if("".equals(strUser) || strUser==null ){
                Toast.makeText(mContext,"请输入用户名/手机/邮箱",Toast.LENGTH_SHORT).show();
            }
            if( "".equals(strPwd) || strPwd==null ){
                Toast.makeText(mContext,"请输入密码",Toast.LENGTH_SHORT).show();
            }
            if(strUser.equals("abc") && strPwd.equals("123")){
                Toast.makeText(mContext,"Login success",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"Login fail",Toast.LENGTH_SHORT).show();
            }
        }
        if (i == R.id.img_eye) {

            if(etPwd.getInputType() == 129){
                //true ,show the pwd
                etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imgEye.setImageResource(R.drawable.game_sdk_pwd_eye_icon);
                Toast.makeText(mContext,"true etPwd.getInputType()=== "
                        +etPwd.getInputType(),Toast.LENGTH_LONG).show();
            }else {
                //hide the pwd
                etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgEye.setImageResource(R.drawable.game_sdk_hide_pwd_icon);
                Toast.makeText(mContext,"false etPwd.getInputType()=== "
                        +etPwd.getInputType(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
