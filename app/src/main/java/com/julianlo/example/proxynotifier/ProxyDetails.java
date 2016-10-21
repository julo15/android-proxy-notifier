package com.julianlo.example.proxynotifier;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by julianlo on 10/14/16.
 */

public class ProxyDetails {
    private static final String TAG = ProxyDetails.class.getSimpleName();

    private boolean isProxyOn;
    private String proxyHost;
    private String proxyPort;

    private boolean isWifiConnected;
    private String ssid;

    public static ProxyDetails retrieve(Context context) {
        // Proxy info
        boolean isProxyOn;
        String proxyHost = null;
        String proxyPort = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ProxyInfo proxyInfo = getProxyInfo(context);
            isProxyOn = (proxyInfo != null);
            if (isProxyOn) {
                proxyHost = proxyInfo.getHost();
                proxyPort = String.valueOf(proxyInfo.getPort());
            }
        } else {
            proxyHost = System.getProperty("http.proxyHost");
            proxyPort = System.getProperty("http.proxyPort");
            isProxyOn = (proxyHost != null) && (proxyPort != null);
        }

        ProxyDetails proxyDetails = new ProxyDetails(isProxyOn, proxyHost, proxyPort);

        // Wifi info
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        proxyDetails.isWifiConnected = (networkInfo != null) && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) && networkInfo.isConnected();
        if (proxyDetails.isWifiConnected) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                proxyDetails.ssid = wifiInfo.getSSID();
            }
        }

        return proxyDetails;
    }

    private ProxyDetails(boolean isProxyOn, String proxyHost, String proxyPort) {
        this.isProxyOn = isProxyOn;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static android.net.ProxyInfo getProxyInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.ProxyInfo proxyInfo = cm.getDefaultProxy();
        Log.d(TAG, "Proxy on = " + (proxyInfo != null));
        if (proxyInfo != null) {
            Log.d(TAG, "Proxy info = " + proxyInfo.toString());
            Log.d(TAG, "Proxy host = " + proxyInfo.getHost());
            Log.d(TAG, "Proxy port = " + proxyInfo.getPort());
        }
        return proxyInfo;
    }

    public boolean isProxyOn() {
        return isProxyOn;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public boolean isWifiConnected() {
        return isWifiConnected;
    }

    public String getSSID() {
        return ssid;
    }

    public String getWifiSummary(Context context) {
        if (isWifiConnected()) {
            return context.getString(R.string.wifi_summary_connected, getSSID() != null ? getSSID() : R.string.unknown_wifi);
        } else {
            return context.getString(R.string.wifi_summary_disconnected);
        }
    }
}
