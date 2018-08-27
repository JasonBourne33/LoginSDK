package com.example.asus_pc.loginsdk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewManager;
import android.view.Window;

import com.example.mylibrary.TestSdk;

public class InGameActivity extends AppCompatActivity {

    private static final String TAG = "e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_in_game);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TestSdk.startLoginService(InGameActivity.this);
        com.example.manager.ViewManager manager = com.example.manager.ViewManager.getInstance(this);
        manager.showFloatBall();
        manager.showButtonMenu();
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否退出游戏")
                    .setCancelable(false)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        if(keyCode==KeyEvent.KEYCODE_BACK){
//            moveTaskToBack(true);
//            return true;//不执行父类点击事件
//        }
//        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Dialog dialog = new AlertDialog.Builder(this)
//                .setTitle("")//设置标题
//                .setMessage("是否退出游戏？")//设置提示内容
//                //确定按钮
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                //取消按钮
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).create();
//        dialog.show();
//    }

}
