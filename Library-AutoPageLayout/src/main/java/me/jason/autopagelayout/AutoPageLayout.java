package me.jason.autopagelayout;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

/**
 * Project Name:LiZhiWeiKe
 * Package Name:com.widget.dialog
 * Created by jason on 2018/11/1 15:03 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */
public class AutoPageLayout extends FrameLayout {
    /*-----------------------------------SHOW TYPE BEGIN-----------------------------------*/
    /**
     * 内容布局
     */
    public static final int SHOW_TYPE_CONTENT = 0x1;
    /**
     * 空布局
     */
    public static final int SHOW_TYPE_EMPTY = 0x2;
    /**
     * 加载中布局
     */
    public static final int SHOW_TYPE_LOADING = 0x3;
    /**
     * 错误布局
     */
    public static final int SHOW_TYPE_ERROR = 0x4;
    /**
     * 自定义布局
     */
    public static final int SHOW_TYPE_CUSTOM = 0x5;
    /*-----------------------------------SHOW TYPE END-----------------------------------*/

    protected final Builder builder;
    private LayoutInflater layoutInflater;
    private View contentLayout;
    private View emptyLayout;
    private View loadingLayout;
    private View errorLayout;
    private View customLayout;

    public AutoPageLayout(@NonNull Builder builder) {
        super(builder.context);
        this.builder = builder;
        // init page
        initPage();
        // Don't keep a Context reference in the Builder after this point
        builder.context = null;
        builder.targetView = null;
    }

    public interface OnViewAddListener {
        void onCreate(View view);
    }

    public interface OnViewShowListener {
        void onShow(boolean isShow, View view);
    }

    /**
     * 初始化配置
     */
    private void initPage() {
        // 初始化参数
        layoutInflater = LayoutInflater.from(builder.context);
        /*--------------------------核心代码 begin--------------------------*/
        ViewGroup parentLayout = null;
        if (builder.targetView instanceof Activity) {
            parentLayout = ((Activity) builder.targetView).findViewById(android.R.id.content);
        } else if (builder.targetView instanceof Fragment) {
            parentLayout = (ViewGroup) ((Fragment) builder.targetView).getView().getParent();
        } else if (builder.targetView instanceof View) {
            parentLayout = (ViewGroup) ((View) builder.targetView).getParent();
        }
        if (parentLayout != null) {
            // addView的索引位置
            int index = 0;
            // 旧内容布局
            View oldContentLayout;
            // 子view个数
            int childCount = parentLayout.getChildCount();
            if (builder.targetView instanceof View) {
                oldContentLayout = (View) builder.targetView;
                for (int i = 0; i < childCount; i++) {
                    View childView = parentLayout.getChildAt(i);
                    if (childView.toString().equals(oldContentLayout.toString())) {
                        index = i;
                        break;
                    }
                }
            } else {
                oldContentLayout = parentLayout.getChildAt(0);
            }
            // 1.记录下旧的内容布局
            contentLayout = oldContentLayout;
            // 2.移除PageLayout中所有子view
            removeAllViews();
            // 3.移除父布局中旧内容布局
            parentLayout.removeView(contentLayout);
            // 4.添加PageLayout到父布局
            ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
            parentLayout.addView(this, index, layoutParams);
            // 5.添加内容布局到PageLayout
            addView(contentLayout, 0, new FrameLayout.LayoutParams(-1, -1));
        }
        /*--------------------------核心代码 end--------------------------*/
        // 初始化其他布局：empty，loading，error，custom
        initOtherLayout(parentLayout);
        // 显示布局
        if (builder.showType != -1) showView(builder.showType);
    }

