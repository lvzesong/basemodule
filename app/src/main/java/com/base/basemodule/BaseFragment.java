package com.base.basemodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.base.basemodule.net.logger.Logutil;
import com.trello.rxlifecycle4.components.support.RxFragment;

/**
 * Created by lzs on 2017/6/18 13:13
 * E-Mail Address：343067508@qq.com
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends RxFragment {
    protected VM viewModel;
    protected Context mContext;


    protected abstract Class<VM> getViewModel();

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(getViewModel());
    }

    public View getRootView() {
        return rootView;
    }

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(getLayoutRes(), null);
        initView(rootView);
        return rootView;
    }

    protected <T extends Activity> void startActivity(Class<T> activityClass) {
        Intent intent = new Intent(mContext, activityClass);

        if (Build.VERSION.SDK_INT >= 20) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.activity_right_in, R.anim.activity_right_out);
            ActivityCompat.startActivity(mContext, intent, compat.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    protected void shortToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isLoad;

    @Override
    public void onResume() {
        super.onResume();
        tryLoad();
    }

    private void tryLoad() {
        if (!isLoad) {
            lazyData();
            isLoad = true;
        }
    }

    /**
     * 懒加载
     */
    public abstract void lazyData();

    protected void longToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected abstract void initView(View view);

    protected void logd(String msg) {
        Logutil.d(msg);
    }

    protected void loge(String msg) {
        Logutil.e(msg, null);
    }

    protected void loge(String msg, Throwable e) {
        Logutil.e(msg, e);
    }

    protected void logi(String msg) {
        Logutil.i(msg);
    }

    protected void logjson(String json) {
        Logutil.json(json);
    }
}
