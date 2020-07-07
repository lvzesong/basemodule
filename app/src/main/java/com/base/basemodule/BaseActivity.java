package com.base.basemodule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.base.basemodule.net.logger.Logutil;
import com.base.basemodule.utils.FixMemLeak;
import com.base.basemodule.utils.MyStatusBarUtil;

import com.base.basemodule.utils.ScreenUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import java.util.ArrayList;

/**
 * Created by lzs on 2017/6/18 14:22
 * E-Mail Address：343067508@qq.com
 * 基本原则 view 层不保存数据
 */
public abstract class BaseActivity extends RxAppCompatActivity implements Presenter {
    protected ProgressDialog progressDialog;
    protected Context mContext;

    /**
     * 退出应用
     */
    protected void killApp() {
        LiveEventBus.get(suicide_key, String.class).post("");
    }

    private final static String suicide_key = "suicide_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRequestedOrientationPotrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        ARouter.getInstance().inject(this);
        //透明状态栏
        MyStatusBarUtil.transparencyBar(this);
        if (darkMode()) {
            MyStatusBarUtil.StatusBarLightMode(this);
        }
//
        LiveEventBus.get(suicide_key, String.class).observeForever(killObserver);
//
        progressDialog = new ProgressDialog(this);
        mContext = this;
    }

    Observer<String> killObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            finish();
        }
    };


    // layout_header
    protected void setHeaderPadding(ViewGroup header) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) header.getLayoutParams();
        int h = ScreenUtil.getStatusHeight(mContext);
        params.height = params.height + h;
        header.setLayoutParams(params);
        header.setPadding(0, h, 0, 0);
    }

    //是否强制竖屏
    protected boolean isRequestedOrientationPotrait() {
        return true;
    }


    //true将状态栏字体和图标颜色改为深色，false默认为白色
    protected boolean darkMode() {
        return true;
    }

    //设置顶部第一个布局内边距为状态栏高度
    protected void setStatusBarPadding(View view) {
        view.setPadding(0, ScreenUtil.getStatusHeight(mContext), 0, 0);
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        LiveEventBus.get(suicide_key, String.class).removeObserver(killObserver);
        FixMemLeak.fixLeak(this);
        super.onDestroy();
//        if (mImmersionBar != null)
//            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }


    protected void showProgress() {
        progressDialog.show();
    }

    protected void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void showProgress(String msg, boolean cancelable) {
        progressDialog.setCanceledOnTouchOutside(cancelable);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }


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


    //private static final int BACK_MODE_2COUNT = 1;
//    private static final int BACK_MODE_DIALOG = 2;
    private boolean exitByCount = false;

    protected void setExitByCount(boolean flag) {
        exitByCount = flag;
    }

    //    // 监听返回键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (exitByCount) {
                exitByCount();
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    //
    private int backCount;//返回键按下次数

    // 通过连续按两次退出程序
    private void exitByCount() {
        backCount++;
        if (backCount >= 2) {
            killApp();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.return_tips), Toast.LENGTH_SHORT).show();
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backCount = 0;
                }
            }.start();
        }
    }

    protected void shortToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void longToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    protected void route(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    protected void routeForResult(Activity activity, String path, int request_code) {
        ARouter.getInstance().build(path).navigation(activity, request_code);
    }

    protected <T extends Parcelable> void routeWithParcelableForResult(Activity activity, String path, int request_code, String key, T parcelable) {
        ARouter.getInstance().build(path).withParcelable(key, parcelable).navigation(activity, request_code);
    }

    protected void routeWithString(String path, String key, String value) {
        ARouter.getInstance().build(path).withString(key, value).navigation();
    }

    protected void routeWithAction(String path, String action) {
        ARouter.getInstance().build(path).withAction(action).navigation();
    }

    protected void routeWithInt(String path, String key, int value) {
        ARouter.getInstance().build(path).withInt(key, value).navigation();
    }

    protected void routeWithLong(String path, String key, long value) {
        ARouter.getInstance().build(path).withLong(key, value).navigation();
    }

    protected void routeWithParcelable(String path, String key, Parcelable value) {
        ARouter.getInstance().build(path).withParcelable(key, value).navigation();
    }

    protected void routeWithParcelableArrayList(String path, String key, ArrayList<? extends Parcelable> value) {
        ARouter.getInstance().build(path).withParcelableArrayList(key, value).navigation();
    }

    protected void routeWithParcelableArrayListForResult(Activity activity, int requestCode, String path, String key, ArrayList<? extends Parcelable> value) {
        ARouter.getInstance().build(path).withParcelableArrayList(key, value).navigation(activity, requestCode);
    }

    protected void routeWithParcelableArrayListAction(String path, String key, ArrayList<? extends Parcelable> value, String action) {
        ARouter.getInstance().build(path).withParcelableArrayList(key, value).withAction(action).navigation();
    }

    protected void routeWithBoolean(String path, String key, boolean value) {
        ARouter.getInstance().build(path).withBoolean(key, value).navigation();
    }


    protected Fragment getRouteFragment(String path) {
        // 获取Fragment
        Fragment fragment = (Fragment) ARouter.getInstance().build(path).navigation();
        return fragment;
    }


    protected Fragment getRouteFragment(Context context, String path) {
        // 获取Fragment
        Fragment fragment = (Fragment) ARouter.getInstance().build(path).navigation(context, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
                Log.e("lzs", "onFound:");
            }

            @Override
            public void onLost(Postcard postcard) {
                Log.e("lzs", "onLost:");
            }

            @Override
            public void onArrival(Postcard postcard) {
                Log.e("lzs", "onArrival:");
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                Log.e("lzs", "onInterrupt:");
            }
        });
        return fragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
