package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2500;
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;
    boolean userExist = false, isAppLocked = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if ((checkAppLocked()) == true) {
                    userExist = sharedPrefs.getUserExist(context);
                    if (userExist) {
                        Intent intent = new Intent(SplashScreen.this, PromptPassword.class);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    } else {
                        Intent intent1 = new Intent(SplashScreen.this, CreateNewUser.class);
                        startActivity(intent1);
                        SplashScreen.this.finish();
                    }
                } else {
                    Toast.makeText(context, "You are currently locked out. Please try again later."
                            , Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public boolean checkAppLocked() {
        boolean notLocked = false;
        isAppLocked = sharedPrefs.checkLockApp(context);
        if (isAppLocked == false)
            notLocked = true;
        return notLocked;
    }

}
