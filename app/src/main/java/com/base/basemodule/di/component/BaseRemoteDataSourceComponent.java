package com.base.basemodule.di.component;
/**
 * Created by lzs on 2017/6/18 16:28
 * E-Mail Address：343067508@qq.com
 */
import com.base.basemodule.data.BaseRemoteDataSource;
import com.base.basemodule.di.modules.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class})
public interface BaseRemoteDataSourceComponent {

    void inject(BaseRemoteDataSource baseRemoteDataSource);

}
