package com.starstudio.projectdemo.utils;

import android.content.Context;

public class ContextHolder {
    private static Context CONTEXT;
    public static void init(Context context) {
        CONTEXT = context;
    }

    public static Context context() {
        if (CONTEXT == null)
            throw new NullPointerException("请先执行init(context)方法");
        return CONTEXT;
    }
}
