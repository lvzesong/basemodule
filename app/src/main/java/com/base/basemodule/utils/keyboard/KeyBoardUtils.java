package com.base.basemodule.utils.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;


/**
 * by lzsong on 2018/8/20 18:55
 */
public class KeyBoardUtils {


    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏软键盘 //
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void showKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //显示软键盘
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }


    public static void switchKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //切换软键盘的显示与隐藏
        imm.toggleSoftInputFromWindow(view.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static class KeyboardStatusDetector {
        private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 100;
        private KeyboardVisibilityListener mVisibilityListener;
        ViewTreeObserver.OnGlobalLayoutListener viewTreeL = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(view == null){
                    return;
                }
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);

                int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
                //Log.e("lzs","heightDiff:"+heightDiff);
                if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) { // if more than 100 pixels, its probably a keyboard...
                    if (!keyboardVisible) {
                        keyboardVisible = true;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(true,heightDiff);
                        }
                    }
                } else {
                    if (keyboardVisible) {
                        keyboardVisible = false;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(false,heightDiff);
                        }
                    }
                }
            }
        };
        boolean keyboardVisible = false;

        public KeyboardStatusDetector registerFragment(Fragment f) {
            registerView(f.getActivity().getWindow().getDecorView().findViewById(android.R.id.content));
            return this;
        }

        public KeyboardStatusDetector registerActivity(Activity a) {
            registerView(a.getWindow().getDecorView().findViewById(android.R.id.content));
            return this;
        }

        private View view;

        public KeyboardStatusDetector registerView(final View v) {
            view = v;
            v.getViewTreeObserver().addOnGlobalLayoutListener(viewTreeL);

            return this;
        }

        public KeyboardStatusDetector setmVisibilityListener(KeyboardVisibilityListener listener) {
            mVisibilityListener = listener;
            return this;
        }

        public void unRegister(Activity activity) {
            activity.getWindow().getDecorView().findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeL);
        }

        public void unRegister(Fragment fragment) {
            fragment.getActivity().getWindow().getDecorView().findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeL);
        }

        public void unRegister(View view) {
            if(view!=null){
                view.getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeL);
            }
        }

    }

    public interface KeyboardVisibilityListener {
        void onVisibilityChanged(boolean keyboardVisible,int keyBoardHeight);
    }

}
