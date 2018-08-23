package com.example.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mylibrary2.R;

/**
 * Created by ZY on 2016/8/10.
 * 底部菜单栏
 */
public class ButtonMenu extends LinearLayout implements View.OnClickListener{
    private Context mContext;
    private LinearLayout layout;
    private RelativeLayout llSelfCenter;
    private RelativeLayout llRecorder;


    private TranslateAnimation animation;

    public ButtonMenu(final Context context) {
        super(context);
        this.mContext = context;
        View root = View.inflate(context, R.layout.button_menu, null);
        layout = (LinearLayout) root.findViewById(R.id.ll_menu);
        llSelfCenter = (RelativeLayout) root.findViewById(R.id.ll_selfCenter);
        llRecorder = (RelativeLayout) root.findViewById(R.id.ll_recorder);
//        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 1.0f,
//                Animation.RELATIVE_TO_SELF, 0);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        layout.setAnimation(animation);
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                ViewManager manager = ViewManager.getInstance(context);
//                manager.showFloatBall();
//                manager.hideFloatMenu();
                return false;
            }
        });
        llSelfCenter.setOnClickListener(this);
        llRecorder.setOnClickListener(this);
        addView(root);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_selfCenter) {
            Toast.makeText(mContext, "个人中心", Toast.LENGTH_SHORT).show();

        } else if (i == R.id.ll_recorder) {
            Toast.makeText(mContext, "消费记录", Toast.LENGTH_SHORT).show();

        }
    }

//    public void startAnimation() {
//        animation.start();
//    }
}
