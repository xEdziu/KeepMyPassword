package me.goral.keepmypassword.auth.forms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import me.goral.keepmypassword.MainActivity;
import me.goral.keepmypassword.R;
import me.goral.keepmypassword.utils.Toasts;
import me.goral.keepmypassword.utils.asyncTasks.RegistrationDB;
import me.goral.keepmypassword.utils.FormsClasses;

public class Register extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bundle bundle = getIntent().getExtras();

        final String actionBarColor = bundle.getString("colorString");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(actionBarColor));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(colorDrawable);
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
                    Toasts.makeErrorToast("Passwords are not the same!", getApplicationContext());
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

        if (!numberFlag)
            Toasts.makeErrorToast("Password have to contain at least 1 number", getApplicationContext());
        else if (!capitalFlag)
            Toasts.makeErrorToast("Password have to contain at least 1 upper case letter", getApplicationContext());
        else if (!lowerCaseFlag)
            Toasts.makeErrorToast("Password have to contain at least 1 lower case letter", getApplicationContext());
        else if (!counterFlag)
            Toasts.makeErrorToast("Password have to contain at least 8 characters long", getApplicationContext());
        return false;
    }

}
