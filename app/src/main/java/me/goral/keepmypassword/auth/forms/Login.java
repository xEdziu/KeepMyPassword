package me.goral.keepmypassword.auth.forms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.vdx.designertoast.DesignerToast;

import java.util.concurrent.Executor;

import me.goral.keepmypassword.MainActivity;
import me.goral.keepmypassword.R;
import me.goral.keepmypassword.main.LoggedMain;
import me.goral.keepmypassword.utils.asyncTasks.CheckAndroidID;
import me.goral.keepmypassword.utils.asyncTasks.LoginDB;
import me.goral.keepmypassword.utils.FormsClasses;

public class Login extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private String databaseUser = null;
    private String databaseId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Bundle bundle = getIntent().getExtras();

        final String actionBarColor = bundle.getString("colorString");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(actionBarColor));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        final String androidID = bundle.getString("androidID");
        final Activity thisActivity = this;
        final Context context = getApplicationContext();

        //login using username and password
        final EditText userNameEditText = findViewById(R.id.loginUsername);
        final EditText passwordEditText = findViewById(R.id.loginPassword);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        Button btnLoginDB = findViewById(R.id.btnLoginDB);
        btnLoginDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(userNameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                //execute asynchronous task in background and wait for response
                FormsClasses.LoginParams params = new FormsClasses.LoginParams(username, password, androidID);
                LoginDB async = new LoginDB(new LoginDB.AsyncResponse(){
                    @Override
                    public void processFinish(String output) {
                        if (!output.equals("Incorrect data!")){
                            String[] result = output.split(";");
                            databaseId = result[0];
                            databaseUser = result[1];
                            Intent intent = new Intent(Login.this, LoggedMain.class);
                            intent.putExtra("username", databaseUser);
                            intent.putExtra("uid", databaseId);
                            startActivity(intent);
                        } else {
                            DesignerToast.Warning(getApplicationContext(), "Warning", output, Gravity.BOTTOM, Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                        }
                    }
                });
                async.setProgressBar(progressBar);
                async.getParentActivity(thisActivity);
                async.getAllInputs(userNameEditText, passwordEditText);
                async.execute(params);
            }
        });

        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(Login.this, LoggedMain.class);
                intent.putExtra("username", databaseUser);
                intent.putExtra("uid", databaseId);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("BIOMETRIC LOGIN")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("CANCEL")
                .build();

        // login using biometrics
        Button btnBiometricLogin = findViewById(R.id.btnBiometricLogin);
        btnBiometricLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAndroidID async = new CheckAndroidID(new CheckAndroidID.AsyncResponse(){
                    @Override
                    public void processFinish(String output) {
                        String[] result = output.split(";");
                        if (!result[0].equals("TRUE")) DesignerToast.Info(getApplicationContext(), "Info",
                                "Biometric authentication is now unavailable." +
                                " Please login using username and password", Gravity.BOTTOM, Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
                        else {
                            databaseUser = result[1];
                            databaseId = result[2];
                        }
                    }
                });
                async.execute(androidID);

                if (databaseUser != null){
                    biometricPrompt.authenticate(promptInfo);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }

}
