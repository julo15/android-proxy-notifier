package com.julianlo.example.proxynotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by julianlo on 10/13/16.
 */

public class Preferences {

    private static final String PREF_FILE_GENERAL = "general";

    private static Preferences instance;
    private Context appContext;
    private SharedPreferences sharedPreferences;

    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context.getApplicationContext());
        }
        return instance;
    }

    private Preferences(Context context) {
        appContext = context;
        sharedPreferences = context.getSharedPreferences(PREF_FILE_GENERAL, Context.MODE_PRIVATE);
    }

    public boolean isNotificationEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getBoolean(appContext.getString(R.string.preference_notification_enabled_key), true);
    }
}
