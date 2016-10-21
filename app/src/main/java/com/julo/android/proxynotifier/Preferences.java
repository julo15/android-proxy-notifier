package com.julo.android.proxynotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by julianlo on 10/13/16.
 */

public class Preferences {

    private static final String PREF_FILE_GENERAL = "general";
    private static final String PREF_KEY_APP_FIRST_LAUNCH_MILLIS = "app_first_launch_millis";
    private static final String PREF_KEY_APP_LAUNCH_COUNT = "app_launches_count";
    private static final String PREF_KEY_INTRO_SHOWN = "intro_shown";
    private static final String PREF_KEY_LAST_AD_SHOWN_MILLIS = "last_ad_shown_millis";

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

    public void setFirstLaunchIfNecessary() {
        if (getFirstLaunchMillis() == 0) {
            sharedPreferences.edit()
                    .putLong(PREF_KEY_APP_FIRST_LAUNCH_MILLIS, System.currentTimeMillis())
                    .apply();
        }
    }

    public long getFirstLaunchMillis() {
        return sharedPreferences.getLong(PREF_KEY_APP_FIRST_LAUNCH_MILLIS, 0);
    }

    public void incrementLaunchCount() {
        int count = getAppLaunchCount() + 1;
        sharedPreferences.edit()
                .putInt(PREF_KEY_APP_LAUNCH_COUNT, count)
                .apply();
    }

    public int getAppLaunchCount() {
        return sharedPreferences.getInt(PREF_KEY_APP_LAUNCH_COUNT, 0);
    }

    public void setIntroShown() {
        sharedPreferences.edit()
                .putBoolean(PREF_KEY_INTRO_SHOWN, true)
                .apply();
    }

    public boolean getIntroShown() {
        return sharedPreferences.getBoolean(PREF_KEY_INTRO_SHOWN, false);
    }

    public void updateLastAdShownMillis() {
        sharedPreferences.edit()
                .putLong(PREF_KEY_LAST_AD_SHOWN_MILLIS, System.currentTimeMillis())
                .apply();
    }

    public long getLastAdShownMillis() {
        return sharedPreferences.getLong(PREF_KEY_LAST_AD_SHOWN_MILLIS, 0);
    }

    public boolean isNotificationEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getBoolean(appContext.getString(R.string.preference_notification_enabled_key), true);
    }
}
