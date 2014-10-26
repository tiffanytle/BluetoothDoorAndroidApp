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
    public static final String USER_NAME = "User_Name";
    public static final String VACATION = "Security_Vacation";
    public static final String CAR_MODEL = "Security_CarModel";
    public static final String CAR_MAKE = "Security_CarMake";
    public static final String LOCK_DATE = "Lock_Date";
    public static final String IS_LOCKED = "Is_currently_locked";

    public shared_preferences() {
        super();
    }

    public void saveData(Context context, String userName, String pinValue) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
        editor.putString(USER_NAME, userName);
        editor.putString(USER_PIN, pinValue);
        editor.putBoolean(USER_EXIST, true);
        editor.commit();
    }

    public void saveSecurity(Context context, String vacation, String make, String model) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(VACATION, vacation);
        editor.putString(CAR_MAKE, make);
        editor.putString(CAR_MODEL, model);
        editor.commit();
    }

    public boolean getUserExist(Context context) {
        boolean exist;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        exist = settings.getBoolean(USER_EXIST, false);
        return exist;
    }

    public String getPinValue(Context context) {
        String pin = null;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pin = settings.getString(USER_PIN, null);
        return pin;
    }

    public String getUserName(Context context) {
        String name = null;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        name = settings.getString(USER_NAME, null);
        return name;
    }

    public String getSecurityVacation(Context context) {
        String vacation = null;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        vacation = settings.getString(VACATION, null);
        return vacation;
    }

    public String getSecurityCarMake(Context context) {
        String carMake = null;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        carMake = settings.getString(CAR_MAKE, null);
        return carMake;
    }

    public String getSecurityCarModel(Context context) {
        String carModel = null;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        carModel = settings.getString(CAR_MODEL, null);
        return carModel;
    }

    public void clearSavedPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public void setLockApp(Context context) {

        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        long startLockTime = System.currentTimeMillis();

        editor.putLong(LOCK_DATE,startLockTime);
        editor.putBoolean(IS_LOCKED,true);
        editor.commit();
    }

    public boolean checkLockApp (Context context) {
        boolean appLocked;
        long compareLockTime, startLockTime, stopLockTime;
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        appLocked = settings.getBoolean(IS_LOCKED,false);
        if (appLocked == true) {
            startLockTime = settings.getLong(LOCK_DATE,0);
            stopLockTime = System.currentTimeMillis();
            compareLockTime = stopLockTime - startLockTime;
            // 300,000 milliseconds = 5 mins
            if (compareLockTime > 300000) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(IS_LOCKED,false);
                editor.putLong(LOCK_DATE,0);
                editor.commit();
                appLocked = false;
            }
        }
        return appLocked; // if false, then app is unlocked. if true, then app is locked

    }

}
