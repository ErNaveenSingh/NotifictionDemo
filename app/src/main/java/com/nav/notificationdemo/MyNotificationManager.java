package com.nav.notificationdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by naveensingh on 10/01/17.
 */

public class MyNotificationManager {
    public final static String message = "Daily Summary - 10th Jan 2017\n" +
            "Total Invoices - 5 (Rs. 56,200)\n" +
            "Cash In - Rs. 45,022\n" +
            "Cash Out - Rs. 43,012";
    public final static String summaryText = "This is summary text";
    public final static String bigContentTitle = "Big Content Title";
    public final static int mNotificationId = 001;

    public static NotificationCompat.Builder createBasicNotification(Context context, String messageText) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notifications)
                        .setLargeIcon(bm)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(messageText);
//                        .setDefaults(Notification.DEFAULT_ALL);// This is for Vibration
        return builder;
    }

    public static void createTextNotification(Context context, String message, String summaryText
            , String bigContentTitle){
        Intent dismissIntent = new Intent(context, NotificationOpenActivity.class);
        dismissIntent.setAction("Dismiss");
        PendingIntent piDismiss = PendingIntent.getService(context, 0, dismissIntent, 0);

        NotificationCompat.Builder builder = MyNotificationManager.createBasicNotification(context, message);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .setSummaryText(summaryText)//Comes on the bottom of notification
                .bigText(message)//Multiline text
                .setBigContentTitle(bigContentTitle))//Title when the notification is expanded
                .addAction(R.drawable.ic_notifications,
                        "Done", piDismiss)
                .setAutoCancel(true);
        showNotification(context, builder);
    }

    public static void createImageNotification(Context context, String message, String summaryText
            , String bigContentTitle, @NonNull Bitmap bm){

        Intent dismissIntent = new Intent(context, NotificationOpenActivity.class);
        dismissIntent.setAction("Dismiss");
        PendingIntent piDismiss = PendingIntent.getService(context, 0, dismissIntent, 0);

        NotificationCompat.Builder builder = createBasicNotification(context, message);
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bm)// The big image
                .setSummaryText(summaryText)//Comes below title
                .setBigContentTitle(bigContentTitle))//Title when the notification is expanded
                .addAction(R.drawable.ic_notifications,
                        "Done", piDismiss)
                .setAutoCancel(true);
        showNotification(context, builder);
    }

    public static void showNotification(Context context, NotificationCompat.Builder builder){
        Intent resultIntent = new Intent(context, NotificationOpenActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);


        // Gets an instance of the MyNotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, builder.build());
    }
}
