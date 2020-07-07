package com.base.basemodule.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lzs on 2018/11/26 15:18
 * E-Mail Address：343067508@qq.com
 */
public class EmojiFilter implements InputFilter {

    //关键的正则表达式
    Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udd00-\ud83e\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            //Toast.makeText(mContext, "不支持Emoji输入", Toast.LENGTH_SHORT).show();
            return "";
        }
        return null;
    }
}