    private void initOtherLayout(ViewGroup parentLayout) {
        if (parentLayout == null) return;
        // 空布局
        if (builder.emptyLayoutId > 0) {
            emptyLayout = layoutInflater.inflate(builder.emptyLayoutId, this, false);
            addView(emptyLayout);
            // 添加布局成功并且回调
            if (builder.emptyViewAddListener != null)
                builder.emptyViewAddListener.onCreate(emptyLayout);
            // 初始化隐藏并且回调
            emptyLayout.setVisibility(GONE);
            if (builder.emptyViewShowListener != null)
                builder.emptyViewShowListener.onShow(false, emptyLayout);
        }
        // 加载中布局
        if (builder.loadingLayoutId > 0) {
            loadingLayout = layoutInflater.inflate(builder.loadingLayoutId, this, false);
            addView(loadingLayout);
            // 添加布局成功并且回调
            if (builder.loadingViewAddListener != null)
                builder.loadingViewAddListener.onCreate(loadingLayout);
            // 初始化隐藏并且回调
            loadingLayout.setVisibility(GONE);
            if (builder.loadingViewShowListener != null)
                builder.loadingViewShowListener.onShow(false, loadingLayout);
        }
        // 错误布局
        if (builder.errorLayoutId > 0) {
            errorLayout = layoutInflater.inflate(builder.errorLayoutId, this, false);
            addView(errorLayout);
            // 添加布局成功并且回调
            if (builder.errorViewAddListener != null)
                builder.errorViewAddListener.onCreate(errorLayout);
            // 初始化隐藏并且回调
            errorLayout.setVisibility(GONE);
            if (builder.errorViewShowListener != null)
                builder.errorViewShowListener.onShow(false, errorLayout);
        }
        // 自定义布局
        if (builder.customLayoutId > 0) {
            customLayout = layoutInflater.inflate(builder.customLayoutId, this, false);
            addView(customLayout);
            // 添加布局成功并且回调
            if (builder.customViewAddListener != null)
                builder.customViewAddListener.onCreate(customLayout);
            // 初始化隐藏并且回调
            customLayout.setVisibility(GONE);
            if (builder.customViewShowListener != null)
                builder.customViewShowListener.onShow(false, customLayout);
        }
    }

    /**
     * 显示内容布局
     */
    public void showContent() {
        showView(SHOW_TYPE_CONTENT);
    }

    /**
     * 显示空布局
     */
    public void showEmpty() {
        showView(SHOW_TYPE_EMPTY);
    }

    /**
     * 显示加载中布局
     */
    public void showLoading() {
        showView(SHOW_TYPE_LOADING);
    }

    /**
     * 显示错误布局
     */
    public void showError() {
        showView(SHOW_TYPE_ERROR);
    }

    /**
     * 显示自定义布局
     */
    public void showCustom() {
        showView(SHOW_TYPE_CUSTOM);
    }

    /**
     * 显示哪种页面
     *
     * @param showType {@link AutoPageLayout#SHOW_TYPE_CONTENT}
     *                 、{@link AutoPageLayout#SHOW_TYPE_EMPTY}
     *                 、{@link AutoPageLayout#SHOW_TYPE_LOADING}
     *                 、{@link AutoPageLayout#SHOW_TYPE_ERROR}
     *                 、{@link AutoPageLayout#SHOW_TYPE_CUSTOM}
     */
    private void showView(final int showType) {
        if (Looper.myLooper() == Looper.getMainLooper()) changeView(showType);
        else post(new Runnable() {
            @Override
            public void run() {
                changeView(showType);
            }
        });
    }

    private void changeView(int showType) {
        /**
         * 为什么一直显示contentLayout？
         * 1.由于contentLayout添加在父布局的索引0位置，不需要隐藏，其他View肯定在它之上
         * 2.并且contentLayout包含WebView的时候，当url加载很快，执行隐藏后显示会偶现空白页面
         */
        showContentLayout(true);
        switch (showType) {
            case SHOW_TYPE_CONTENT:
                showEmptyLayout(false);
                showLoadingLayout(false);
                showErrorLayout(false);
                showCustomLayout(false);
                break;
            case SHOW_TYPE_EMPTY:
                showEmptyLayout(true);
                showLoadingLayout(false);
                showErrorLayout(false);
                showCustomLayout(false);
                break;
            case SHOW_TYPE_LOADING:
                showEmptyLayout(false);
                showLoadingLayout(true);
                showErrorLayout(false);
                showCustomLayout(false);
                break;
            case SHOW_TYPE_ERROR:
                showEmptyLayout(false);
                showLoadingLayout(false);
                showErrorLayout(true);
                showCustomLayout(false);
                break;
            case SHOW_TYPE_CUSTOM:
                showEmptyLayout(false);
                showLoadingLayout(false);
                showErrorLayout(false);
                showCustomLayout(true);
                break;
        }
    }

