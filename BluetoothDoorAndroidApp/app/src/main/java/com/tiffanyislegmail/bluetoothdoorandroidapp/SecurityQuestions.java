package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecurityQuestions extends Activity implements View.OnClickListener {

    EditText secQuestVacation, secQuestMakeCar, secQuestModelCar;
    TextView failedSecurity;
    Button buttonSave;

    boolean secReady = false;

    String failMsg = "Please answer all security questions.";

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_questions);

        secQuestVacation = (EditText) findViewById(R.id.secQuest_vacation);
        secQuestMakeCar = (EditText) findViewById(R.id.secQuest_makeCar);
        secQuestModelCar = (EditText) findViewById(R.id.secQuest_modelCar);

        failedSecurity = (TextView) findViewById(R.id.failedSecurity);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        failedSecurity.setText("");

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
    public void onClick(View v) {
        secReady = securityReady();
        if (secReady) {
            sharedPrefs.saveSecurity(context,secQuestVacation.getText().toString(),
                    secQuestMakeCar.getText().toString(),secQuestModelCar.getText().toString() );
            clearData();
            Intent intent = new Intent(v.getContext(), login_menu.class);
            startActivity(intent);
        }
        else
            failedSecurity.setText(failMsg);
    }

    public boolean securityReady() {
        boolean realAnswers = false;
        if (secQuestVacation.getText().toString().length() > 0 &
                secQuestMakeCar.getText().toString().length() > 0 &
                secQuestModelCar.getText().toString().length() > 0)
            realAnswers = true;
        return realAnswers;
    }

    public void clearData() {
        secQuestVacation.setText("");
        secQuestMakeCar.setText("");
        secQuestMakeCar.setText("");
        secQuestMakeCar.setText("");
        secQuestModelCar.setText("");
    }
}
