package com.starstudio.projectdemo.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.R;

import static com.starstudio.projectdemo.todo.TODONotification.CHANNEL_ID;
import static com.starstudio.projectdemo.todo.TODONotification.NOTIFICATION_ID;

public class TodoService extends Service {
    private AlarmManager alarmManager;
    private long[] needNotification;
    private Handler handler;
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
            Log.e("测试后台服务", "for: 为第l个通知定时:"+needNotification.length);
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

    private void notification() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("待办提醒")
                .setContentText("您有待办事项需要处理")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}