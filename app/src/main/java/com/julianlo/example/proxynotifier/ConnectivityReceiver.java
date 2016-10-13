package com.julianlo.example.proxynotifier;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.ProxyInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by julianlo on 10/12/16.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectivityReceiver.class.getSimpleName();
    private static final int NOTIFICATION_ID_PROXY_INFO = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received connectivity change: " + intent.toString());
        updateNotification(context);
    }


    public static void updateNotification(Context context) {
        boolean enabled = Preferences.getInstance(context).isNotificationEnabled();
        if (enabled) {
            sendNotification(context);
        } else {
            clearNotification(context);
        }
    }

    private static void sendNotification(Context context) {
        // Proxy info
        boolean isProxyOn;
        String proxyHost = null;
        String proxyPort = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ProxyInfo proxyInfo = ProxyMaster.getProxyInfo(context);
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

        StringBuilder sb = new StringBuilder()
                .append("Proxy is ")
                .append(isProxyOn ? "ON" : "OFF");

        if (isProxyOn) {
            sb.append(": ")
                    .append(proxyHost)
                    .append(":")
                    .append(proxyPort);
        }
        final String title = sb.toString();

        // Network info
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isWifiConnected = (networkInfo != null) && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) && networkInfo.isConnected();
        sb = new StringBuilder();
        if (isWifiConnected) {
            sb.append("Wifi: ");
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && wifiInfo.getSSID() != null) {
                sb.append(wifiInfo.getSSID());
            } else {
                sb.append("Unknown");
            }
        } else {
            sb.append("No Wifi");
        }
        final String text = sb.toString();

        final Intent intent = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                MainActivity.REQUEST_NOTIFICATION_LAUNCH,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(isProxyOn ? R.drawable.ic_priority_high_black_24dp : R.drawable.ic_check_black_24dp)
                .setColor(ContextCompat.getColor(context, isProxyOn ? R.color.notification_proxy_on : R.color.notification_proxy_off))
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true);

        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.notify(NOTIFICATION_ID_PROXY_INFO, builder.build());
    }

    private static void clearNotification(Context context) {
        getNotificationManager(context).cancel(NOTIFICATION_ID_PROXY_INFO);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
