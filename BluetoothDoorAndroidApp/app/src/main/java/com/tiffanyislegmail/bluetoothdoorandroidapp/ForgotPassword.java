/*----------------------------------------------------------------
 * Author:       Tiffany Le and Thuan Chu
 * File Name:    ForgotPassword.java
 * Created On:   09/10/2014
 * Last updated: 12/03/2014
 *
 * Description:  Prompts the user to enter answers to security
 *               questions that were previously saved when the
 *               user created a new account. If the user enters
 *               the information correctly, it will redirect to
 *               create a new account.
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

public class ForgotPassword extends Activity implements View.OnClickListener {

    EditText answerVacation, answerMakeCar, answerModelCar;
    Button   buttonReset;

    String   failMsg = "Incorrect answers. Attempts left: ";
    int      counter = 3;

    boolean  correctAnswer = false;

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        answerVacation = (EditText) findViewById(R.id.answer_vacation);
        answerMakeCar  = (EditText) findViewById(R.id.answer_makeCar);
        answerModelCar = (EditText) findViewById(R.id.answer_modelCar);

        buttonReset    = (Button) findViewById(R.id.buttonTry);
        buttonReset.setOnClickListener(this);
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

    /* This method checks if the answers the user entered are valid. The user has
     * three attempts to enter valid answers. If the user uses all of the attempts,
     * the app will lock itself for 5 minutes.
     */
    @Override
    public void onClick(View v) {
        correctAnswer = compareAnswers();
        /* If answers are correct, proceed to reset password */
        if (correctAnswer) {
            Intent intent = new Intent(v.getContext(), CreateNewUser.class);
            sharedPrefs.clearSavedPrefs(context);
            clearData();
            startActivity(intent);
        }
        /* If answers are wrong, then lock the app after 3 attempts */
        else {
            counter--;
            if (counter == 0) {
                Toast.makeText(context,"No more attempts. Redirecting...",Toast.LENGTH_SHORT).show();
                sharedPrefs.setLockApp(context);
                Intent intent = new Intent(v.getContext(), login_menu.class);
                clearData();
                startActivity(intent);
            }
            Toast.makeText(context,failMsg + counter,Toast.LENGTH_SHORT).show();

        }
    }

    /* This method compares user entries to saved data. If data does not match, then the app will
     * lock the user out. If the data matches, then it will allow the user to reset password */
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

    /* Clear data on screen */
    public void clearData() {
        answerVacation.setText("");
        answerMakeCar.setText("");
        answerModelCar.setText("");
        correctAnswer = false;
    }
}
