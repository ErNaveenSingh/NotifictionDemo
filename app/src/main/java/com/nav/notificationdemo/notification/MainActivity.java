package com.nav.notificationdemo.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nav.notificationdemo.R;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nav.notificationdemo.notification.MyNotificationManager.bigContentTitle;
import static com.nav.notificationdemo.notification.MyNotificationManager.message;
import static com.nav.notificationdemo.notification.MyNotificationManager.summaryText;

public class MainActivity extends AppCompatActivity {

    /*
    Notes

    1. When a notification is created in MyNotificationManager in showNotification(..)
        instead of a Activity Pending Intent a broadcast Intent ('ClickBroadcast') is sent as pending Intent.
    2. When the notification is clicked then the ClickBroadcast is called where it is checked if the app is running in foreground or not
        if the app is in foreground a local broadcast is generated which can be captured by registering a receiver for that broadcast
        if the app is not in foreground a new Activity is launched like before
    Changes
    1. Register the broadcast in manifest file
    2. Register to listen for broadcast in Activity where you want to receive a message
        E.g. See the onResume and onPause below.

     */


    private long ONE_MINUTES = 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Register for local notification
         */
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ClickBroadcast.INTENT_FILTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        Un Register for local notification
         */
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @OnClick(R.id.textNotificationButton)
    public void createTextNotification() {
        MyNotificationManager.createTextNotification(this, message, summaryText, bigContentTitle);
    }

    @OnClick(R.id.imageNotificationButton)
    public void createImageNotification() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.demo_image);
        MyNotificationManager.createImageNotification(this, message, summaryText, bigContentTitle, bm);
    }

    @OnClick(R.id.generateOneMinuteButton)
    public void generateFiveMinuteNotification() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pIntent = generateBroadcastIntent();

        Calendar cal = Calendar.getInstance();
        /*
        Use this to set Time in hours, minutes and seconds
        cal.set(Calendar.HOUR_OF_DAY, 19);
        cal.set(Calendar.MINUTE, 20);
        cal.set(Calendar.SECOND, 0);
        */
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 1000, ONE_MINUTES, pIntent);
    }

    @OnClick(R.id.stopOneMinuteButton)
    public void stopFiveMinuteNotification() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pIntent = generateBroadcastIntent();
        alarmMgr.cancel(pIntent);
    }

    private PendingIntent generateBroadcastIntent() {
        Intent intent = new Intent(this, MyBroadcast.class);
        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
            You can start a new activity here
             */
            if (intent != null) {
                String message = intent.getStringExtra("key");
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
