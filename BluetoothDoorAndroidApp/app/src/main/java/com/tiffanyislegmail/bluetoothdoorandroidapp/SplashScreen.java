/*----------------------------------------------------------------
 * Author:       Tiffany Le and Thuan Chu
 * File Name:    SplashScreen.java
 * Created On:   11/04/2014
 * Last updated: 12/03/2014
 *
 * Description:  Splash screen will display application name and
 *               logo for 2.5 seconds.
 *----------------------------------------------------------------*/
package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2500;
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;
    boolean userExist = false;

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
                userExist = sharedPrefs.getUserExist(context);
                /* If user exists, then prompt password. If user doesn't exist, then create a
                 * a new user */
                if (userExist) {
                    Intent intent = new Intent(SplashScreen.this, PromptPassword.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } else {
                    Intent intent1 = new Intent(SplashScreen.this, CreateNewUser.class);
                    startActivity(intent1);
                    SplashScreen.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
