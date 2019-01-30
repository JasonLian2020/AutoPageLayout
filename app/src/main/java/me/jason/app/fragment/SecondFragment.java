package me.jason.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.jason.app.R;
import me.jason.app.base.BaseFragment;
import me.jason.library.AutoPageLayout;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.fragment
 * Created by jason on 2019/1/30 15:10 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */
public class SecondFragment extends BaseFragment {

    public static SecondFragment newInstance() {
        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void parseBundle(@NonNull Bundle bundle) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        return rootView;
    }

    @Override
    public void initData() {
        // Must be called after onCreateView
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
