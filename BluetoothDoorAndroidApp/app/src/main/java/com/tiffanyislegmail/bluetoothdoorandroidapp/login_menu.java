package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class login_menu extends Activity implements OnClickListener{

    boolean userExist = false, isAppUnlocked = true;
    String userName = "";
    String failedLoginMsg = "Please create a new user.";
    String failedCreateMsg = "User exists. Please login.";
    String failedLockedMsg = "You are locked out.";

    Button LoginBtn, createNewUserBtn, forgotPassword;

    TextView failedLogin;
    TextView userNameBox;

    Activity context = this;

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);

        LoginBtn = (Button) findViewById(R.id.loginBtn);
        createNewUserBtn = (Button) findViewById(R.id.createNewUserBtn);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);

        userNameBox = (TextView) findViewById(R.id.displayUserName);
        failedLogin = (TextView) findViewById(R.id.failedLogin);

        // Initializing buttons on login menu
        LoginBtn.setOnClickListener(this);
        createNewUserBtn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        // Retrieving user name
        userName = sharedPrefs.getUserName(context);
        userNameBox.setText(userName);

    }

    public void onClick (View v) {
        if (checkAppLocked() == true) {
            failedLogin.setText("");
            userExist = sharedPrefs.getUserExist(context);
            switch (v.getId()) {
                case R.id.loginBtn:
                    if (userExist) {
                        Intent intent = new Intent(v.getContext(), PromptPassword.class);
                        startActivity(intent);
                    } else
                        failedLogin.setText(failedLoginMsg);
                    break;
                case R.id.createNewUserBtn:
                    if (!userExist) {
                        Intent intent1 = new Intent(v.getContext(), CreateNewUser.class);
                        startActivity(intent1);
                    } else
                        failedLogin.setText(failedCreateMsg);
                    break;
                case R.id.forgotPassword:
                    if (userExist) {
                        Intent intent2 = new Intent(v.getContext(), ForgotPassword.class);
                        startActivity(intent2);
                        break;
                    } else
                        failedLogin.setText(failedLoginMsg);
            }
        }
        else
            failedLogin.setText(failedLockedMsg);
    }

    public boolean checkAppLocked() {
        boolean notLocked = false;
        isAppUnlocked = sharedPrefs.lockApplicationCheck(context);
        if (isAppUnlocked)
            notLocked = true;
        return notLocked;
    }


    @Override
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        callCreate.saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        callCreate.saveData();
    } */
}
