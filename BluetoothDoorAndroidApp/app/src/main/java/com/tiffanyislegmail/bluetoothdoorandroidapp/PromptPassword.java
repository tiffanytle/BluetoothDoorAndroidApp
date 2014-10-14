package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptPassword extends Activity implements View.OnClickListener {

    String userEntry = "";
    String pinValue = "";
    String failMsg = "Login failed. Please try again.";

    boolean correctPassword = false;

    public static final String MyPREFERENCES = "MyPrefs";

    final int PIN_LENGTH = 4;

    TextView pinBox0, pinBox1, pinBox2, pinBox3, failedPassword;

    Button button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9,
            buttonLogin, buttonClear;

    CreateNewUser callCreate = new CreateNewUser();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_password);

        pinBox0 = (TextView) findViewById(R.id.pinBox0);
        pinBox1 = (TextView) findViewById(R.id.pinBox1);
        pinBox2 = (TextView) findViewById(R.id.pinBox2);
        pinBox3 = (TextView) findViewById(R.id.pinBox3);
        failedPassword = (TextView) findViewById(R.id.failedPassword);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        pinValue = sharedPreferences.getString("pinValue",null);

    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.buttonClear:
                clearData();
                break;
            case R.id.buttonLogin:
                if (correctPassword == true) {
                    Intent intent = new Intent(v.getContext(), MainMenu.class);
                    clearData();
                    startActivity(intent);
                }
                else {
                    failedPassword.setText(failMsg);
                    clearData();
                }
                break;
            default:
                failedPassword.setText("");
                Button pressedButton = (Button) v;
                if (userEntry.length() < PIN_LENGTH) {
                    userEntry = userEntry + pressedButton.getText();
                    switch (userEntry.length()-1) {
                        case 0:
                            pinBox0.setText(pressedButton.getText().toString());
                            break;
                        case 1:
                            pinBox1.setText(pressedButton.getText().toString());
                            break;
                        case 2:
                            pinBox2.setText(pressedButton.getText().toString());
                            break;
                        case 3:
                            pinBox3.setText(pressedButton.getText().toString());
                            if (userEntry.equals(pinValue))
                                correctPassword = true;
                    }
                }
                break;
        }
    }

    public void clearData () {
        if (userEntry.length() > 0) {
            pinBox0.setText("");
            pinBox1.setText("");
            pinBox2.setText("");
            pinBox3.setText("");
            userEntry = "";
            correctPassword = false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callCreate.saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        callCreate.saveData();
    }
}
