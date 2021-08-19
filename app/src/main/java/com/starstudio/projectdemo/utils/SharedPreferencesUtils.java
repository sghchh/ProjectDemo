package com.starstudio.projectdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 *
 */
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils sharedPreferencesUtils;
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILENAME="qrcode";

    public static enum Key{
        KEY_IVINFO("ivInfo"), KEY_BUDGET_EAT("budgetEat"), KEY_BUDGET_LEARN("budgetLearn"),
        KEY_BUDGET_CLOTHES("budgetClothes"), KEY_BUDGET_COMMUN_TRANS("budgetCommunTrans"),
        KEY_BUDGET_RELAX("budgetRelax"), KEY_BUDGET_OTHER("budgetOther");
        private final String text;
        private Key(final String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

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

    public void putUri(String key, Uri uri){
        editor.putString(key,uri.toString());
        editor.commit();
    }

    public boolean readBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public String readString(String key){
        return sharedPreferences.getString(key,"");
    }

    public Uri readUri(String key){
        if(sharedPreferences.getString(key,"") == ""){
            return null;
        }else{
            return Uri.parse(sharedPreferences.getString(key,""));
        }
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
