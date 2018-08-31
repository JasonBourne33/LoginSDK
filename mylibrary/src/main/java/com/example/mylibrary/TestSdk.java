package com.example.mylibrary;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.service.StartFloatBallService;
import com.example.util.SharedPreferencesUtil;

import static android.content.ContentValues.TAG;

public class TestSdk {
//    public static void login(Context context, FragmentManager fragmentManager) {
//        login(context,fragmentManager,mOnLoginListener);
//    }

    public static void login(Context context, OnLoginListener listener) {
        Log.i("aaa", "test=== ");
        if (listener != null) {
            mOnLoginListener = listener;
            LoginDialog loginDialog = new LoginDialog(context, listener);
            loginDialog.show();
        } else {

        }
    }

    public static void startLoginService(Activity context) {
       // StartFloatBallService.startService(context);
        Intent intent = new Intent(context, StartFloatBallService.class);
        context.startService(intent);
        //context.bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

//    private static ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//            ((StartFloatBallService.SDKBinder)service).getService().startService()
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    public static OnLoginListener mOnLoginListener;

    public interface OnLoginListener {
        void onSuccess(String successJson);

        void onFailed(String failJson);
    }


    public interface OnWindowButtonDestory{
        void OnButtonDestory(Boolean needDestory);
    }
    public static OnWindowButtonDestory mOnWindowButtonDestory;

    public static void DestoryWindowButton(Context context){
        com.example.manager.ViewManager manager = com.example.manager.ViewManager.getInstance(context);
        manager.destory();
    };

    public static String getUserName(Context context){
        String userName = SharedPreferencesUtil.getString(context, "userName");
        Log.e(TAG, "getUserName:=== "+userName );
        return userName;
    };
//    public static void setOnLoginListener(OnLoginListener listener) {
//        mOnLoginListener = listener;
//    }
}
