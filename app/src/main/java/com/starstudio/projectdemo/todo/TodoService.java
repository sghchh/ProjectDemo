package com.starstudio.projectdemo.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TodoService extends Service {
    private AlarmManager alarmManager;
    private long[] needNotification;
    public TodoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        needNotification = intent.getLongArrayExtra("serviceData");
        // 为通知开启时间间隔
        long[] notificationTimeLength = new long[needNotification.length];
        long now = System.currentTimeMillis();
        for (int i = 0; i < needNotification.length; i ++)
            notificationTimeLength[i] = Math.max(0, needNotification[i] - now);

        Intent setAlertIntent = new Intent(this, NotificationReceiver.class);
        int code = 0;

        for (long l : notificationTimeLength) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, code ++, setAlertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, l, pendingIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarmManager = null;
    }
}