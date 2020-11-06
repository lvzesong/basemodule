package com.base.basemodule.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.base.basemodule.data.Status.EMPTY;
import static com.base.basemodule.data.Status.ERROR;
import static com.base.basemodule.data.Status.HIDE;
import static com.base.basemodule.data.Status.LOADING;
import static com.base.basemodule.data.Status.SUCCESS;

/**
 * Created by lzs on 2017/6/18 14:12
 * E-Mail Address：343067508@qq.com
 */
//a generic class that describes a data with a status
public class DataStatus<T> {
    public int code;
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private DataStatus(@NonNull Status status, @Nullable T data, @Nullable String message, int code) {
        this.status = status;
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> DataStatus<T> success(@NonNull T data) {
        return new DataStatus<>(SUCCESS, data, null, 0);
    }

    public static <T> DataStatus<T> success(@NonNull T data, @NonNull String msg) {
        return new DataStatus<>(SUCCESS, data, msg, 0);
    }

    public static <T> DataStatus<T> success(@NonNull T data, @NonNull String msg, int code) {
        return new DataStatus<>(SUCCESS, data, msg, code);
    }

    public static <T> DataStatus<T> success(@NonNull String msg) {
        return new DataStatus<>(SUCCESS, null, msg, 0);
    }

    public static <T> DataStatus<T> success(@NonNull String msg, int code) {
        return new DataStatus<>(SUCCESS, null, msg, code);
    }


    public static <T> DataStatus<T> error(@NonNull T data) {
        return new DataStatus<>(ERROR, data, null, 0);
    }

    public static <T> DataStatus<T> error(@NonNull T data, @NonNull String msg) {
        return new DataStatus<>(ERROR, data, msg, 0);
    }

    public static <T> DataStatus<T> error(@NonNull String msg) {
        return new DataStatus<>(ERROR, null, msg, 0);
    }

    public static <T> DataStatus<T> error(@NonNull String msg, int code) {
        return new DataStatus<>(ERROR, null, msg, code);
    }

    public static <T> DataStatus<T> empty(@NonNull T data) {
        return new DataStatus<>(EMPTY, data, null, 0);
    }

    public static <T> DataStatus<T> empty(@NonNull T data, @NonNull String msg) {
        return new DataStatus<>(EMPTY, data, msg, 0);
    }

    public static <T> DataStatus<T> empty(@NonNull String msg) {
        return new DataStatus<>(EMPTY, null, msg, 0);
    }


//    public static <T> DataStatus<T> loading(@NonNull T data) {
//        return new DataStatus<>(LOADING, data, null);
//    }

    public static <T> DataStatus<T> loading(@NonNull T data, @NonNull String msg) {
        return new DataStatus<>(LOADING, data, msg, 0);
    }

    public static <T> DataStatus<T> loading(@NonNull String msg) {
        return new DataStatus<>(LOADING, null, msg, 0);
    }

    public static <T> DataStatus<T> hide(@NonNull T data, @NonNull String msg) {
        return new DataStatus<>(HIDE, data, msg, 0);
    }

    public static <T> DataStatus<T> hide(@NonNull String msg) {
        return new DataStatus<>(HIDE, null, msg, 0);
    }

}
