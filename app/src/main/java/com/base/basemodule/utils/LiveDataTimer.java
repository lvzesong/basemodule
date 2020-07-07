package com.base.basemodule.utils;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.base.basemodule.SingleLiveEvent;

/**
 * Created by lzs on 2018/10/24 17:46
 * E-Mail Address：343067508@qq.com
 * 单位秒
 */
public class LiveDataTimer {
    public LiveData<Integer> getTime() {
        return time;
    }

    SingleLiveEvent<Integer> time;

    public LiveDataTimer() {
        time = new SingleLiveEvent<>();
    }

    private boolean isRunning = false;

    public void start() {
        if (isRunning) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int t = startTime;
                isRunning = true;
                while (t < endTime) {
                    t += interval;
                    time.postValue(t);
                    try {
                        Thread.sleep(interval * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time.postValue(startTime);
                isRunning = false;
            }
        }).start();
    }

    public void stop() {

    }

    private int startTime;
    private int endTime;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private int interval = 1;


    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
