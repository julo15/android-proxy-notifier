package com.julianlo.example.proxynotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ProxyInfo;
import android.util.Log;

/**
 * Created by julianlo on 10/12/16.
 */

public class ProxyMaster {
    private static final String TAG = ProxyMaster.class.getSimpleName();

    public static ProxyInfo getProxyInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ProxyInfo proxyInfo = cm.getDefaultProxy();
        Log.d(TAG, "Proxy on = " + (proxyInfo != null));
        if (proxyInfo != null) {
            Log.d(TAG, "Proxy info = " + proxyInfo.toString());
            Log.d(TAG, "Proxy host = " + proxyInfo.getHost());
            Log.d(TAG, "Proxy port = " + proxyInfo.getPort());
        }
        return proxyInfo;
    }
}
