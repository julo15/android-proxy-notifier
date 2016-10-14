package com.julianlo.example.proxynotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_NOTIFICATION_LAUNCH = 1;

    private ImageView statusImageView;

    private BroadcastReceiver proxyDetailsChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received broadcast: " + intent.getAction());
            updateProxyInformation();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusImageView = (ImageView)findViewById(R.id.status_imageview);

        // Settings fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        // Update the view
        updateProxyInformation();

        // Update the notification
        ConnectivityReceiver.updateNotification(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        registerReceiver(proxyDetailsChangeReceiver, new IntentFilter(ConnectivityReceiver.ACTION_PROXY_DETAILS_CHANGE));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(proxyDetailsChangeReceiver);
    }

    private SettingsFragment getSettingsFragment() {
        return (SettingsFragment)getSupportFragmentManager().findFragmentById(R.id.settings_container);
    }

    private void updateProxyInformation() {
        ProxyDetails proxyDetails = ProxyDetails.retrieve(this);

        statusImageView.setImageResource(proxyDetails.isProxyOn() ? R.drawable.ic_priority_high_black_48dp : R.drawable.ic_check_black_48dp);

        SettingsFragment settingsFragment = getSettingsFragment();
        if (settingsFragment != null) {
            settingsFragment.onProxyDetailsChanged(proxyDetails);
        }
    }
}
