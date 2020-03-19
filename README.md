[![Download](https://img.shields.io/badge/Download-AutoPageLayout-brightgreen.svg?style=flat)](https://bintray.com/jasonlian/maven/auto-page-layout)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://www.travis-ci.org/tomUp/AutoPageLayout.svg?branch=master)](https://www.travis-ci.org/tomUp/AutoPageLayout)

# AutoPageLayout

> 我们在开发过程中，会遇到很多需要切换页面（空页面、错误页面、加载页面）的情况，为此写了很多涉及切换页面逻辑的代码在项目中，这些逻辑代码可能会影响到主页面（内容页面），迭代中也会比较难维护。
>
> 主页面逻辑与这些页面逻辑不应该耦合在一起，需要写得更解耦，为此设计该库。

![Base Activity](https://github.com/tomUp/AutoPageLayout/blob/master/image/Base%20Activity.jpg?raw=true)

![Base Fragment](https://github.com/tomUp/AutoPageLayout/blob/master/image/Base%20Fragment.jpg?raw=true)

![Base View](https://github.com/tomUp/AutoPageLayout/blob/master/image/Base%20View.jpg?raw=true)

## Download

Get the latest artifact via gradle
```groovy
implementation 'me.jason.library:auto-page-layout:1.1.0'
```

## Usage

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



Base View

```java
    @BindView(R.id.contentView)
    TextView contentView;

    private AutoPageLayout autoPageLayout;

    private void initAutoPageLayout() {
        autoPageLayout = new AutoPageLayout.Builder(this)
                .setTarget(contentView)
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
```



See more，[Demo](https://github.com/tomUp/AutoPageLayout/tree/master/app)



## ChangeLog

See more，[ChangeLog](https://github.com/tomUp/AutoPageLayout/releases)



## Interface Explain

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

|     方法名      |            备注            |
| :-------------: | :------------------------: |
|  showContent()  |        显示内容布局        |
|   showEmpty()   |         显示空布局         |
|  showLoading()  |       显示加载中布局       |
|   showError()   |        显示错误布局        |
|  showCustom()   |       显示自定义布局       |
| isShowContent() |  判断当前显示是否内容布局  |
|  isShowEmpty()  |   判断当前显示是否空布局   |
| isShowLoading() | 判断当前显示是否加载中布局 |
|  isShowError()  |  判断当前显示是否错误布局  |
| isShowCustom()  | 判断当前显示是否自定义布局 |



## Author
