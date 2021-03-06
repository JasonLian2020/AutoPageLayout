package me.jason.app.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.base
 * Created by jason on 2019/1/30 15:22 .
 */
public interface IFragment {
    /**
     * 解析bundle数据
     * <P>{@link Fragment#getArguments()}不为null的时候才会回调</P>
     */
    void parseBundle(@NonNull Bundle bundle);

    /**
     * 初始化视图
     */
    View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化数据
     */
    void initData();
}
