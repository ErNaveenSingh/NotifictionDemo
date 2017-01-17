package com.nav.notificationdemo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.nav.notificationdemo.notification.MyNotificationManager.bigContentTitle;
import static com.nav.notificationdemo.notification.MyNotificationManager.message;
import static com.nav.notificationdemo.notification.MyNotificationManager.summaryText;

/**
 * Created by naveensingh on 10/01/17.
 */

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        A New Service can be created that can generate a notification
         */
        MyNotificationManager.createTextNotification(context, message, summaryText, bigContentTitle);
    }
}
