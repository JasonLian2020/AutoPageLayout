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
        // init parameter
        layoutInflater = LayoutInflater.from(builder.context);
        /*--------------------------core code begin--------------------------*/
        ViewGroup parentLayout = null;
        if (builder.targetView instanceof Activity) {
            parentLayout = ((Activity) builder.targetView).findViewById(android.R.id.content);
        } else if (builder.targetView instanceof Fragment) {
            parentLayout = (ViewGroup) ((Fragment) builder.targetView).getView().getParent();
        } else if (builder.targetView instanceof View) {
            parentLayout = (ViewGroup) ((View) builder.targetView).getParent();
        }
        if (parentLayout != null) {
            // addView index
            int index = 0;
            // old content layout
            View oldContentLayout;
            // child view count
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
            // 1.save old
            contentLayout = oldContentLayout;
            // 2.remove all views with AutoPageLayout
            removeAllViews();
            // 3.remove old content layout with parent layout
            parentLayout.removeView(contentLayout);
            // 4.add AutoPageLayout to parent layout
            ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
            parentLayout.addView(this, index, layoutParams);
            // 5.add old content layout to AutoPageLayout
            addView(contentLayout, 0, new FrameLayout.LayoutParams(-1, -1));
        }
        /*--------------------------core code end--------------------------*/
        // init other：empty，loading，error，custom
        initOtherLayout(parentLayout);
        // show layout
        if (builder.showType != -1) showView(builder.showType);
    }

    private void initOtherLayout(ViewGroup parentLayout) {
        if (parentLayout == null) return;
        // empty layout
        if (builder.emptyLayoutId > 0) {
            emptyLayout = layoutInflater.inflate(builder.emptyLayoutId, this, false);
            addView(emptyLayout);
            //
            if (builder.emptyViewAddListener != null)
                builder.emptyViewAddListener.onCreate(emptyLayout);
            //
            emptyLayout.setVisibility(GONE);
            if (builder.emptyViewShowListener != null)
                builder.emptyViewShowListener.onShow(false, emptyLayout);
        }
        // loading layout
        if (builder.loadingLayoutId > 0) {
            loadingLayout = layoutInflater.inflate(builder.loadingLayoutId, this, false);
            addView(loadingLayout);
            //
            if (builder.loadingViewAddListener != null)
                builder.loadingViewAddListener.onCreate(loadingLayout);
            //
            loadingLayout.setVisibility(GONE);
            if (builder.loadingViewShowListener != null)
                builder.loadingViewShowListener.onShow(false, loadingLayout);
        }
        // error layout
        if (builder.errorLayoutId > 0) {
            errorLayout = layoutInflater.inflate(builder.errorLayoutId, this, false);
            addView(errorLayout);
            //
            if (builder.errorViewAddListener != null)
                builder.errorViewAddListener.onCreate(errorLayout);
            //
            errorLayout.setVisibility(GONE);
            if (builder.errorViewShowListener != null)
                builder.errorViewShowListener.onShow(false, errorLayout);
        }
        // custom layout
        if (builder.customLayoutId > 0) {
            customLayout = layoutInflater.inflate(builder.customLayoutId, this, false);
            addView(customLayout);
            //
            if (builder.customViewAddListener != null)
                builder.customViewAddListener.onCreate(customLayout);
            //
            customLayout.setVisibility(GONE);
            if (builder.customViewShowListener != null)
                builder.customViewShowListener.onShow(false, customLayout);
        }
    }

    public void showContent() {
        showView(SHOW_TYPE_CONTENT);
    }

    public void showEmpty() {
        showView(SHOW_TYPE_EMPTY);
    }

    public void showLoading() {
        showView(SHOW_TYPE_LOADING);
    }

    public void showError() {
        showView(SHOW_TYPE_ERROR);
    }

    public void showCustom() {
        showView(SHOW_TYPE_CUSTOM);
    }

    /**
     * show layout
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
            tempLayout.setTag(TAG_VISIBLE);
            if (tempLayout.getAnimation() == null && tempLayout.getVisibility() == VISIBLE) return;
            tempLayout.setVisibility(VISIBLE);
            if (tempListener != null) tempListener.onShow(true, tempLayout);
        } else {
            tempLayout.setTag(TAG_GONE);
            if (tempLayout.getVisibility() == GONE) return;
            viewHideAnimation(tempLayout);
            if (tempListener != null) tempListener.onShow(false, tempLayout);
        }
    }

    private static final String TAG_VISIBLE = "VISIBLE";
    private static final String TAG_GONE = "GONE";

    private void viewShowAnimation(final View view) {
        if (view.getAnimation() != null) return;
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(100);
        alpha.setRepeatCount(0);
        alpha.setFillAfter(true);
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

    private void viewHideAnimation(final View view) {
        if (view.getAnimation() != null) return;
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(100);
        alpha.setRepeatCount(0);
        alpha.setFillAfter(true);
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
        // add listenr
        protected OnViewAddListener emptyViewAddListener;
        protected OnViewAddListener loadingViewAddListener;
        protected OnViewAddListener errorViewAddListener;
        protected OnViewAddListener customViewAddListener;
        // show or hide listenr
        protected OnViewShowListener emptyViewShowListener;
        protected OnViewShowListener loadingViewShowListener;
        protected OnViewShowListener errorViewShowListener;
        protected OnViewShowListener customViewShowListener;

        /**
         * base on Activity
         */
        public Builder setTarget(@NonNull Activity targetView) {
            this.targetView = targetView;
            return this;
        }

        /**
         * base on Fragment
         */
        public Builder setTarget(@NonNull Fragment targetView) {
            this.targetView = targetView;
            return this;
        }

        /**
         * base on View
         */
        public Builder setTarget(@NonNull View targetView) {
            this.targetView = targetView;
            return this;
        }

        public Builder showType(int showType) {
            this.showType = showType;
            return this;
        }

        public Builder setEmptyLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            emptyLayoutId = layoutResId;
            emptyViewAddListener = onViewAddListener;
            return this;
        }

        public Builder setLoadingLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            loadingLayoutId = layoutResId;
            loadingViewAddListener = onViewAddListener;
            return this;
        }

        public Builder setErrorLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            errorLayoutId = layoutResId;
            errorViewAddListener = onViewAddListener;
            return this;
        }

        public Builder setCustomLayout(@LayoutRes int layoutResId, OnViewAddListener onViewAddListener) {
            customLayoutId = layoutResId;
            customViewAddListener = onViewAddListener;
            return this;
        }

        public Builder setEmptyShowListener(OnViewShowListener onViewShowListener) {
            emptyViewShowListener = onViewShowListener;
            return this;
        }

        public Builder setLoadingShowListener(OnViewShowListener onViewShowListener) {
            loadingViewShowListener = onViewShowListener;
            return this;
        }

        public Builder setErrorShowListener(OnViewShowListener onViewShowListener) {
            errorViewShowListener = onViewShowListener;
            return this;
        }

        public Builder setCustomShowListener(OnViewShowListener onViewShowListener) {
            customViewShowListener = onViewShowListener;
            return this;
        }

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder(@NonNull Fragment fragment) {
            this.context = fragment.getContext();
        }

        @UiThread
        public AutoPageLayout build() {
            return new AutoPageLayout(this);
        }
    }
}