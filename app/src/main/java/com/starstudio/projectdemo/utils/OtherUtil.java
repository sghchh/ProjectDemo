package com.starstudio.projectdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherUtil {
    private static final Gson gson = new Gson();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-E");


    public static Bitmap scaleSquare(Bitmap originBitmap) {
        Bitmap res;
        int originWidth = originBitmap.getWidth();
        int originHeight = originBitmap.getHeight();
        if (originWidth > originHeight) {
            int x = (originWidth - originHeight) / 2;
            int y = 0;
            res = Bitmap.createBitmap(originBitmap, x, y, originHeight, originHeight);
        } else {
            int y = (originHeight - originWidth) / 2;
            int x = 0;
            res = Bitmap.createBitmap(originBitmap, x, y, originWidth, originWidth);
        }
        return res;
    }

    public static String decodeObject(Object o) {
        return gson.toJson(o);
    }

    public static Object encodeString(String json) {
        return gson.toJson(json);
    }

    public static String getSystemMonth() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[0];
    }

    public static String getSystemDay() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[0];
    }

    public static String getSystemWeek() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[0];
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int p2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px 转换为 sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 sp 转换为 px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



}
