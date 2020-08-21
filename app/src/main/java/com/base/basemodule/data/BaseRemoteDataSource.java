package com.base.basemodule.data;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import com.base.basemodule.di.component.BaseRemoteDataSourceComponent;
import com.base.basemodule.di.component.DaggerBaseRemoteDataSourceComponent;
import com.base.basemodule.di.modules.RetrofitModule;
import com.base.basemodule.di.serviceImpl.RetrofitServiceImpl;


import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by lzs on 2017/6/18 14:08
 * E-Mail Address：343067508@qq.com
 */
public class BaseRemoteDataSource {


    @Inject
    protected Retrofit retrofit;

    public BaseRemoteDataSource() {
        //注入
        BaseRemoteDataSourceComponent component = DaggerBaseRemoteDataSourceComponent.builder().retrofitModule(new RetrofitModule()).build();
        component.inject(this);
    }

}
