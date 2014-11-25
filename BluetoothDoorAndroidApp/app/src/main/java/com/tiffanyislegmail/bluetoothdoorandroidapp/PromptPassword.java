package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PromptPassword extends Activity implements View.OnClickListener {

    String userEntry = "";
    String pinValue = "";
    String failMsg = "Incorrect PIN. Attempts left: ";

    int counter = 3;

    boolean correctPassword = false;

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;

    final int PIN_LENGTH = 4;

    TextView pinBox0, pinBox1, pinBox2, pinBox3, userNameBox;

    String userName;

    Button button0, button1, button2, button3, button4,
            button5, button6, button7, button8, button9,
            buttonLogin, buttonClear, buttonForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_password);

        pinBox0 = (TextView) findViewById(R.id.pinBox0);
        pinBox1 = (TextView) findViewById(R.id.pinBox1);
        pinBox2 = (TextView) findViewById(R.id.pinBox2);
        pinBox3 = (TextView) findViewById(R.id.pinBox3);
        userNameBox = (TextView) findViewById(R.id.displayUserName);

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
        buttonForget = (Button) findViewById(R.id.forgotPassword);

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
        buttonForget.setOnClickListener(this);


        pinValue = sharedPrefs.getPinValue(context);
    }

    public void onClick (View v) {
        switch (v.getId()) {
            /* Clear data on screen */
            case R.id.buttonClear:
                clearData();
                break;
            /* Check if entered pin is correct to proceed login */
            case R.id.buttonLogin:
                if (checkAppLocked() == true) {
                    if (correctPassword == true) {
                        Intent intent = new Intent(v.getContext(), MainMenu.class);
                        clearData();
                        startActivity(intent);
                    } else {
                        clearData();
                        counter--;
                        if (counter == 0) {
                            Toast.makeText(context, "No more attempts. Redirecting...", Toast.LENGTH_SHORT).show();
                            sharedPrefs.setLockApp(context);
                            Intent intent = new Intent(v.getContext(), SplashScreen.class);
                            startActivity(intent);
                        }
                        Toast.makeText(context, failMsg + counter, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(context, "Sorry, you are locked out.", Toast.LENGTH_SHORT).show();
                break;
            /* Redirect user to forget password page */
            case R.id.forgotPassword:
                if (checkAppLocked() == true) {
                    Intent intent = new Intent(v.getContext(), ForgotPassword.class);
                    clearData();
                    startActivity(intent);
                } else
                    Toast.makeText(context, "Sorry, you are locked out.", Toast.LENGTH_SHORT).show();
                break;
            /* User enters pin value */
            default:
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

    /* Method to clear data on screen */
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

    /* Method to check if app is already locked */
    public boolean checkAppLocked() {
        boolean isAppLocked, notLocked = false;
        isAppLocked = sharedPrefs.checkLockApp(context);
        if (isAppLocked == false)
            notLocked = true;
        return notLocked;
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

    /* Do not allow user to go back */
    @Override
    public void onBackPressed() {
    }
}
