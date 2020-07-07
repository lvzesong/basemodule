package com.base.basemodule.di.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

import retrofit2.Retrofit;

/**
 * Created by lzs on 2019/5/10.
 * E-Mail Address：343067508@qq.com
 **/
public interface IRetrofitService extends IProvider {
    Retrofit getRetrofit();
}
