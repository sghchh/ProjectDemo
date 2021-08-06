package com.starstudio.projectdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;

public class OtherUtil {

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