    private void showContentLayout(boolean isShow) {
        showViewLayout(isShow, SHOW_TYPE_CONTENT);
    }

    private void showEmptyLayout(boolean isShow) {
        showViewLayout(isShow, SHOW_TYPE_EMPTY);
    }

    private void showLoadingLayout(boolean isShow) {
        showViewLayout(isShow, SHOW_TYPE_LOADING);
    }

    private void showErrorLayout(boolean isShow) {
        showViewLayout(isShow, SHOW_TYPE_ERROR);
    }

    private void showCustomLayout(boolean isShow) {
        showViewLayout(isShow, SHOW_TYPE_CUSTOM);
    }

    private void showViewLayout(boolean isShow, int showType) {
        View tempLayout = null;
        OnViewShowListener tempListener = null;
        switch (showType) {
            case SHOW_TYPE_CONTENT:
                tempLayout = contentLayout;
                break;
            case SHOW_TYPE_EMPTY:
                tempLayout = emptyLayout;
                tempListener = builder.emptyViewShowListener;
                break;
            case SHOW_TYPE_LOADING:
                tempLayout = loadingLayout;
                tempListener = builder.loadingViewShowListener;
                break;
            case SHOW_TYPE_ERROR:
                tempLayout = errorLayout;
                tempListener = builder.errorViewShowListener;
                break;
            case SHOW_TYPE_CUSTOM:
                tempLayout = customLayout;
                tempListener = builder.customViewShowListener;
                break;
        }
        if (tempLayout == null) return;
        if (isShow) {
            // 标记
            tempLayout.setTag(TAG_VISIBLE);
            // 已显示：不需要再执行动作和回调
            if (tempLayout.getAnimation() == null && tempLayout.getVisibility() == VISIBLE) return;
            // 执行显示动作并且回调
            tempLayout.setVisibility(VISIBLE);
            if (tempListener != null) tempListener.onShow(true, tempLayout);
        } else {
            // 标记
            tempLayout.setTag(TAG_GONE);
            // 已隐藏：不需要再执行动作和回调
            if (tempLayout.getVisibility() == GONE) return;
            // 执行隐藏动作并且回调
            viewHideAnimation(tempLayout);
            if (tempListener != null) tempListener.onShow(false, tempLayout);
        }
    }

    private static final String TAG_VISIBLE = "VISIBLE";
    private static final String TAG_GONE = "GONE";

    /**
     * view显示动画
     */
    private void viewShowAnimation(final View view) {
        // 避免动画未结束，再次调用
        if (view.getAnimation() != null) return;
        // 开始执行动画
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(100);// 动画持续时间，毫秒为单位
        alpha.setRepeatCount(0);// 重复次数
        alpha.setFillAfter(true);// 控件动画结束时是否保持动画最后的状态
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (view.getTag() == null || TAG_VISIBLE.equals(view.getTag()))
                    view.setVisibility(VISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alpha);
        view.setVisibility(VISIBLE);
    }

    /**
     * view隐藏动画
     */
    private void viewHideAnimation(final View view) {
        // 避免动画未结束，再次调用
        if (view.getAnimation() != null) return;
        // 开始执行动画
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(100);// 动画持续时间，毫秒为单位
        alpha.setRepeatCount(0);// 重复次数
        alpha.setFillAfter(true);// 控件动画结束时是否保持动画最后的状态
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (view.getTag() == null || TAG_GONE.equals(view.getTag()))
                    view.setVisibility(GONE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alpha);
    }

