package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dao.LoginBean;
import com.example.mylibrary2.R;
import com.example.util.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class RegistByAccountFragment extends Fragment implements View.OnClickListener{

    private static final int USER_EXIST = 1;
    private Context mContext;
    @SuppressLint("ValidFragment")
    public RegistByAccountFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registbyaccount, container, false);
        Button btnEnter = rootView.findViewById(R.id.btn_enter);
        btnEnter.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_enter) {
            regist();
        }
    }
    private String message;
    private void regist(){
        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(user)
        RequestBody body = new FormBody.Builder()
                .add("username", "test")
                .add("password", "test")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url("http://45.78.17.33/gameserver/user/register")
                .build();
//                    Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Gson gson = new Gson();
                    LoginBean bean = gson.fromJson(jsonData, LoginBean.class);
                    Log.e("TAG", "bean.getErroe_code()=== " + bean.getErroe_code());

                    message = bean.getMessage().toString();
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(jsonData);
                    int erroe_code = jsonObject.getInt("erroe_code");
                    if (erroe_code == 1) {

                    } else {
                        Message message = new Message();
                        message.what = USER_EXIST;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case USER_EXIST:
                    Log.e(TAG, "handleMessage:mContext=== "+mContext );
                    Toast.makeText(mContext,"账号已被注册",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
