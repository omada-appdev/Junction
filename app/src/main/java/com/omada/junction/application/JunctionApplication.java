package com.omada.junction.application;

import android.app.Application;
import android.content.Context;

import com.omada.junction.utils.FileUtilities;

public final class JunctionApplication extends Application {

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
