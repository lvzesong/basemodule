package com.base.basemodule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.basemodule.data.DataStatus;
import com.base.basemodule.data.Status;
import com.base.basemodule.utils.ScreenUtil;
import com.trello.rxlifecycle4.components.support.RxFragment;

import java.util.ArrayList;

/**
 * Created by lzs on 2017/6/18 14:11
 * E-Mail Address：343067508@qq.com
 */
public abstract class BaseDBFragment<VM extends BaseViewModel, BD extends ViewDataBinding> extends RxFragment implements Presenter {

    private static final String TAG = "BaseDBFragment";
    protected Context mContext;

//    protected BackPressHandler mBackPressHandler;
//
//    public interface BackPressHandler {
//
//        public abstract void setSelectedFragment(Fragment selectedFragment);
//    }

    protected VM viewModel;

    protected BD dataBinding;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;
    protected ProgressDialog progressDialog;

    protected abstract Class<VM> getViewModel();

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onCreate");
        }
        if (isBindLifeWithActivity()) {
            viewModel = new ViewModelProvider(getActivity()).get(getViewModel());
        } else {
            viewModel = new ViewModelProvider(this).get(getViewModel());
        }

    }

    /**
     * viewModel 生命周期是否和 activity 一致,和 activity 生命周期一致可以通过 viewmodel 传递数据
     *
     * @return
     */
    protected boolean isBindLifeWithActivity() {
        return false;
    }

    //吸取 View的颜色，并将状态栏设置成该颜色
//    protected Bitmap paletteWithSatusBarBitmap(){
//        return null;
//    }
//    protected void paletteWithSatusBar(Bitmap bitmap){
//
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                Palette.Swatch vibrant = palette.getVibrantSwatch();
//                if (vibrant == null) {
//                    for (Palette.Swatch swatch : palette.getSwatches()) {
//                        vibrant = swatch;
//                        break;
//                    }
//                }
//                // 这样获取的颜色可以进行改变。
//                int rbg = vibrant.getRgb();
//                if (Build.VERSION.SDK_INT > 21) {
//                    Window window = getActivity().getWindow();
//                    //状态栏改变颜色。
//                    int color = changeColor(rbg);
//                    window.setStatusBarColor(color);
//                }
//            }
//        });
//
//
//    }
//    private int changeColor(int rgb) {
//        int red = rgb >> 16 & 0xFF;
//        int green = rgb >> 8 & 0xFF;
//        int blue = rgb & 0xFF;
//        red = (int) Math.floor(red * (1 - 0.2));
//        green = (int) Math.floor(green * (1 - 0.2));
//        blue = (int) Math.floor(blue * (1 - 0.2));
//        return Color.rgb(red, green, blue);
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "setUserVisibleHint  isVisibleToUser:" + isVisibleToUser + "  isViewCreated:" + isViewCreated);
        }
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        //Logutil.e("setUserVisibleHint:"+isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onViewCreated");
        }
        isViewCreated = true;
        //懒加载
        lazyLoad();
    }

    protected abstract void loadData();

    private void lazyLoad() {
        //Logutil.e("isViewCreated "+isViewCreated+" isUIVisible:"+isUIVisible);
        if (isViewCreated && isUIVisible) {
            //Logutil.e("lazyLoad");
            if (loadDataOnce) {
                loadData();
                loadDataOnce = false;
            }
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    private boolean loadDataOnce = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onCreateView");
        }
        if (dataBinding == null) {
            mContext = getActivity();
            dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
            dataBinding.setLifecycleOwner(this);
//            if (isSetStatusBarPadding()) {
//                setStatusBarPadding(dataBinding.getRoot());
//            }
            progressDialog = new ProgressDialog(getActivity());
            viewModel.getProgressEvent().observe(getViewLifecycleOwner(), new Observer<DataStatus<String>>() {
                @Override
                public void onChanged(@Nullable DataStatus<String> stringProgressStatus) {
                    if (stringProgressStatus.status == Status.LOADING) {
                        showProgress(TextUtils.isEmpty(stringProgressStatus.data) ? "" : stringProgressStatus.data);
                    } else if (stringProgressStatus.status == Status.HIDE) {
                        hideProgress();
                    }
                }
            });
            setupDatabinding();
            // rootView  = inflater.inflate(getLayoutRes(),null);
            initView(dataBinding.getRoot());

        }
        return dataBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onPause");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onResume");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onDestroyView");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onActivityCreated");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onActivityCreated");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onAttach context");
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onAttach activity");
        }
    }


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onAttachFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onDetach");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //Log.e(TAG, "onHiddenChanged:" + hidden);
    }

//    //设置顶部第一个布局内边距为状态栏高度
//    protected void setStatusBarPadding(View view) {
//        view.setPadding(0, ScreenUtil.getStatusHeight(mContext), 0, 0);
//    }
//
//    protected boolean isSetStatusBarPadding() {
//        return false;
//    }

    protected <T extends Activity> void startActivity(Class<T> activityClass) {
        Intent intent = new Intent(mContext, activityClass);

        if (Build.VERSION.SDK_INT >= 20) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.activity_right_in, R.anim.activity_right_out);
            ActivityCompat.startActivity(mContext, intent, compat.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void startActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= 20) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.activity_right_in, R.anim.activity_right_out);
            ActivityCompat.startActivity(mContext, intent, compat.toBundle());
        } else {
            startActivity(intent);
        }
    }


    protected abstract void setupDatabinding();

    protected abstract void initView(View view);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.handleActivityResult(requestCode, resultCode, data);
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

    public void setLoadDataOnce(boolean loadDataOnce) {
        this.loadDataOnce = loadDataOnce;
    }


    protected void route(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    protected void routeForResult(Activity activity, String path, int request_code) {
        ARouter.getInstance().build(path).navigation(activity, request_code);
    }

    protected void routeWithAction(String path, String action) {
        ARouter.getInstance().build(path).withAction(action).navigation();
    }

    protected void routeWithString(String path, String key, String value) {
        ARouter.getInstance().build(path).withString(key, value).navigation();
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

    protected void routeWithBoolean(String path, String key, boolean value) {
        ARouter.getInstance().build(path).withBoolean(key, value).navigation();
    }


}
