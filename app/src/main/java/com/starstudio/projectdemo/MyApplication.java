package com.starstudio.projectdemo;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.huawei.hms.mlsdk.common.MLApplication;
import com.starstudio.projectdemo.journal.api.JournalDatabase;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MLApplication.getInstance().setApiKey("CgB6e3x9c2tIlXQZdvRg9VeCfngxvAwbW5FpKsYs/7eW39cdgYZ90pxu2gM85yEp+f2zCFSTXy4CebF3cdcULMzc");
        JournalDatabase.init(this);
    }


    // https://www.jianshu.com/p/7c5217662d9f
    private String getIMEIDeviceId(Context context) {
        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    deviceId = mTelephony.getImei();
                }else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }
}
