package com.example.mylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adapter.ViewPagerAdapter;
import com.example.fragment.RegistByAccountFragment;
import com.example.fragment.RegistByPhoneFragment;
import com.example.mylibrary2.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterDialog extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private FragmentManager fragmentManager;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> listFragment;
    private ImageView imgClose;
    private String[] titles = {"手机/邮箱注册", "普通账号注册"};
//    public RegisterDialog(@NonNull Context context, FragmentManager fragmentManager) {
//        super(context);
//        this.mContext=context;
//        this.fragmentManager = fragmentManager;
//    }
//
//    public RegisterDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }
//
//    protected RegisterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_register);

        tabLayout =  findViewById(R.id.tab_mine_pay);
        viewPager =  findViewById(R.id.vp_mine_pay);
        imgClose =  findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        Log.e("e","fragmentManager=== "+getSupportFragmentManager());
        listFragment = new ArrayList<>();
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), listFragment);
        for (int i = 0; i < titles.length; i++) {
            View view = View.inflate(this, R.layout.item_tab_view, null);
            TextView tvTabs =  view.findViewById(R.id.tv_tab_item_title);
            tvTabs.setText(titles[i]);
            tvTabs.setTextSize(14);
            tabLayout.addTab(tabLayout.newTab().setCustomView(view));
        }
        Fragment registByPhoneFragment = new RegistByPhoneFragment();
        Fragment registByAccountAccount = new RegistByAccountFragment();
        listFragment.add(registByPhoneFragment);
        listFragment.add(registByAccountAccount);
        viewPager.setAdapter(adapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_close) {
            RegisterDialog.this.finish();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        System.out.println("tag = "+getTag()); // tag which is from acitivity which started this fragment
//        View view  = inflater.inflate(R.layout.dialog_register, null);
//        tabLayout =  view.findViewById(R.id.tab_mine_pay);
//        viewPager =  view.findViewById(R.id.vp_mine_pay);
//        Log.e("e","fragmentManager=== "+getFragmentManager());
//        listFragment = new ArrayList<>();
//        adapter = new ViewPagerAdapter(getFragmentManager(), listFragment);
//        for (int i = 0; i < titles.length; i++) {
//            View tabView = View.inflate(mContext, R.layout.item_tab_view, null);
//            TextView tvTabs =  tabView.findViewById(R.id.tv_tab_item_title);
//            tvTabs.setText(titles[i]);
//            tvTabs.setTextSize(16);
//            tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));
//        }
//        Fragment registByPhoneFragment = new RegistByPhoneFragment();
//        Fragment registByAccountAccount = new RegistByAccountFragment();
//        listFragment.add(registByPhoneFragment);
//        listFragment.add(registByAccountAccount);
//        viewPager.setAdapter(adapter);
//
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        return view;
//    }
}
