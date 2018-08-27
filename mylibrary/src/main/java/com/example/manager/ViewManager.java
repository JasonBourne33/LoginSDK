package com.example.manager;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.view.ButtonMenu;
import com.example.view.FloatBall;
import com.example.view.FloatMenu;

import java.lang.reflect.Field;

import static android.content.ContentValues.TAG;

/**
 * 管理者，单例模式
 */
public class ViewManager {

    private FloatBall floatBall;

    private FloatMenu floatMenu;
    private ButtonMenu buttonMenu;

    private WindowManager windowManager;

    private static ViewManager manager;

    private LayoutParams floatBallParams;

    private LayoutParams floatMenuParams;
    private LayoutParams buttonParams;

    private Context context;
    private float endX;
    private float endY;

    //私有化构造函数
    private ViewManager(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        floatBall = new FloatBall(context);
//        floatMenu = new FloatMenu(context);
        buttonMenu = new ButtonMenu(context);
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            float startX;
            float startY;
            float tempX;
            float tempY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Message message = new Message();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();

                        tempX = event.getRawX();
                        tempY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() - startX;
                        float y = event.getRawY() - startY;
                        //计算偏移量，刷新视图
                        floatBallParams.x += x;
                        floatBallParams.y += y;
                        floatBall.setDragState(true);
                        windowManager.updateViewLayout(floatBall, floatBallParams);
                        if (buttonParams == null) {
                        } else {
                            windowManager.removeView(buttonMenu);
                            buttonParams = null;
                        }
                        startX = event.getRawX();
                        startY = event.getRawY();
                        isOnMoving = true; //正在移动，不要隐藏

                        message.what = MSG_HIDE;
                        hideHandler.removeMessages(message.what);
                        break;
                    case MotionEvent.ACTION_UP:
                        //判断松手时View的横坐标是靠近屏幕哪一侧，将View移动到依靠屏幕

                        endX = event.getRawX();
                        endY = event.getRawY();
                        if (endX < getScreenWidth() / 2) {
                            //左边
                            endX = 0;
//                            animateTo((int)endX,0);
                            isLeftHide = true;
                            isRightShow = false;
                        } else {
                            endX = getScreenWidth() - floatBall.width;
//                            animateTo((int)endX,getScreenWidth() - floatBall.width);
                            isLeftHide = false;
                            isRightShow = true;
                        }
                        floatBallParams.x = (int) endX;
                        floatBall.setDragState(false);

                        windowManager.updateViewLayout(floatBall, floatBallParams);
//                        Log.e(TAG, floatBallParams.x+" floatX onTouch: floatY=== "+floatBallParams.y );
                        //如果初始落点与松手落点的坐标差值超过6个像素，则拦截该点击事件
                        //否则继续传递，将事件交给OnClickListener函数处理
                        isOnMoving = false;
//                        hideHandler.postDelayed(hideRunable, 3000);
                        message.what = MSG_HIDE;
                        hideHandler.sendMessageDelayed(message, 3000);
                        if (Math.abs(endX - tempX) > 6 && Math.abs(endY - tempY) > 6) {
                            return true;
                        }

                        break;
                }
                return false;
            }
        };
        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Log.e(TAG, "onClick: buttonParams=== "+buttonParams );
                if (buttonParams == null) {
                    //还没打开
                    showButtonMenu();
                } else {
                    windowManager.removeView(buttonMenu);
                    buttonParams = null;
                }
//                buttonMenu.startAnimation();
            }
        };
