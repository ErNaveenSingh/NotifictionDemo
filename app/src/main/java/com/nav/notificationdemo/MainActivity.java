package com.nav.notificationdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nav.notificationdemo.MyNotificationManager.bigContentTitle;
import static com.nav.notificationdemo.MyNotificationManager.message;
import static com.nav.notificationdemo.MyNotificationManager.summaryText;

public class MainActivity extends AppCompatActivity {

    private long ONE_MINUTES = 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
}
