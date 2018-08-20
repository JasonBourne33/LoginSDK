package com.example.asus_pc.loginsdk;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mylibrary.TestSdk;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        TestSdk.login(this, new TestSdk.OnLoginListener() {
            @Override
            public void onSuccess(String successJson) {
                Intent intent = new Intent(MainActivity.this,InGameActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String failJson) {

            }

        });
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                TestSdk.login(this,fragmentManager);
                TestSdk.login(this, new TestSdk.OnLoginListener() {
                    @Override
                    public void onSuccess(String successJson) {
                        Intent intent = new Intent(MainActivity.this,InGameActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String failJson) {

                    }

                });
                break;
        }
    }
}
