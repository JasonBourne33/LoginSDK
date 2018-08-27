package com.example.mylibrary;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mylibrary2.R;

public class GameCenterActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout rlBack;
    private ImageView imgChangeUser;
    private RelativeLayout rlService;
    private RelativeLayout rlBackToGame;
    private LinearLayout llPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_game_center);
        rlBack = findViewById(R.id.rl_back);
        imgChangeUser = findViewById(R.id.img_changeUser);
        rlService = findViewById(R.id.rl_service);
        rlBackToGame = findViewById(R.id.rl_backToGame);
        llPhone = findViewById(R.id.ll_phone);
        rlBack.setOnClickListener(this);
        imgChangeUser.setOnClickListener(this);
        rlService.setOnClickListener(this);
        rlBackToGame.setOnClickListener(this);
        llPhone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_back) {
            finish();
        } else if (i == R.id.img_changeUser) {
            Intent intent = new Intent(this,ChangeUserActivity.class);
            startActivity(intent);
        }else if (i == R.id.rl_backToGame) {
            finish();
        }else if (i == R.id.ll_phone) {
            Intent intent = new Intent(this,BindPhoneActivity.class);
            startActivity(intent);
        }
    }
}
