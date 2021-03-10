package me.goral.keepmypassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.goral.keepmypassword.auth.forms.Login;
import me.goral.keepmypassword.auth.forms.Register;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String android_id;
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // checking if app can use permission and if not - ask for them
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET}, 1);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.USE_BIOMETRIC}, 1);
        }

        // redirect to register form
        Button btnRegister = findViewById(R.id.btnRegistrationDB);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                intent.putExtra("androidID", android_id);
                startActivity(intent);
            }
        });

        //redirect to login form
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.putExtra("androidID", android_id);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null)
            savedInstanceState = null;
    }
    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}