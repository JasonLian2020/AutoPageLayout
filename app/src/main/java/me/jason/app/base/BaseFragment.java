package me.jason.app.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Project Name:AutoPageLayout
 * Package Name:me.jason.app.base
 * Created by jason on 2019/1/30 15:14 .
 */
public abstract class BaseFragment extends Fragment implements IFragment {

    private BaseHandler baseHandler;

    /**
     * 需要处理消息，就去重写该方法，并且用getHandler()来发送消息
     */
    protected void handleMessage(Message msg) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //
        baseHandler = new BaseHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (getActivityReference() == null || getActivityReference().get() == null) return;
                BaseFragment.this.handleMessage(msg);
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) parseBundle(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            baseHandler = new BaseHandler(getActivity());
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
