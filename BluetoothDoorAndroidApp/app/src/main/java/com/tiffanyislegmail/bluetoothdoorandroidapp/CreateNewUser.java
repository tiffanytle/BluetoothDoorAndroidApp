package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;


public class CreateNewUser extends Activity implements View.OnClickListener {

    private static String pinValue = "";
    private static String userName = "";
    private static boolean userExist;
    public static final String MyPREFERENCES = "MyPrefs";

    String userEntry = "";
    String failPin = "Invalid pin. Please try again.";
    String failUser = "Invalid username. Please try again.";

    boolean readyPin = false;

    final int PIN_LENGTH = 4;

    TextView pinBox0, pinBox1, pinBox2, pinBox3, failedCreatePin;

    EditText userVal;

    Button button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9,
            buttonCreate, buttonClear;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        pinBox0 = (TextView) findViewById(R.id.pinBox0);
        pinBox1 = (TextView) findViewById(R.id.pinBox1);
        pinBox2 = (TextView) findViewById(R.id.pinBox2);
        pinBox3 = (TextView) findViewById(R.id.pinBox3);
        failedCreatePin = (TextView) findViewById(R.id.failedCreatePin);

        userVal = (EditText) findViewById(R.id.editNewUsername);

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
        buttonCreate = (Button) findViewById(R.id.buttonCreate);

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
        buttonCreate.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userExist = false;
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
    } // end onOptionItemSelected method

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClear:
                clearData();
                break;
            case R.id.buttonCreate:
                if (readyPin == true & (userVal.getText().toString().length() > 0)) {
                    Intent intent = new Intent(v.getContext(), login_menu.class);
                    saveData();
                    clearData();
                    startActivity(intent);
                } else if (userVal.getText().toString().length() == 0) {
                    failedCreatePin.setText(failUser);
                    clearData();
                } else {
                    failedCreatePin.setText(failPin);
                    clearData();
                }
                break;
            default:
                failedCreatePin.setText("");
                Button pressedButton = (Button) v;
                if (userEntry.length() < PIN_LENGTH) {
                    userEntry = userEntry + pressedButton.getText();
                    switch (userEntry.length() - 1) {
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
                            readyPin = true;
                            break;
                    }
                }
        }
    } // end onClick method

    public void clearData () {
        if (userEntry.length() > 0) {
            pinBox0.setText("");
            pinBox1.setText("");
            pinBox2.setText("");
            pinBox3.setText("");
            userEntry = "";
            readyPin = false;
        }
    } // end clearData method

    public void saveData() {
        this.userName = userVal.getText().toString();
        this.pinValue = userEntry;
        this.userExist = true;
        SharedPreferences.Editor pinSaver = sharedPreferences.edit();
        pinSaver.putString("userName",this.userName);
        pinSaver.putString("pinValue",this.pinValue);
        pinSaver.putBoolean("userExist",this.userExist);
        pinSaver.commit();
    }

    public String getPinValue () {
        return this.pinValue;
    }

    public String getUserName () {
        return this.userName;
    }

    public boolean getUserExist() {
        return this.userExist;
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

} // end CreateNewUser class
