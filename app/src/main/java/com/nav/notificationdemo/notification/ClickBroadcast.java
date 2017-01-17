package com.nav.notificationdemo.notification;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 * Created by naveensingh on 10/01/17.
 */

public class ClickBroadcast extends BroadcastReceiver {
    public static final String INTENT_FILTER = "com.nav.test";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!isAppOpened(context)) {
            /*
            if app is in background open new activity
             */
            Intent newIntent = new Intent(context, NotificationOpenActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }else{
            /*
            else generate a local broadcast and notify any listening activity
             */
            Intent newIntent = new Intent(INTENT_FILTER);
            //Put your all data using put extra
            newIntent.putExtra("key", "Notification was clicked");
            LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
        }
    }

    /*
    This checks if the app is in foreground or background
     */
    public boolean isAppOpened(Context context) {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            return true;
        }

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // App is foreground, but screen is locked, so show notification
        return km.inKeyguardRestrictedInputMode();
    }
}
