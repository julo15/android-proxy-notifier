package com.julianlo.example.proxynotifier.ads;

import android.content.Context;
import android.util.Log;

import com.julianlo.example.proxynotifier.Config;
import com.julianlo.example.proxynotifier.Preferences;

import java.util.Date;

/**
 * Created by julianlo on 10/14/16.
 */

public class AdBehaviour {
    private static final String TAG = AdBehaviour.class.getSimpleName();

    private static final long INITIAL_AD_FREE_GRACE_PERIOD_MILLIS = 1000 * 60 * 60 * 24 * 7; // one week
    private static final int MINIMUM_LAUNCHES_BEFORE_ADS_SHOW = 10;
    private static final long LONG_TIME_SINCE_LAST_AD_SHOWN_MILLIS = 1000 * 60 * 60 * 2; // two hours
    private static final long LONG_DELAY_BEFORE_SHOWING_MILLIS = 10 * 1000;
    private static final long SHORT_DELAY_BEFORE_SHOWING_MILLIS = 3 * 1000;

    private boolean shouldShow;
    private long delayBeforeShowingMillis;

    public static AdBehaviour determine(Context context) {
        final Preferences preferences = Preferences.getInstance(context);

        // Give an initial ad-free experience.
        final long firstLaunchMillis = preferences.getFirstLaunchMillis();
        if (Config.ENABLE_INITIAL_AD_FREE_GRACE_PERIOD &&
                System.currentTimeMillis() - firstLaunchMillis < INITIAL_AD_FREE_GRACE_PERIOD_MILLIS) {
            Log.d(TAG, "Skipping ad: In grace period (installed on " + new Date(firstLaunchMillis).toString() + ")");
            return noShow();
        }

        // And give an initial number of ad-free launches.
        final int launchCount = preferences.getAppLaunchCount();
        if (Config.ENABLE_MINIMUM_LAUNCHES_BEFORE_ADS_SHOWN &&
                launchCount <= MINIMUM_LAUNCHES_BEFORE_ADS_SHOW) {
            Log.d(TAG, "Skipping ad: Let user launch the app more (launch count: " + launchCount + ")");
            return noShow();
        }

        // An ad should be shown.
        // Here we determine how long the user will need to stay in the app before we show it.
        // The delay will either be 'short' or 'long', depending on how long ago the user saw an ad.
        final long lastAdShown = preferences.getLastAdShownMillis();
        final boolean useLongDelay = Config.ENABLE_LONG_DELAY_BEFORE_SHOWING_AD &&
                System.currentTimeMillis() - lastAdShown < LONG_TIME_SINCE_LAST_AD_SHOWN_MILLIS;
        final long delayMillis = useLongDelay ? LONG_DELAY_BEFORE_SHOWING_MILLIS : SHORT_DELAY_BEFORE_SHOWING_MILLIS;

        Log.d(TAG, "Showing ad: Using " + (useLongDelay ? "long" : "short") + " delay. (Last shown: " + new Date(lastAdShown).toString() + ")");
        return new AdBehaviour(true, delayMillis);
    }

    private static AdBehaviour noShow() {
        return new AdBehaviour(false, 0);
    }

    private AdBehaviour(boolean shouldShow, long delayBeforeShowingMillis) {
        this.shouldShow = shouldShow;
        this.delayBeforeShowingMillis = delayBeforeShowingMillis;
    }

    public boolean isShouldShow() {
        return shouldShow;
    }

    public long getDelayBeforeShowingMillis() {
        return delayBeforeShowingMillis;
    }

    public void notifyAdShown(Context context) {
        Preferences.getInstance(context).updateLastAdShownMillis();
    }
}
