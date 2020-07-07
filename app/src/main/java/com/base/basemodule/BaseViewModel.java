package com.base.basemodule;

import android.content.Intent;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.basemodule.data.DataStatus;
import com.base.basemodule.data.Status;


/**
 * A ViewModel that is also an Observable,
 * to be used with the Data Binding Library.
 */
public abstract class BaseViewModel extends ViewModel implements Observable {
    public BaseViewModel() {
        ARouter.getInstance().inject(this);
    }
    public SingleLiveEvent<DataStatus<String>> getProgressEvent() {
        return progressEvent;
    }

    //进度条状态事件
    protected SingleLiveEvent<DataStatus<String>> progressEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<DataStatus> getLoadStatusSingleLiveEvent() {
        return loadStatusSingleLiveEvent;
    }

    public ObservableField<Status> status = new ObservableField<>();
    //网络请求状态事件封装
    protected SingleLiveEvent<DataStatus> loadStatusSingleLiveEvent = new SingleLiveEvent<>();


    protected void showProgressEvent() {
        progressEvent.postValue(DataStatus.<String>loading(null, ""));
    }

    protected void showProgressEvent(String msg) {
        progressEvent.postValue(DataStatus.<String>loading(msg));
    }

    protected void hideProgress() {
        progressEvent.postValue(DataStatus.<String>hide(""));
    }

    protected void postSuccessLoaddata() {
        loadStatusSingleLiveEvent.postValue(DataStatus.success(""));
    }

    protected void postSuccessLoaddata(String msg) {
        loadStatusSingleLiveEvent.postValue(DataStatus.success(null, msg));
    }

    protected <T> void postSuccessLoaddata(String msg, T data) {
        loadStatusSingleLiveEvent.postValue(DataStatus.success(data, msg));
    }

    protected void postFailedLoaddata() {
        loadStatusSingleLiveEvent.postValue(DataStatus.error(""));
    }

    protected void postFailedLoaddata(String msg) {
        loadStatusSingleLiveEvent.postValue(DataStatus.error(msg, msg));
    }

    protected <T> void postFailedLoaddata(String msg, T data) {
        loadStatusSingleLiveEvent.postValue(DataStatus.error(data, msg));
    }


    protected void postEmptyLoaddata() {
        loadStatusSingleLiveEvent.postValue(DataStatus.empty(""));
    }

    protected void postEmptyLoaddata(String msg) {
        loadStatusSingleLiveEvent.postValue(DataStatus.empty(msg, msg));
    }

    protected <T> void postEmptyLoaddata(String msg, T data) {
        loadStatusSingleLiveEvent.postValue(DataStatus.empty(data, msg));
    }


    public abstract void handleActivityResult(int requestCode, int resultCode, Intent data);

    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(
            Observable.OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(
            Observable.OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }
}
