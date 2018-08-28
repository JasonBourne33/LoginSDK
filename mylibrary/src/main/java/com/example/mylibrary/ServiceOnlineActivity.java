package com.example.mylibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary2.R;
import com.example.mylibrary2.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceOnlineActivity extends AppCompatActivity {

    @BindView(R2.id.img_back)
    ImageView imgBack;
    @BindView(R2.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R2.id.img_qq)
    ImageView imgQq;
    @BindView(R2.id.tv_notResponse)
    TextView tvNotResponse;
    @BindView(R2.id.tv_couldCall)
    TextView tvCouldCall;
    @BindView(R2.id.tv_phoneNum)
    TextView tvPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_online);
        ButterKnife.bind(this);

    }

    @OnClick({R2.id.rl_back, R2.id.img_qq, R2.id.tv_phoneNum})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.rl_back) {
            finish();
        } else if (i == R.id.img_qq) {
            Toast.makeText(this,"您未安装QQ",Toast.LENGTH_SHORT).show();
        } else if (i == R.id.tv_phoneNum) {
            String phoneNum = tvPhoneNum.getText().toString();
            diallPhone(phoneNum);
        }
    }

    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
