package me.jason.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import me.jason.app.base.BaseActivity;
import me.jason.app.ui.FirstActivity;
import me.jason.app.ui.SecondActivity;
import me.jason.app.ui.ThirdActivity;


public class MainActivity extends BaseActivity {
    @BindView(R.id.buttonActivity)
    Button buttonActivity;
    @BindView(R.id.buttonFragment)
    Button buttonFragment;
    @BindView(R.id.buttonView)
    Button buttonView;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.buttonActivity, R.id.buttonFragment, R.id.buttonView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonActivity:
                // Base On Activity
                FirstActivity.start(this);
                break;
            case R.id.buttonFragment:
                // Base On Fragment
                SecondActivity.start(this);
                break;
            case R.id.buttonView:
                // Base On View
                ThirdActivity.start(this);
                break;
        }
    }
}
