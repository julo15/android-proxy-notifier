package com.julianlo.example.proxynotifier;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_NOTIFICATION_LAUNCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Notification enabled toggle
        Switch notificationEnabledToggleButton = (Switch)findViewById(R.id.notification_enabled_togglebutton);
        notificationEnabledToggleButton.setChecked(Preferences.getInstance(this).isNotificationEnabled());
        notificationEnabledToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences.getInstance(MainActivity.this).setNotificationEnabled(b);
                ConnectivityReceiver.updateNotification(MainActivity.this);
            }
        });

        // Open wifi button
        findViewById(R.id.wifi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });

        // Update the notification
        ConnectivityReceiver.updateNotification(this);
    }

}
