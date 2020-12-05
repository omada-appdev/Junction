package com.omada.junction.application;

import android.app.Application;
import android.content.Context;

public class JunctionApplication extends Application {

    private static JunctionApplication instance;

    public static JunctionApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
