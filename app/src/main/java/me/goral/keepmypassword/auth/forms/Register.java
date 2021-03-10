package me.goral.keepmypassword.auth.forms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import me.goral.keepmypassword.MainActivity;
import me.goral.keepmypassword.R;
import me.goral.keepmypassword.utils.asyncTasks.RegistrationDB;
import me.goral.keepmypassword.utils.FormsClasses;

public class Register extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bundle bundle = getIntent().getExtras();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        final Activity thisActivity = this;
        final String androidID = bundle.getString("androidID");
        final EditText userNameEditText = findViewById(R.id.registerUsername);
        final EditText emailEditText = findViewById(R.id.registerEmail);
        final EditText passwordEditText = findViewById(R.id.registerPassword);
        final EditText password2EditText = findViewById(R.id.registerPassword2);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        Button btnRegisterDB = findViewById(R.id.btnRegistrationDB);

        btnRegisterDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(userNameEditText.getText());
                String email = String.valueOf(emailEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                String password2 = String.valueOf(password2EditText.getText());
                //check if passwords are the same
                if (!password.equals(password2)){
                    Toast.makeText(getApplicationContext(), "Passwords are not the same!", Toast.LENGTH_SHORT ).show();
                    return;
                }
                //validate password input
                if(!checkString(password)) return;
                //execute asynchronous task in background and wait for response
                FormsClasses.RegisterParams params = new FormsClasses.RegisterParams(username, email, password, password2, androidID);
                RegistrationDB async = new RegistrationDB();
                async.setProgressBar(progressBar);
                async.getParentActivity(thisActivity);
                async.getAllInputs(userNameEditText, emailEditText, passwordEditText, password2EditText);
                async.execute(params);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }

    public boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean counterFlag = false;
        if (str.length() >= 8) counterFlag = true;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag && counterFlag)
                return true;
        }

        if (!numberFlag) Toast.makeText(getApplicationContext(), "Password have to contain at " +
                "least 1 number", Toast.LENGTH_SHORT).show();
        else if (!capitalFlag) Toast.makeText(getApplicationContext(), "Password have to contain at " +
                "least 1 capital letter", Toast.LENGTH_SHORT).show();
        else if (!lowerCaseFlag) Toast.makeText(getApplicationContext(), "Password have to contain at " +
                "least 1 lower case letter", Toast.LENGTH_SHORT).show();
        else if (!counterFlag) Toast.makeText(getApplicationContext(), "Password have to be at " +
                "least 8 characters long", Toast.LENGTH_SHORT).show();
        return false;
    }

}
