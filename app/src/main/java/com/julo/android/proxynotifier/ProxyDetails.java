package com.julo.android.proxynotifier;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
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
        String proxyHost = null;
        String proxyPort = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "retrieve: On Marshmallow or higher");
            ProxyInfo proxyInfo = getProxyInfo(context);
            if ((proxyInfo != null)) {
                Log.d(TAG, "retrieve: proxyInfo not null");
                proxyHost = proxyInfo.getHost();
                proxyPort = String.valueOf(proxyInfo.getPort());
            }
        } else {
            Log.d(TAG, "retrieve: Below Marshmallow");
            proxyHost = System.getProperty("http.proxyHost");
            proxyPort = System.getProperty("http.proxyPort");
        }

        ProxyDetails proxyDetails = new ProxyDetails(proxyHost, proxyPort);

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

        Log.d(TAG, "retrieve: " + proxyDetails);

        return proxyDetails;
    }

    private ProxyDetails(@Nullable String proxyHost, @Nullable String proxyPort) {
        this.isProxyOn = !isStringNullOrEmpty(proxyHost) && !isStringNullOrEmpty(proxyPort);
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

    private static boolean isStringNullOrEmpty(String s) {
        return (s == null) || s.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("on=");
        sb.append(isProxyOn());
        if (isProxyOn()) {
            sb.append(", host=");
            sb.append(getProxyHost());
            sb.append(", port=");
            sb.append(getProxyPort());
        }
        if (!isStringNullOrEmpty(getSSID())) {
            sb.append(", wifi=");
            sb.append(getSSID());
        } else {
            sb.append(", no wifi");
        }
        return sb.toString();
    }
}