//        OnClickListener clickListener = new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                windowManager.removeView(floatBall);
//                showFloatMenu();
//                floatMenu.startAnimation();
//            }
//        };
        floatBall.setOnTouchListener(touchListener);
        floatBall.setOnClickListener(clickListener);
    }

    //显示浮动小球
    public void showFloatBall() {
        if (floatBallParams == null) {
            floatBallParams = new LayoutParams();
            floatBallParams.width = floatBall.width;
            floatBallParams.height = floatBall.height - getStatusHeight();
            floatBallParams.gravity = Gravity.TOP | Gravity.LEFT;
            floatBallParams.type = LayoutParams.TYPE_TOAST;
            floatBallParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            floatBallParams.format = PixelFormat.RGBA_8888;
        }
        windowManager.addView(floatBall, floatBallParams);
        needHide = true;
        Log.e(TAG, "needHide=== " + needHide);
        Message message = new Message();
        message.what = MSG_HIDE;
        hideHandler.sendMessageDelayed(message, 3000);

    }

    private Boolean isRightShow = false;

    //显示按钮
    public void showButtonMenu() {
        if (buttonParams == null) {
//            Log.e(TAG, floatBallParams.x+" x showButtonMenu: y=== "+floatBallParams.y );
            buttonParams = new LayoutParams();
            buttonParams.width = LayoutParams.WRAP_CONTENT;
            buttonParams.height = LayoutParams.WRAP_CONTENT;
            buttonParams.gravity = Gravity.LEFT | Gravity.TOP;
            buttonParams.type = LayoutParams.TYPE_TOAST;
            buttonParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
            buttonParams.format = PixelFormat.RGBA_8888;
            buttonParams.x = floatBallParams.x;
            buttonParams.y = floatBallParams.y;
            if (isRightShow) {
                floatBallParams.x = 550;
                isRightShow = false;
            }
        }
//        Log.e(TAG, floatBallParams.x+" x showButtonMenu: y=== "+floatBallParams.y );
        windowManager.addView(buttonMenu, buttonParams);
        windowManager.removeView(floatBall);
        windowManager.addView(floatBall, floatBallParams);
    }

    //显示底部菜单
    private void showFloatMenu() {
        if (floatMenuParams == null) {
            floatMenuParams = new LayoutParams();
            floatMenuParams.width = getScreenWidth();
            floatMenuParams.height = getScreenHeight() - getStatusHeight();
            floatMenuParams.gravity = Gravity.BOTTOM;
            floatMenuParams.type = LayoutParams.TYPE_TOAST;
            floatMenuParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
            floatMenuParams.format = PixelFormat.RGBA_8888;
        }
        windowManager.addView(floatMenu, floatMenuParams);
    }

    //隐藏底部菜单
    public void hideFloatMenu() {
        if (floatMenu != null) {
            windowManager.removeView(floatMenu);
        }
    }

    //获取ViewManager实例
    public static ViewManager getInstance(Context context) {
        if (manager == null) {
            manager = new ViewManager(context);
        }
        return manager;
    }

    //获取屏幕宽度
    public int getScreenWidth() {
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    //获取屏幕高度
    public int getScreenHeight() {
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }

    //获取状态栏高度
    public int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(object);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            return 0;
        }
    }

//    public void removeView(){
//        windowManager.removeView();
//    }


    //计时隐藏
    public final static int MSG_HIDE = 0x01;
    public final static int MSG_HIDE_LEFT = 0x03;
    public final static int MSG_HIDE_RIGHT = 0x02;
    private Boolean isLeftHide = true; //用来判断是要左边隐藏还是右边

    @SuppressLint("HandlerLeak")
    private Handler hideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HIDE:
                    if (isLeftHide) {
                        Log.e(TAG, "needHide=== " + needHide);
                        if (needHide) {
                            floatBallParams.x = 0 - floatBall.getWidth() / 2;
                            windowManager.updateViewLayout(floatBall, floatBallParams);
                        }
//                    animateTo(floatBallParams.x,0 - floatBall.getWidth() / 2);
                        if (buttonParams != null) {
                            windowManager.removeView(buttonMenu);
                            buttonParams = null;
                        }
                    } else {
                        if (needHide) {
                            floatBallParams.x = getScreenWidth() - floatBall.getWidth() / 2;
                            windowManager.updateViewLayout(floatBall, floatBallParams);
                        }
//                    animateTo(floatBallParams.x,getScreenWidth() - floatBall.getWidth()/2);
                        if (buttonParams != null) {
                            windowManager.removeView(buttonMenu);
                            buttonParams = null;
                        }
                    }
                    break;
            }

        }
    };

    private Boolean isOnMoving;
    private Runnable hideRunable = new Runnable() {

        @Override
        public void run() {
            Message message = new Message();
            if (!isOnMoving) {
                if (isLeftHide) {
                    message.what = MSG_HIDE_LEFT;
                    hideHandler.sendMessage(message);
                } else {
                    message.what = MSG_HIDE_RIGHT;
                    hideHandler.sendMessage(message);
                }
            }
        }
    };

    private void animateTo(int beginValue, int endValue) {
        Log.e(TAG, beginValue + " beginValue endValue=== " + endValue);
        ValueAnimator animator = ValueAnimator.ofInt(beginValue, endValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                endX = (Integer) animation.getAnimatedValue();
                floatBallParams.x = (int) endX;
                windowManager.updateViewLayout(floatBall, floatBallParams);
            }
        });

        animator.start();
    }

    private Boolean needHide = true;

    public void destory() {
        windowManager.removeViewImmediate(floatBall);
        needHide = false;
//        Message message = new Message();
//        message.what = MSG_HIDE;
//        hideHandler.removeMessages(message.what);
        if (buttonParams != null) {
            windowManager.removeViewImmediate(buttonMenu);
            buttonParams = null;
        }
    }

    public interface OnMenuClickListener {
        void OnSelfCenter(String success);
    }

}
