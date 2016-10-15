package com.julianlo.example.proxynotifier;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by julianlo on 10/15/16.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        final Preferences preferences = Preferences.getInstance(this);
        preferences.setFirstLaunchIfNecessary();
    }
}
