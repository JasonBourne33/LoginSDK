package com.example.mylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary2.R;
import com.example.mylibrary2.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);
        ButterKnife.bind(this);

    }

    @OnClick({R2.id.rl_back})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.rl_back) {
            finish();
        }
    }

}
