package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mylibrary2.R;
import com.example.mylibrary2.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameCenterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_game_center);
        ButterKnife.bind(this);

    }


    @OnClick({R2.id.ll_changePwd,R2.id.rl_back,R2.id.img_changeUser,R2.id.rl_backToGame,R2.id.ll_phone,R2.id.ll_email,
                R2.id.rl_service})
    public void onViewClicked(View v) {
        int i = v.getId();
        if (i == R.id.rl_back) {
            finish();
        } else if (i == R.id.img_changeUser) {
            Intent intent = new Intent(this, ChangeUserActivity.class);
            startActivity(intent);
        } else if (i == R.id.rl_backToGame) {
            finish();
        } else if (i == R.id.ll_phone) {
            Intent intent = new Intent(this, BindPhoneActivity.class);
            startActivity(intent);
        } else if (i == R.id.ll_changePwd){
            Intent intent = new Intent(this, ChangePwdActivity.class);
            startActivity(intent);
        } else if (i == R.id.ll_email){
            Intent intent = new Intent(this, BindEmailActivity.class);
            startActivity(intent);
        } else if (i == R.id.rl_service){
            Intent intent = new Intent(this, ServiceOnlineActivity.class);
            startActivity(intent);
        }
    }
}
