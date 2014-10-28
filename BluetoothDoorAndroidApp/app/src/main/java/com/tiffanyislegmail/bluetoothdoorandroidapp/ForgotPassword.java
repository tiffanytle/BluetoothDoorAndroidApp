package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ForgotPassword extends Activity implements View.OnClickListener {

    EditText answerVacation, answerMakeCar, answerModelCar;
    TextView failedRetrieve;
    Button buttonReset;

    String failMsg = "Incorrect answers. Attempts left: ";
    int counter = 3;

    boolean correctAnswer = false;

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        answerVacation = (EditText) findViewById(R.id.answer_vacation);
        answerMakeCar = (EditText) findViewById(R.id.answer_makeCar);
        answerModelCar = (EditText) findViewById(R.id.answer_modelCar);

        failedRetrieve = (TextView) findViewById(R.id.failedRetrieve);

        buttonReset = (Button) findViewById(R.id.buttonTry);
        buttonReset.setOnClickListener(this);

        failedRetrieve.setText("");
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
        correctAnswer = compareAnswers();
        if (correctAnswer) {
            Intent intent = new Intent(v.getContext(), CreateNewUser.class);
            sharedPrefs.clearSavedPrefs(context);
            clearData();
            startActivity(intent);
        }
        else {
            counter--;
            if (counter == 0) {
                Toast.makeText(context,"No more attempts. Redirecting...",Toast.LENGTH_SHORT).show();
                sharedPrefs.setLockApp(context);
                Intent intent = new Intent(v.getContext(), login_menu.class);
                clearData();
                startActivity(intent);
            }
            failedRetrieve.setText(failMsg + counter);
        }

    }

    public boolean compareAnswers() {
        boolean correctCompare = false;
        String tryVacation = answerVacation.getText().toString();
        String tryCarMake = answerMakeCar.getText().toString();
        String tryCarModel = answerModelCar.getText().toString();
        String SavedVacation = sharedPrefs.getSecurityVacation(context);
        String SavedCarMake = sharedPrefs.getSecurityCarMake(context);
        String SavedCarModel = sharedPrefs.getSecurityCarModel(context);

        if ((tryVacation.equalsIgnoreCase(SavedVacation)) &
                (tryCarMake.equalsIgnoreCase(SavedCarMake)) &
                (tryCarModel.equalsIgnoreCase(SavedCarModel)))
            correctCompare = true;
        return correctCompare;
    }

    public void clearData() {
        answerVacation.setText("");
        answerMakeCar.setText("");
        answerModelCar.setText("");
        correctAnswer = false;
    }
}
