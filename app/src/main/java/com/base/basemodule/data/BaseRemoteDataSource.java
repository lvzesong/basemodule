package com.base.basemodule.data;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import com.base.basemodule.di.serviceImpl.RetrofitServiceImpl;


import retrofit2.Retrofit;

/**
 * Created by lzs on 2017/6/18 14:08
 * E-Mail Address：343067508@qq.com
 */
public class BaseRemoteDataSource {

    @Autowired(name = "base/yourservicegroupname/hello")
    RetrofitServiceImpl retrofitService;
    protected Retrofit retrofit;

    public BaseRemoteDataSource() {
        ARouter.getInstance().inject(this);
        retrofit = retrofitService.getRetrofit();
    }

}
