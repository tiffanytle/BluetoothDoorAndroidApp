/*----------------------------------------------------------------
 * Author:       Tiffany Le and Thuan Chu
 * File Name:    SecurityQuestions.java
 * Created On:   10/21/2014
 * Last updated: 12/03/2014
 *
 * Description:  Prompts the user to answer security questions
 *               for when the user forgets pin and needs to
 *               reset the account. The questions include first
 *               vacation and make/model of first car.
 *----------------------------------------------------------------*/
package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecurityQuestions extends Activity implements View.OnClickListener {

    EditText  secQuestVacation, secQuestMakeCar, secQuestModelCar;
    Button    buttonSave;

    boolean secReady = false;

    String failMsg   = "Please answer all security questions.";

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_questions);

        secQuestVacation = (EditText) findViewById(R.id.secQuest_vacation);
        secQuestMakeCar  = (EditText) findViewById(R.id.secQuest_makeCar);
        secQuestModelCar = (EditText) findViewById(R.id.secQuest_modelCar);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);


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

    /* Check if security questions were answered correctly */
    @Override
    public void onClick(View v) {
        secReady = securityReady();
        if (secReady) {
            sharedPrefs.saveSecurity(context,secQuestVacation.getText().toString(),
                    secQuestMakeCar.getText().toString(),secQuestModelCar.getText().toString() );
            clearData();
            Intent intent = new Intent(v.getContext(), SplashScreen.class);
            startActivity(intent);
        }
        else
            Toast.makeText(context, failMsg, Toast.LENGTH_SHORT).show();
    }

    /* Method to check if security questions were answered */
    public boolean securityReady() {
        boolean realAnswers = false;
        if (secQuestVacation.getText().toString().length() > 0 &
                secQuestMakeCar.getText().toString().length() > 0 &
                secQuestModelCar.getText().toString().length() > 0)
            realAnswers = true;
        return realAnswers;
    }

    /* Clear data on screen */
    public void clearData() {
        secQuestVacation.setText("");
        secQuestMakeCar.setText("");
        secQuestMakeCar.setText("");
        secQuestMakeCar.setText("");
        secQuestModelCar.setText("");
    }
}
