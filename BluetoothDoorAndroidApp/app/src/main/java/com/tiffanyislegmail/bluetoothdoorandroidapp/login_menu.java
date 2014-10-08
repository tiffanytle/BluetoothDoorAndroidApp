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

    boolean userExist = false;
    String userName = "";
    String failedLoginMsg = "Please create a new user.";
    String failedCreateMsg = "User exists. Please login.";

    TextView failedLogin;
    CreateNewUser getUser = new CreateNewUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);

        Button LoginBtn = (Button) findViewById(R.id.loginBtn);
        Button createNewUserBtn = (Button) findViewById(R.id.createNewUserBtn);
        TextView userNameBox = (TextView) findViewById(R.id.displayUserName);
        failedLogin = (TextView) findViewById(R.id.failedLogin);

        // Initializing buttons on login menu
        LoginBtn.setOnClickListener(this);
        createNewUserBtn.setOnClickListener(this);

        userName = getUser.getUserName();
        userNameBox.setText(userName);
    }

    public void onClick (View v) {
        failedLogin.setText("");
        userExist = getUser.getUserExist();
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
        }
    }

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
}
