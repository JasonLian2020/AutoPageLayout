package me.jason.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.jason.app.R;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.ui
 * Created by jason on 2018/12/20 15:16 .
 */
public class ThirdActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ThirdActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }
}
