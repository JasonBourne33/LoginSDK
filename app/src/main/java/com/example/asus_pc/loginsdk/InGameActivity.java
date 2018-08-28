package com.example.asus_pc.loginsdk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.manager.ViewManager;
import com.example.mylibrary.TestSdk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InGameActivity extends AppCompatActivity {

    private static final String TAG = "e";
    @BindView(R.id.tv_userInfo)
    TextView tvUserInfo;
    @BindView(R.id.btn_unLogin)
    Button btnUnLogin;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.btn_uploadInfo)
    Button btnUploadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_in_game);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TestSdk.startLoginService(InGameActivity.this);
        ViewManager manager = ViewManager.getInstance(this);
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

    @OnClick({R.id.btn_unLogin, R.id.btn_pay, R.id.btn_uploadInfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_unLogin:
                break;
            case R.id.btn_pay:
                break;
            case R.id.btn_uploadInfo:
                break;
        }
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
