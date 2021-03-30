package com.base.basemodule.net;
/**
 * Created by lzs on 2017/5/8 10:41
 * E-Mail Address：343067508@qq.com
 */

import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ApiException extends RuntimeException {
    private String msg;

    public ApiException(String message) {
        super(message);
        msg = message;

    }

    public ApiException(Throwable e) {
        super();
        try {
            if (e instanceof ApiException) {
                code = ((ApiException) e).getCode();
                msg = e.getMessage();
            } else if (e instanceof ConnectException) {
                msg = "Network error, please try again later";
            } else if (e instanceof UnknownHostException) {
                msg = "Please check the network status";
            } else if (e instanceof SocketException) {
                msg = "Network error, please try again later";
            } else if (e instanceof SocketTimeoutException) {
                msg = "Connection timeout, please try again later";
            } else if (e instanceof IOException) {
                msg = "Connection timeout, please try again later";
            } else if (e instanceof ParseException) {
                msg = "Network error, please try again later";
            } else if (e instanceof XmlPullParserException) {
                msg = "Network error, please try again later";
            } else if (e instanceof HttpException) {
                msg = "Network error, please try again later";
            } else if (e instanceof JSONException || e instanceof JsonSyntaxException) {
                Log.e("lzs", "json 解析异常:" + e.getMessage());
                msg = "Data parsing exception";
            } else {
                if (e == null || TextUtils.isEmpty(e.getMessage())) {
                    msg = "Unknown error";
                } else {
                    msg = e.getMessage();
                }
                // Log.e("lzs", "其他错误，对用户隐藏:" + e.getMessage() + "    " + e.getClass().getSimpleName() + "  " + e.getLocalizedMessage());
            }
        } catch (Exception e1) {

        }
        //e.printStackTrace();
    }

    int code;

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return msg;
    }

}
