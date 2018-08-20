package com.example.mylibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary2.R;
import com.example.util.SharedPreferencesUtil;

public class OneKeyRegistDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    public OneKeyRegistDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_onekey_regist);
        TextView tvBackToLogin = findViewById(R.id.tv_backToLogin);
        ImageView imgClose = findViewById(R.id.img_close);
        EditText etAccount = findViewById(R.id.et_account);
        etAccount.setText(SharedPreferencesUtil.getString(mContext,"userName"));
        tvBackToLogin.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        etAccount.setKeyListener(null);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_backToLogin) {
            dismiss();
        }
        if (i == R.id.img_close) {
            dismiss();
        }
    }
}
