package com.base.basemodule;


import android.content.Context;
import android.content.res.Configuration;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.basemodule.config.AppConfig;
import com.base.basemodule.net.logger.Logutil;


/**
 * Created by lzs on 2017/6/18 14:30
 * E-Mail Address：343067508@qq.com
 */
public abstract class BaseApplication extends MultiDexApplication implements IAppInit {
    public static BaseApplication myApplication;

    public static BaseApplication getApplication() {
        return myApplication;
    }

    public abstract AppConfig createAppConfig();


    public AppConfig getAppConfig() {
        return appConfig;
    }

    private AppConfig appConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        appConfig = createAppConfig();
        initArouter();
        Logutil.init(BuildConfig.DEBUG, BuildConfig.LOG_TAG);
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        synInit(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void asynInit(Context context) {
        //autoSize();
    }

    private void initArouter() {
        if (BuildConfig.DEBUG) {
            //ARouter.setLogger();
            ARouter.openDebug();
            ARouter.openLog();
        } else {

        }
        ARouter.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    @Override
    public void onTerminate() {
        //销毁释放资源
        ARouter.getInstance().destroy();
        super.onTerminate();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


}
