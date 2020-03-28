package com.qin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class SharedPreferencesUtil {

    private static SharedPreferencesUtil sharedPreferencesUtil;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILENAME="lafcache";

    private SharedPreferencesUtil(Context context){
        sharedPreferences=context.getSharedPreferences(FILENAME,context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public static SharedPreferencesUtil getInstance(Context context){
        if(sharedPreferencesUtil==null){
            synchronized (SharedPreferencesUtil.class){
                if(sharedPreferencesUtil==null){
                    sharedPreferencesUtil=new SharedPreferencesUtil(context);
                }
            }
        }
        return sharedPreferencesUtil;
    }

    public void putBoolean(String key,boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void putString(String key,String value){
        editor.putString(key, value);
        editor.commit();
    }

    public boolean readBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }
    public String readString(String key){
        return sharedPreferences.getString(key,"");
    }

    public Object readObject(String key,Class clazz){
        String str=sharedPreferences.getString(key,"");
        Gson gson=new Gson();
        return gson.fromJson(str,clazz);
    }

    public void delete(String key){
        editor.remove(key).commit();
    }

    public void clear(){
        editor.clear().commit();
    }
}
