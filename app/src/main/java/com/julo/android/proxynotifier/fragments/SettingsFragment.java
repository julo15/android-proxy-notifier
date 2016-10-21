package com.julo.android.proxynotifier.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julo.android.proxynotifier.ConnectivityReceiver;
import com.julo.android.proxynotifier.ProxyDetails;
import com.julo.android.proxynotifier.R;

/**
 * Created by julianlo on 10/13/16.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    public interface Listener {
        void onLaunchIntro();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Update the view once. After, depend on the activity calling onProxyDetailsChanged.
        onProxyDetailsChanged(ProxyDetails.retrieve(getContext()));

        return view;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (doesPreferenceMatchKey(preference, R.string.preference_proxy_enabled_key)) {
            View view = getView();
            if (view != null) {
                Snackbar.make(view, R.string.proxy_toast, Snackbar.LENGTH_LONG)
                        .setAction(R.string.dismiss, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Dismiss
                            }
                        })
                        .show();
            }
            return true;
        } else if (doesPreferenceMatchKey(preference, R.string.preference_notification_enabled_key)) {
            ConnectivityReceiver.updateNotification(getContext());
            return true;
        } else if (doesPreferenceMatchKey(preference, R.string.preference_open_wifi_key)) {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
            return true;
        } else if (doesPreferenceMatchKey(preference, R.string.preference_intro_key)) {
            ((Listener)getActivity()).onLaunchIntro();
            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }

    public void onProxyDetailsChanged(ProxyDetails proxyDetails) {
        Preference proxyPreference = findPreference(getString(R.string.preference_proxy_enabled_key));
        proxyPreference.setTitle(String.format("Proxy is %s", proxyDetails.isProxyOn() ? "ON" : "OFF"));
        if (proxyDetails.isProxyOn()) {
            proxyPreference.setSummary(String.format("Set to %s:%s", proxyDetails.getProxyHost(), proxyDetails.getProxyPort()));
        } else {
            proxyPreference.setSummary(null);
        }

        Preference wifiPreference = findPreference(getString(R.string.preference_open_wifi_key));
        wifiPreference.setTitle(proxyDetails.getWifiSummary(getContext()));
    }

    private boolean doesPreferenceMatchKey(Preference preference, @StringRes int key) {
        return preference.getKey().equals(getString(key));
    }
}
