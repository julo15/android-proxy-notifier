package com.julo.android.proxynotifier.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.julo.android.proxynotifier.ConnectivityReceiver;
import com.julo.android.proxynotifier.Preferences;
import com.julo.android.proxynotifier.ProxyDetails;
import com.julo.android.proxynotifier.R;
import com.julo.android.proxynotifier.ads.AdBehaviour;
import com.julo.android.proxynotifier.ads.AdRequestBuilderImpl;
import com.julo.android.proxynotifier.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements SettingsFragment.Listener {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_NOTIFICATION_LAUNCH = 1;
    private static final int REQUEST_INTRO_ON_CREATE = 2;

    private static final long AD_LOAD_TIME_MILLIS = 3000;
    private static final long AD_FADE_IN_DURATION_MILLIS = 1500;
    private static final long STATUS_ICON_TRANSITION_MILLIS = 1000;

    private ViewGroup statusSceneRoot;
    private ImageView statusImageView;

    private boolean checkAdDone;

    private BroadcastReceiver proxyDetailsChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received broadcast: " + intent.getAction());
            updateProxyInformation(ProxyDetails.retrieve(context));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Settings fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        // Status area
        statusSceneRoot = (ViewGroup)findViewById(R.id.status_scene_root);
        TransitionManager.go(Scene.getSceneForLayout(statusSceneRoot, R.layout.scene_main_status, this));
        statusImageView = (ImageView)findViewById(R.id.status_imageview);

        // Update the notification
        ConnectivityReceiver.updateNotification(this);

        // Update the launch count before we check on ads
        Preferences.getInstance(this).incrementLaunchCount();

        if (shouldShowIntro()) {
            proceedToIntro(true /* onCreate */);
        } else {
            // Queue up an ad show if necessary
            checkAdOnCreate();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Update the view
        updateProxyInformation(ProxyDetails.retrieve(this));

        registerReceiver(proxyDetailsChangeReceiver, new IntentFilter(ConnectivityReceiver.ACTION_PROXY_DETAILS_CHANGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(proxyDetailsChangeReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_INTRO_ON_CREATE) {
            checkAdOnCreate();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLaunchIntro() {
        proceedToIntro(false /* onCreate */);
    }

    private void proceedToIntro(boolean onCreate) {
        Intent intent = IntroActivity.newIntent(this);
        if (onCreate) {
            startActivityForResult(intent, REQUEST_INTRO_ON_CREATE);
        } else {
            startActivity(intent);
        }
        onIntroShown();
    }

    private SettingsFragment getSettingsFragment() {
        return (SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.settings_container);
    }

    private void updateStatusArea(ProxyDetails proxyDetails) {
        statusImageView.setImageResource(proxyDetails.isProxyOn() ? R.drawable.ic_priority_high_black_48dp : R.drawable.ic_check_black_48dp);
    }

    private void updateProxyInformation(ProxyDetails proxyDetails) {
        updateStatusArea(proxyDetails);

        SettingsFragment settingsFragment = getSettingsFragment();
        if (settingsFragment != null) {
            settingsFragment.onProxyDetailsChanged(proxyDetails);
        }
    }

    /**
     * Helper called during onCreate that queues up the ad show if necessary.
     */
    private void checkAdOnCreate() {
        if (checkAdDone) {
            return;
        }

        checkAdDone = true;
        final AdBehaviour adBehaviour = AdBehaviour.determine(this);
        if (adBehaviour.isShouldShow()) {
            // showAd will add its own delay of AD_LOAD_TIME_MILLIS, so subtract that amount off here.
            long delayMillis = (adBehaviour.getDelayBeforeShowingMillis() > AD_LOAD_TIME_MILLIS) ?
                    adBehaviour.getDelayBeforeShowingMillis() - AD_LOAD_TIME_MILLIS : 0;

            Log.d(TAG, "On resume: Will start showing ad in " + delayMillis + "ms");

            statusImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isDestroyed()) {
                            showAdAsync();
                            adBehaviour.notifyAdShown(MainActivity.this);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Failed to show ad after behaviour delay", e);
                    }
                }
            }, delayMillis);
        }
    }

    /**
     * Loads an ad, gives it time to load, then transitions it in.
     * Note that this method also initializes AdMob since its only possible for this to get called
     * once per activity instance creation.
     */
    private void showAdAsync() {
        // Init AdMob and load the ad
        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_unit_id_main_native));
        final AdView adView = (AdView)findViewById(R.id.adview);
        AdRequest adRequest = new AdRequestBuilderImpl().build();
        adView.loadAd(adRequest);

        Runnable adTransition = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isDestroyed()) {
                        // Shrink and move the status icon
                        final Scene scene = Scene.getSceneForLayout(statusSceneRoot,
                                R.layout.scene_main_status_with_ad,
                                MainActivity.this);
                        final Transition transition = new AutoTransition()
                                .setDuration(STATUS_ICON_TRANSITION_MILLIS);
                        TransitionManager.go(scene, transition);
                        statusImageView = (ImageView) findViewById(R.id.status_imageview);
                        updateStatusArea(ProxyDetails.retrieve(MainActivity.this));

                        // Fade in the ad
                        findViewById(R.id.ad_container).animate()
                                .alpha(1.0f)
                                .setDuration(AD_FADE_IN_DURATION_MILLIS);

                        Log.d(TAG, "Ad now showing");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to fade in advertisement after load delay", e);
                }
            }
        };

        // Give the ad some time to load before fading it in.
        statusSceneRoot.postDelayed(adTransition, AD_LOAD_TIME_MILLIS);
    }

    private boolean shouldShowIntro() {
        return !Preferences.getInstance(this).getIntroShown();
    }

    private void onIntroShown() {
        Preferences.getInstance(this).setIntroShown();
    }
}
