package com.example.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.manager.ViewManager;

public class StartFloatBallService extends Service {

    public StartFloatBallService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ViewManager manager = ViewManager.getInstance(this);
//        manager.showFloatBall();

//        Dialog dialog = new Dialog(this);
//        dialog.show();
    }

    public static void startService(Context context) {

        ViewManager manager = ViewManager.getInstance(context);
        manager.showFloatBall();
    }


//    public class SDKBinder extends Binder {
//
//        public StartFloatBallService getService() {
//            return StartFloatBallService.this;
//        }
//    }
}
