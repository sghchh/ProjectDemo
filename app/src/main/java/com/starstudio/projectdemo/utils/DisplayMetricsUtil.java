package com.starstudio.projectdemo.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * created by sgh
 * 2021-8-1
 * 获取屏幕宽高
 */
public class DisplayMetricsUtil {
    public static int getDisplayWidthPxiels(Activity context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getDisplayHeightPxiels(Activity context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
