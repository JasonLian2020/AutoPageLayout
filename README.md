[![Download](https://img.shields.io/badge/Download-AutoPageLayout-brightgreen.svg?style=flat)](https://bintray.com/jasonlian/maven/auto-page-layout)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://www.travis-ci.org/tomUp/AutoPageLayout.svg?branch=master)](https://www.travis-ci.org/tomUp/AutoPageLayout)

# AutoPageLayout


![Base Activity](https://github.com/tomUp/AutoPageLayout/blob/master/image/Base%20Activity.jpg?raw=true)

![Base Fragment](https://github.com/tomUp/AutoPageLayout/blob/master/image/Base%20Fragment.jpg?raw=true)

![Base View](https://github.com/tomUp/AutoPageLayout/blob/master/image/Base%20View.jpg?raw=true)

# Download

Get the latest artifact via gradle
```groovy
implementation 'me.jason.library:auto-page-layout:1.0.1'
```

# Usage

Base Activity

~~~java
public class FirstActivity extends BaseActivity {
    private AutoPageLayout autoPageLayout;

    private void initAutoPageLayout() {
        autoPageLayout = new AutoPageLayout.Builder(this)
                .setTarget(FirstActivity.this)
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
}
~~~



Base Fragment

- Must be called after onCreateView
- ViewPager + Fragment ，You can't use **setTarget(Fragment)**

```java
public class SecondFragment extends BaseFragment {
    private AutoPageLayout autoPageLayout;

    // Must be called after onCreateView
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
}
```



# Interface Explain

AutoPageLayout.Builder

|                        方法名                        |                             备注                             |
| :--------------------------------------------------: | :----------------------------------------------------------: |
|                 setTarget(Activity)                  |              基于Activity，以此控件作为内容布局              |
|                 setTarget(Fragment)                  |              基于Fragment，以此控件作为内容布局              |
|                   setTarget(View)                    |                基于View，以此控件作为内容布局                |
|                showType(int showType)                | 初始化显示哪种布局，默认是SHOW_TYPE_CONTENT。五种方式：SHOW_TYPE_CONTENT（内容布局）、SHOW_TYPE_EMPTY（空布局）、SHOW_TYPE_LOADING（加载中布局）、SHOW_TYPE_ERROR（错误布局）、SHOW_TYPE_CUSTOM（自定义布局） |
|  setEmptyLayout(int layoutResId, OnViewAddListener)  |      设置空布局，添加成功OnViewAddListener回调onCreate       |
| setLoadingLayout(int layoutResId, OnViewAddListener) |    设置加载中布局，添加成功OnViewAddListener回调onCreate     |
|  setErrorLayout(int layoutResId, OnViewAddListener)  |     设置错误布局，添加成功OnViewAddListener回调onCreate      |
| setCustomLayout(int layoutResId, OnViewAddListener)  |    设置自定义布局，添加成功OnViewAddListener回调onCreate     |
|       setEmptyShowListener(OnViewShowListener)       | 空布局显示或者隐藏的时候，OnViewShowListener回调onShow告诉外界此时的状态，但是发生状态变化才会 |
|      setLoadingShowListener(OnViewShowListener)      | 加载中布局显示或者隐藏的时候，OnViewShowListener回调onShow告诉外界此时的状态，但是发生状态变化才会 |
|       setErrorShowListener(OnViewShowListener)       | 错误布局显示或者隐藏的时候，OnViewShowListener回调onShow告诉外界此时的状态，但是发生状态变化才会 |
|      setCustomShowListener(OnViewShowListener)       | 自定义布局显示或者隐藏的时候，OnViewShowListener回调onShow告诉外界此时的状态，但是发生状态变化才会 |
|                       build()                        |                  最后调用创建AutoPageLayout                  |



AutoPageLayout

|    方法名     |      备注      |
| :-----------: | :------------: |
| showContent() |  显示内容布局  |
|  showEmpty()  |   显示空布局   |
| showLoading() | 显示加载中布局 |
|  showError()  |  显示错误布局  |
| showCustom()  | 显示自定义布局 |



# Author
