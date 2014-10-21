package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tiffany on 10/20/2014.
 */
public class shared_preferences extends Activity {

    public static final String PREF_NAME = "DOOR_PREF";
    public static final String USER_EXIST = "User_Exists";
    public static final String USER_PIN = "User_Pin_Val";

    public shared_preferences() {
        super();
    }

    public void saveData (Context context, String userName, String pinValue) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
        editor.putString("userName",userName);
        editor.putString(USER_PIN,pinValue);
        editor.putBoolean(USER_EXIST,true);
        editor.commit();
        }
    public boolean getUserExist(Context context) {
        SharedPreferences settings;
        boolean exist;
        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        exist = settings.getBoolean(USER_EXIST,false);
        return exist;
    }

    public String getPinValue(Context context) {
        SharedPreferences settings;
        String pin = null;
        settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        pin = settings.getString(USER_PIN,null);
        return pin;
    }
}
