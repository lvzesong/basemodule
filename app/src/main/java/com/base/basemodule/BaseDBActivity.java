package com.base.basemodule;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.base.basemodule.data.DataStatus;
import com.base.basemodule.data.Status;
/**
 * Created by lzs on 2017/6/18 14:20
 * E-Mail Address：343067508@qq.com
 */
public abstract class BaseDBActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends BaseActivity {
    @LayoutRes
    protected abstract int getLayoutRes();

    protected DB dataBinding;
    protected VM viewModel;


    protected abstract Class<VM> getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        dataBinding.setLifecycleOwner(this);
        if (isSetStatusBarPadding()) {
            setStatusBarPadding(dataBinding.getRoot());
        }
        viewModel =  getDefaultViewModelProviderFactory().create(getViewModel());
        //观察更改progressbar状态
        viewModel.getProgressEvent().observe(this, new Observer<DataStatus<String>>() {
            @Override
            public void onChanged(@Nullable DataStatus<String> stringProgressStatus) {
                if (stringProgressStatus.status == Status.LOADING) {
                    showProgress(TextUtils.isEmpty(stringProgressStatus.data) ? "" : stringProgressStatus.data);
                } else if (stringProgressStatus.status == Status.HIDE) {
                    hideProgress();
                }
            }
        });
        setupDataBinding();
        initView(dataBinding.getRoot());
    }

    protected abstract void setupDataBinding();

    protected abstract void initView(View view);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            viewModel.handleActivityResult(requestCode, resultCode, data);
        }
    }

    protected boolean isSetStatusBarPadding() {
        return false;
    }

}
