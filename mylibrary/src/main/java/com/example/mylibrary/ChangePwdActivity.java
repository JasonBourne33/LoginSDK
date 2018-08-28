package com.example.mylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mylibrary2.R;
import com.example.mylibrary2.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.et_oldPwd, R2.id.et_newPwd, R2.id.et_pwdAgain, R2.id.btn_save, R2.id.rl_back})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.et_oldPwd) {
        } else if (i == R.id.et_newPwd) {
        } else if (i == R.id.et_pwdAgain) {
        } else if (i == R.id.btn_save) {
        } else if (i == R.id.rl_back) {
            finish();
        }
    }
}
