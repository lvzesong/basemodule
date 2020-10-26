package com.base.basemodule;

import androidx.fragment.app.Fragment;

/**
 * create by lzs 2020-09-27 10:40
 * email:343067508@qq.com
 * description: Androidx fragment 懒加载  配合MaxLifecyclePagerAdapter使用
 **/
public abstract class XLazyFragment extends Fragment {
    private boolean isLoad;

    @Override
    public void onResume() {
        super.onResume();
        tryLoad();
    }

    private void tryLoad() {
        if (!isLoad) {
            lazyData();
            isLoad = true;
        }
    }

    /**
     * 懒加载
     */
    public abstract void lazyData();
}
