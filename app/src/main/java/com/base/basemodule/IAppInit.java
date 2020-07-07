package com.base.basemodule;
/**
 * Created by lzs on 2017/6/18 14:08
 * E-Mail Address：343067508@qq.com
 */
import android.content.Context;
//app 初始化接口
public interface IAppInit {
    //同步初始化
    void synInit(Context context);
    //异步线程初始化  推迟到开屏页或者欢迎页(app 启动的第一个页面)做初始化
    void asynInit(Context context);
}
