package com.flowertale.flowertaleandroid.application;

import android.app.Application;

import com.flowertale.flowertaleandroid.util.ContextUtil;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.initial(this);
    }
}