    public static class Builder {
        protected Context context;
        protected Object targetView;
        protected int showType = SHOW_TYPE_CONTENT;
        @LayoutRes
        protected int emptyLayoutId = -1;
        @LayoutRes
        protected int loadingLayoutId = -1;
        @LayoutRes
        protected int errorLayoutId = -1;
        @LayoutRes
        protected int customLayoutId = -1;
        // 添加布局监听
        protected OnViewAddListener emptyViewAddListener;
        protected OnViewAddListener loadingViewAddListener;
        protected OnViewAddListener errorViewAddListener;
        protected OnViewAddListener customViewAddListener;
        // 显示隐藏监听
        protected OnViewShowListener emptyViewShowListener;
        protected OnViewShowListener loadingViewShowListener;
        protected OnViewShowListener errorViewShowListener;
        protected OnViewShowListener customViewShowListener;

        /**
         * 设置基于Activity目标来切换
         */
        public Builder setTarget(@NonNull Activity targetView) {
            this.targetView = targetView;
            return this;
        }

        /**
         * 设置基于Fragment目标来切换
         */
        public Builder setTarget(@NonNull Fragment targetView) {
            this.targetView = targetView;
            return this;
        }

        /**
         * 设置基于View目标来切换
         */
        public Builder setTarget(@NonNull View targetView) {
            this.targetView = targetView;
            return this;
        }

        /**
         * 设置默认展示的页面
         */
        public Builder showType(int showType) {
            this.showType = showType;
            return this;
        }

        /**
         * 设置空布局
         *
         * @param layoutResId       资源id
         * @param onViewAddListener 布局添加的监听
         */
        public Builder setEmptyLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            emptyLayoutId = layoutResId;
            emptyViewAddListener = onViewAddListener;
            return this;
        }

        /**
         * 设置加载中布局
         *
         * @param layoutResId       资源id
         * @param onViewAddListener 布局添加的监听
         */
        public Builder setLoadingLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            loadingLayoutId = layoutResId;
            loadingViewAddListener = onViewAddListener;
            return this;
        }

        /**
         * 设置错误布局
         *
         * @param layoutResId       资源id
         * @param onViewAddListener 布局添加的监听
         */
        public Builder setErrorLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            errorLayoutId = layoutResId;
            errorViewAddListener = onViewAddListener;
            return this;
        }

        /**
         * 设置自定义布局
         *
         * @param layoutResId       资源id
         * @param onViewAddListener 布局添加的监听
         */
        public Builder setCustomLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            customLayoutId = layoutResId;
            customViewAddListener = onViewAddListener;
            return this;
        }

        /**
         * 设置空布局显示隐藏监听
         */
        public Builder setEmptyShowListener(OnViewShowListener onViewShowListener) {
            emptyViewShowListener = onViewShowListener;
            return this;
        }

        /**
         * 设置加载中布局显示隐藏监听
         */
        public Builder setLoadingShowListener(OnViewShowListener onViewShowListener) {
            loadingViewShowListener = onViewShowListener;
            return this;
        }

        /**
         * 设置错误布局显示隐藏监听
         */
        public Builder setErrorShowListener(OnViewShowListener onViewShowListener) {
            errorViewShowListener = onViewShowListener;
            return this;
        }

        /**
         * 设置自定义布局显示隐藏监听
         */
        public Builder setCustomShowListener(OnViewShowListener onViewShowListener) {
            customViewShowListener = onViewShowListener;
            return this;
        }

        /**
         * 初始化一些样式
         */
        public Builder(@NonNull Context context) {
            this.context = context;
        }

        /**
         * 初始化一些样式
         */
        public Builder(@NonNull Fragment fragment) {
            this.context = fragment.getContext();
        }

        @UiThread
        public AutoPageLayout build() {
            return new AutoPageLayout(this);
        }
    }
}