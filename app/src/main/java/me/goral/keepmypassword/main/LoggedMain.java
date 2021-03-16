package me.goral.keepmypassword.main;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.navigation.NavigationView;

import me.goral.keepmypassword.MainActivity;
import me.goral.keepmypassword.R;

public class LoggedMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public String username;
    public String uid;

    Boolean flag = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("night", 0);
        flag = sharedPreferences.getBoolean("night_mode", false);

        if (flag) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_main);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        uid = bundle.getString("uid");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            PasswordsFragment pf = new PasswordsFragment();
            pf.setArguments(generateFragmentBundle(username, uid));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    pf, "PasswordFragment").commit();
            navigationView.setCheckedItem(R.id.nav_passwords);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_passwords:
                PasswordsFragment pf = new PasswordsFragment();
                pf.setArguments(generateFragmentBundle(username, uid));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        pf).commit();
                break;
            case R.id.nav_settings:
                SettingsFragment sf = new SettingsFragment();
                sf.setArguments(generateFragmentBundle(username, uid));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        sf).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
            case R.id.nav_credits:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreditsFragment()).commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(LoggedMain.this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else
            Toast.makeText(getApplicationContext(), "To log out, use button in the menu", Toast.LENGTH_LONG).show();
    }

    private Bundle generateFragmentBundle(String username, String uid) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("uid", uid);
        return bundle;
    }
}
