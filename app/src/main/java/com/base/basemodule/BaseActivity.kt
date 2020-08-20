package com.base.basemodule

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.base.basemodule.net.logger.Logutil
import com.base.basemodule.utils.FixMemLeak
import com.base.basemodule.utils.MyStatusBarUtil
import com.base.basemodule.utils.ScreenUtil
import com.jeremyliao.liveeventbus.LiveEventBus
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by lzs on 2017/6/18 14:22
 * E-Mail Address：343067508@qq.com
 * 基本原则 view 层不保存数据
 */
abstract class BaseActivity : RxAppCompatActivity(), Presenter {
    companion object {
        private const val suicide_key = "suicide_key"
        private const val TAG: String = "BaseActivity";
    }

    protected var progressDialog: ProgressDialog? = null

    @JvmField
    protected var mContext: Context? = null

    /**
     * 退出应用
     */
    protected fun killApp() {
        LiveEventBus.get(suicide_key, String::class.java).post("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isRequestedOrientationPotrait) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        ARouter.getInstance().inject(this)
        //透明状态栏
        MyStatusBarUtil.transparencyBar(this)
        if (darkMode()) {
            MyStatusBarUtil.StatusBarLightMode(this)
        }
        //
        LiveEventBus.get(suicide_key, String::class.java).observeForever(killObserver)
        //
        progressDialog = ProgressDialog(this)
        mContext = this
    }

    val killObserver: Observer<String> = Observer<String> { finish() }


    //是否强制竖屏
    protected val isRequestedOrientationPotrait: Boolean
        protected get() = true

    //true将状态栏字体和图标颜色改为深色，false默认为白色
    protected fun darkMode(): Boolean {
        return true
    }

    //设置顶部第一个布局内边距为状态栏高度
    protected fun setStatusBarPadding(view: View) {
        view.setPadding(0, ScreenUtil.getStatusHeight(mContext), 0, 0)
    }

    override fun onDestroy() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        LiveEventBus.get(suicide_key, String::class.java).removeObserver(killObserver)
        FixMemLeak.fixLeak(this)
        super.onDestroy()
        //        if (mImmersionBar != null)
//            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    protected fun showProgress() {
        progressDialog!!.show()
    }

    protected fun showProgress(msg: String?) {
        progressDialog!!.setMessage(msg)
        progressDialog!!.show()
    }

    protected fun showProgress(msg: String?, cancelable: Boolean) {
        progressDialog!!.setCanceledOnTouchOutside(cancelable)
        progressDialog!!.setMessage(msg)
        progressDialog!!.show()
    }

    protected fun hideProgress() {
        if (progressDialog!!.isShowing) {
            progressDialog!!.hide()
        }
    }

    protected fun logd(msg: String?) {
        Logutil.d(msg)
    }

    protected fun loge(msg: String?) {
        Logutil.e(msg, null)
    }

    protected fun loge(msg: String?, e: Throwable?) {
        Logutil.e(msg, e)
    }

    protected fun logi(msg: String?) {
        Logutil.i(msg)
    }

    protected fun logjson(json: String?) {
        Logutil.json(json)
    }

    //private static final int BACK_MODE_2COUNT = 1;
    //    private static final int BACK_MODE_DIALOG = 2;
//    private var exitByCount = false
//    protected fun setExitByCount(flag: Boolean) {
//        exitByCount = flag
//    }

    //    // 监听返回键
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_BACK) {
//            if (exitByCount) {
//                exitByCount()
//                return false
//            }
//        }
//        return super.dispatchKeyEvent(event)
//    }

    //
    //  private var backCount = 0 //返回键按下次数 = 0

    // 通过连续按两次退出程序
//    private fun exitByCount() {
//        backCount++
//        if (backCount >= 2) {
//            killApp()
//        } else {
//            Toast.makeText(applicationContext, resources.getString(R.string.return_tips), Toast.LENGTH_SHORT).show()
//            GlobalScope.launch {
//                delay(2000);
//                backCount = 0
//            }
////            object : Thread() {
////                override fun run() {
////                    try {
////                        sleep(2000)
////                    } catch (e: InterruptedException) {
////                        e.printStackTrace()
////                    }
////                    backCount = 0
////                }
////            }.start()
//        }
//    }

    protected fun shortToast(msg: String?) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toast.makeText(mContext!!.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun longToast(msg: String?) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toast.makeText(mContext!!.applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    protected fun route(path: String?) {
        ARouter.getInstance().build(path).navigation()
    }

    protected fun routeForResult(activity: Activity?, path: String?, request_code: Int) {
        ARouter.getInstance().build(path).navigation(activity, request_code)
    }

    protected fun <T : Parcelable?> routeWithParcelableForResult(activity: Activity?, path: String?, request_code: Int, key: String?, parcelable: T) {
        ARouter.getInstance().build(path).withParcelable(key, parcelable).navigation(activity, request_code)
    }

    protected fun routeWithString(path: String?, key: String?, value: String?) {
        ARouter.getInstance().build(path).withString(key, value).navigation()
    }

    protected fun routeWithAction(path: String?, action: String?) {
        ARouter.getInstance().build(path).withAction(action).navigation()
    }

    protected fun routeWithInt(path: String?, key: String?, value: Int) {
        ARouter.getInstance().build(path).withInt(key, value).navigation()
    }

    protected fun routeWithLong(path: String?, key: String?, value: Long) {
        ARouter.getInstance().build(path).withLong(key, value).navigation()
    }

    protected fun routeWithParcelable(path: String?, key: String?, value: Parcelable?) {
        ARouter.getInstance().build(path).withParcelable(key, value).navigation()
    }

    protected fun routeWithParcelableArrayList(path: String?, key: String?, value: ArrayList<out Parcelable?>?) {
        ARouter.getInstance().build(path).withParcelableArrayList(key, value).navigation()
    }

    protected fun routeWithParcelableArrayListForResult(activity: Activity?, requestCode: Int, path: String?, key: String?, value: ArrayList<out Parcelable?>?) {
        ARouter.getInstance().build(path).withParcelableArrayList(key, value).navigation(activity, requestCode)
    }

    protected fun routeWithParcelableArrayListAction(path: String?, key: String?, value: ArrayList<out Parcelable?>?, action: String?) {
        ARouter.getInstance().build(path).withParcelableArrayList(key, value).withAction(action).navigation()
    }

    protected fun routeWithBoolean(path: String?, key: String?, value: Boolean) {
        ARouter.getInstance().build(path).withBoolean(key, value).navigation()
    }

    protected fun getRouteFragment(path: String?): Fragment {
        // 获取Fragment
        return ARouter.getInstance().build(path).navigation() as Fragment
    }

    protected fun getRouteFragment(context: Context?, path: String?): Fragment {
        // 获取Fragment
        return ARouter.getInstance().build(path).navigation(context, object : NavigationCallback {
            override fun onFound(postcard: Postcard) {
                Log.e(TAG, "onFound:")
            }

            override fun onLost(postcard: Postcard) {
                Log.e(TAG, "onLost:")
            }

            override fun onArrival(postcard: Postcard) {
                Log.e(TAG, "onArrival:")
            }

            override fun onInterrupt(postcard: Postcard) {
                Log.e(TAG, "onInterrupt:")
            }
        }) as Fragment
    }

}