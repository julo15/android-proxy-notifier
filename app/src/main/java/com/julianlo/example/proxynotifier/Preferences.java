package com.julianlo.example.proxynotifier;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by julianlo on 10/13/16.
 */

public class Preferences {

    private static final String PREF_FILE_GENERAL = "general";
    private static final String PREF_KEY_NOTIFICATION_ENABLED = "notification_enabled";

    private static Preferences instance;
    private SharedPreferences sharedPreferences;

    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_GENERAL, Context.MODE_PRIVATE);
    }

    public boolean isNotificationEnabled() {
        return sharedPreferences.getBoolean(PREF_KEY_NOTIFICATION_ENABLED, true);
    }

    public void setNotificationEnabled(boolean enabled) {
        sharedPreferences.edit()
                .putBoolean(PREF_KEY_NOTIFICATION_ENABLED, enabled)
                .apply();
    }
}
