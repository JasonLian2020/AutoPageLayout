package me.jason.app.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.base
 * Created by jason on 2018/12/20 15:25 .
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnbinder;

    private BaseHandler baseHandler = new BaseHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (getActivityReference() == null || getActivityReference().get() == null) return;
            BaseActivity.this.handleMessage(msg);
        }
    };

    /**
     * 需要处理消息，就去重写该方法，并且用getHandler()来发送消息
     */
    protected void handleMessage(Message msg) {

    }

    /**
     * 初始化 View, 如果 {@link #initView(Bundle)} 返回 0, 框架则不会调用 {@link Activity#setContentView(int)}
     *
     * @param savedInstanceState
     * @return
     */
    public abstract int initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    public abstract void initData(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release Unbinder
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        // release Handler
        if (baseHandler != null)
            baseHandler.removeCallbacksAndMessages(null);
        this.baseHandler = null;
    }

    /**
     * handler会在基类里面释放资源，不需要手动再调用释放
     */
    public final Handler getHandler() {
        if (baseHandler == null) {
            baseHandler = new BaseHandler(this);
        }
        return baseHandler;
    }

    private static class BaseHandler extends Handler {
        private final WeakReference<Activity> mActivityReference;

        public BaseHandler(Activity activity) {
            super(Looper.getMainLooper());
            mActivityReference = new WeakReference<>(activity);
        }

        public WeakReference<Activity> getActivityReference() {
            return mActivityReference;
        }
    }
}
