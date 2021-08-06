package com.starstudio.projectdemo.utils;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class HandlerHelper {
    private static final Map<Class, Handler> handlerPool = new HashMap<>();
    public static void register(Class clazz, Handler.Callback callback) {
        if (handlerPool.containsKey(clazz))
            return;
        handlerPool.put(clazz, new Handler(callback));
    }

    public static void send(Class clazz, Message message) {
        if (!handlerPool.containsKey(clazz))
            throw new RuntimeException("请先执行register方法");
        handlerPool.get(clazz).sendMessage(message);
    }
}
