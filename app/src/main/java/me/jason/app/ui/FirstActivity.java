package me.jason.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import me.jason.app.R;
import me.jason.app.base.BaseActivity;
import me.jason.autopagelayout.AutoPageLayout;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.ui
 * Created by jason on 2018/12/20 15:16 .
 */
public class FirstActivity extends BaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FirstActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_first;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initAutoPageLayout();
        // simulate network request
        getHandler().postDelayed(() -> {
            if (layoutRandom()) showContentLayout();
            else showErrorLayout();
        }, 2000);
    }

    private boolean layoutRandom() {
        int randomNumber = (int) (Math.random() * 100);
        if (randomNumber % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void showLoadingLayout() {
        if (autoPageLayout != null) autoPageLayout.showLoading();
    }

    private void showContentLayout() {
        if (autoPageLayout != null) autoPageLayout.showContent();
    }

    private void showErrorLayout() {
        if (autoPageLayout != null) autoPageLayout.showError();
    }

    private void showEmptyLayout() {
        if (autoPageLayout != null) autoPageLayout.showEmpty();
    }

    private AutoPageLayout autoPageLayout;

    private void initAutoPageLayout() {
        autoPageLayout = new AutoPageLayout.Builder(this)
                .setTarget(this)
                .setLoadingLayout(R.layout.public_layout_loading, view -> {
                    // view onCreate, do some initialization
                })
                .setEmptyLayout(R.layout.public_layout_empty, view -> {
                    // view onCreate, do some initialization
                    view.setOnClickListener(v -> clickErrorLayout());
                })
                .setErrorLayout(R.layout.public_layout_error, view -> {
                    // view onCreate, do some initialization
                    view.setOnClickListener(v -> clickErrorLayout());
                })
                .showType(AutoPageLayout.SHOW_TYPE_LOADING)
                .build();
    }

    private void clickErrorLayout() {
        showLoadingLayout();
        // simulate network request
        getHandler().postDelayed(() -> showContentLayout(), 2000);
    }
}
