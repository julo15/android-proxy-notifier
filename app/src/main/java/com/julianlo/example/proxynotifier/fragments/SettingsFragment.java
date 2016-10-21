package com.julianlo.example.proxynotifier.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julianlo.example.proxynotifier.ConnectivityReceiver;
import com.julianlo.example.proxynotifier.ProxyDetails;
import com.julianlo.example.proxynotifier.R;

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
        if (doesPreferenceMatchKey(preference, R.string.preference_notification_enabled_key)) {
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
        wifiPreference.setTitle(proxyDetails.getWifiSummary());
    }

    private boolean doesPreferenceMatchKey(Preference preference, @StringRes int key) {
        return preference.getKey().equals(getString(key));
    }
}
