package com.starstudio.projectdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * created by sgh
 * 2021-7-31
 * 该类中进行所需权限的请求与检查操作
 */
public class RequestPermission {
    public static final int CODE_SIMPLE = 2021;
    public static final int CODE_MUST = 2022;  // 以该code发起权限请求的都必须通过才可
    public static final int CODE_LOCATION = 2222;   // 为定位权限使用

    private Activity context;
    private static RequestPermission INSTANCE;

    public static void init(Activity activity) {
        if (INSTANCE == null)
            INSTANCE = new RequestPermission(activity);
    }

    public static RequestPermission getInstance() {
        if (INSTANCE == null)
            throw new NullPointerException("请在getInstance方法执行之前调用init(Activity)方法");
        return INSTANCE;
    }

    private RequestPermission (Activity context) {
        this.context = context;
    }

    // 检查权限
    public void checkPermissions(int code, String... permissions) {
        ArrayList<String> needPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != 0)
                needPermissions.add(permission);
        }
        String[] needed = new String[needPermissions.size()];
        needPermissions.toArray(needed);
        if (needPermissions.size() > 0)
            requestPermissions(needed, code);
    }

    // 请求权限
    public void requestPermissions(String[] permissions, int code) {
        ActivityCompat.requestPermissions(context, permissions, code);   // 权限处理全部交由MainActivity进行
    }

    // 专门处理定位权限的申请
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean requestLocationPermission() {
        boolean background = false;
        boolean foreground = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            background = ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (!background) {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, CODE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, CODE_LOCATION);
        }

        return foreground && background;
    }
}
