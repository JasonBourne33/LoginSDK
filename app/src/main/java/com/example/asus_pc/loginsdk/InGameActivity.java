package com.example.asus_pc.loginsdk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mylibrary.TestSdk;

public class InGameActivity extends AppCompatActivity {

    private static final String TAG = "e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        TestSdk.startLoginService(InGameActivity.this);
        Log.e(TAG, "onCreate: InGameActivity=== " );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("")//设置标题
                .setMessage("是否退出游戏？")//设置提示内容
                //确定按钮
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                //取消按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();
    }
}
