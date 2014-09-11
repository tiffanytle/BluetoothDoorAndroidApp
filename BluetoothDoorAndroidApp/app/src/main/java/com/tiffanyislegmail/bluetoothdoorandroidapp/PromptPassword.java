package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PromptPassword extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_password);

        // Initialize button on prompt password menu
        Button goToMainMenu = (Button) findViewById(R.id.clickToMainMenuBtn);
        Button goToForgotPW = (Button) findViewById(R.id.clickForgotPW);
        goToMainMenu.setOnClickListener(this);
        goToForgotPW.setOnClickListener(this);
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.clickToMainMenuBtn:
                Intent intent_mainMenu = new Intent(v.getContext(),MainMenu.class);
                startActivity(intent_mainMenu);
                break;
            case R.id.clickForgotPW:
                Intent intent_forgotPW = new Intent(v.getContext(),ForgotPassword.class);
                startActivity(intent_forgotPW);
                break;
        }
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
