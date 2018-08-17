package com.example.mylibrary;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

public class TestSdk {
//    public static void login(Context context, FragmentManager fragmentManager) {
//        login(context,fragmentManager,mOnLoginListener);
//    }

    public static void login(Context context, FragmentManager fragmentManager,OnLoginListener listener) {

        Log.i("aaa", "test=== ");
        if(listener != null){
            LoginDialog loginDialog = new LoginDialog(context, fragmentManager,listener);
            loginDialog.show();
        }else {

        }
    }

    public static OnLoginListener mOnLoginListener;

    public interface OnLoginListener {
        void onSuccess(String successJson);
        void onFailed(String failJson);
    }

    public static void setOnLoginListener(OnLoginListener listener) {
        mOnLoginListener = listener;
    }
}
