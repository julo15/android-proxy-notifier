# Android Proxy Notifier

This app shows and maintains an [ongoing notification](https://developer.android.com/reference/android/app/Notification.Builder.html#setOngoing%28boolean%29) that displays your current proxy status. 

## How it works

The app has a `BroadcastReceiver` for wifi and proxy changes, and will keep the notification up-to-date with current proxy information.

## Installation

1. Download the [apk](https://github.com/julo15/android-proxy-notifier/blob/master/release/proxy-notifier-161013.apk?raw=true).
2. Open the apk on your Android device to install.
3. Open the `ProxyNotifier` app to start the notification.

![](https://github.com/julo15/android-proxy-notifier/blob/master/images/proxy-off.png?raw=true =500x)
![](https://github.com/julo15/android-proxy-notifier/blob/master/images/proxy-on.png?raw=true =500x)

## That's pretty much it

After you start the app, you'll see a notification at the top of your notification tray showing whether a proxy is on or off, and what the proxy host/port is (if it's on).

### See status from the status bar

For the extra lazy, you can glance at the icon in your status bar to see if a proxy is on or off.
- 'exclamation mark' = proxy is ON
- 'check mark' = proxy is OFF


### Enabling or disabling

The notification can be easily shown and hidden by opening the app and toggling the `Notification enabled` switch.
