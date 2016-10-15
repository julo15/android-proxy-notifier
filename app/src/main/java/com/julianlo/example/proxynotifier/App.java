package com.julianlo.example.proxynotifier;

import android.app.Application;

/**
 * Created by julianlo on 10/15/16.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final Preferences preferences = Preferences.getInstance(this);
        preferences.setFirstLaunchIfNecessary();
    }
}
