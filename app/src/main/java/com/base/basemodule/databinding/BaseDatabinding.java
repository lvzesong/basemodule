package com.base.basemodule.databinding;

import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;

import com.base.basemodule.utils.ScreenUtil;

public class BaseDatabinding {
    //设置标题栏高度，加上状态栏高度
    @BindingAdapter(value = {"statusbar_height"})
    public static void setStatusbarHeight(View view, boolean flag) {
        if (flag) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int statusbarHeight = ScreenUtil.getStatusHeight(view.getContext());
            params.height += statusbarHeight;
            view.setLayoutParams(params);
            view.setPadding(view.getPaddingLeft(), view.getTop() + statusbarHeight, view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    @BindingAdapter(value = {"statusbar_margin"})
    public static void setStatusbarHeightMargin(View view, boolean flag) {
        if (flag) {
            try {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                int statusbarHeight = ScreenUtil.getStatusHeight(view.getContext());
                params.topMargin += statusbarHeight;
                view.setLayoutParams(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
