package com.example.mylibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mylibrary2.R;

public class FindBackPwdDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    public FindBackPwdDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_findbackpwd);

        ImageView imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_close) {
            dismiss();
        }
    }
}
