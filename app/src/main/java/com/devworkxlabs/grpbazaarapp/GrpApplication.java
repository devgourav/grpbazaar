package com.devworkxlabs.grpbazaarapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class GrpApplication extends Application {
    private static GrpApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
    public static GrpApplication getInstance() {
        return sInstance ;
    }
}
