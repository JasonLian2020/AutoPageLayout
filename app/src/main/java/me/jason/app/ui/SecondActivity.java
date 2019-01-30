package me.jason.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import me.jason.app.R;
import me.jason.app.base.BaseActivity;
import me.jason.app.fragment.SecondFragment;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.ui
 * Created by jason on 2018/12/20 15:16 .
 */
public class SecondActivity extends BaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SecondActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_second;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentLayout, SecondFragment.newInstance());
        ft.commitAllowingStateLoss();
    }
}
