package com.julo.android.proxynotifier;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.julo.android.proxynotifier.activities.MainActivity;

/**
 * Created by julianlo on 10/12/16.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectivityReceiver.class.getSimpleName();
    private static final int NOTIFICATION_ID_PROXY_INFO = 1;

    public static final String ACTION_PROXY_DETAILS_CHANGE = "com.julianlo.example.proxynotifier.PROXY_DETAILS_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received connectivity change: " + intent.toString());
        updateNotification(context);
        sendBroadcast(context);
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
        ProxyDetails proxyDetails = ProxyDetails.retrieve(context);

        // Proxy info
        StringBuilder sb = new StringBuilder()
                .append("Proxy is ")
                .append(proxyDetails.isProxyOn() ? "ON" : "OFF");

        if (proxyDetails.isProxyOn()) {
            sb.append(": ")
                    .append(proxyDetails.getProxyHost())
                    .append(":")
                    .append(proxyDetails.getProxyPort());
        }
        final String title = sb.toString();

        // Network info
        final String text = proxyDetails.getWifiSummary(context);

        final Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                MainActivity.REQUEST_NOTIFICATION_LAUNCH,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(proxyDetails.isProxyOn() ? R.drawable.ic_priority_high_white_24dp : R.drawable.ic_check_white_24dp)
                .setColor(ContextCompat.getColor(context, proxyDetails.isProxyOn() ? R.color.notification_proxy_on : R.color.notification_proxy_off))
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

    private static void sendBroadcast(Context context) {
        Intent intent = new Intent(ACTION_PROXY_DETAILS_CHANGE);
        context.sendBroadcast(intent);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
