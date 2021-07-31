package com.starstudio.projectdemo.utils;

import android.app.Activity;

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
        ActivityCompat.requestPermissions(context, permissions, code);
    }
}
