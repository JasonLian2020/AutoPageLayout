[![Download](https://img.shields.io/badge/Download-AutoPageLayout-brightgreen.svg?style=flat)](https://bintray.com/jasonlian/maven/auto-page-layout)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://www.travis-ci.org/tomUp/AutoPageLayout.svg?branch=master)](https://www.travis-ci.org/tomUp/AutoPageLayout)

# AutoPageLayout



# Download

Get the latest artifact via gradle
```groovy
implementation 'me.jason.library:auto-page-layout:1.0.1'
```

# Usage

Base Activity

~~~java
public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ``````
        initAutoPageLayout();
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
~~~



# Author
