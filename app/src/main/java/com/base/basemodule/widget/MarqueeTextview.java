package com.base.basemodule.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * by lzsong on 2018/9/7 10:18
 */
public class MarqueeTextview extends AppCompatTextView {

    public MarqueeTextview(Context context) {
        super(context);
    }

    public MarqueeTextview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused(){
        return true;
    }
}
