package com.starstudio.projectdemo.todo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.MyApplication;
import com.starstudio.projectdemo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TODONotification {
    public static final String CHANNEL_ID = "UESTC2021";
    public static final int NOTIFICATION_ID = 2021;

    /**
     * 创建通知渠道
     * @param context
     */
    public static void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Todo Notification";
            String description = "待办事项的通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
