package com.example.mylibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TestSdk {
    public static void print(Context context){

        Log.i("aaa","test=== ");
        Toast.makeText(context,"print=== ",Toast.LENGTH_LONG).show();
        LoginDialog loginDialog = new LoginDialog(context);
        loginDialog.show();
    }
}
