package com.starstudio.projectdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILENAME="qrcode";

    private SharedPreferencesUtils(Context context){
        sharedPreferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesUtils getInstance(Context context){
        if(sharedPreferences == null){
            synchronized (SharedPreferencesUtils.class){
                if(sharedPreferences == null){
                    sharedPreferencesUtils = new SharedPreferencesUtils(context);
                }
            }
        }
        return sharedPreferencesUtils;
    }


    public void putBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void putString(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public boolean readBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public String readString(String key){
        return sharedPreferences.getString(key,"");
    }


    public void delete(String key){
        editor.remove(key);
        editor.commit();
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}
