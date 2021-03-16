# KeepMyPassword - Official Documentation

## Summary

This application is designed to store passwords for free in one app.
It's eniterly created from scrap and developed to version 1.1.0 in one month from begging date.

## Files

### Structure of main files

* ```MainActivity.java```
* _```auth.forms```
*  + ```Login.java```
*  + ```Register.java```
* _ ```main```
*   + ```AboutFragment.java```
*   + ```CreditsFragment.java```
*   + ```LoggedMain.java```
*   + ```PasswordsFragment.java```
*   + ```SettingsFragment.java```
* _```utils```
*   + ```FormsClasses.java```
*   + ```_asyncTasks```
*   +   - ```CheckAndroidID.java```
*   +   - ```DeleteIndividualContentDB.java```
*   +   - ```GetContentDB.java```
*   +   - ```LoginDB.java```
*   +   - ```ManageAccount.java```
*   +   - ```RegistrationDB.java```
*   +   - ```SetContentDB.java```
*   + _```functions```
*   +   - ```OpenHTTP.java```
*   +   - ```RequestHTTP.java```

I decided to omit XML layouts file, because they are easily asccesible in ```res``` folder.

#### MainActivity.java

The landing file is responsible for:
* Reading unique AndroidID and saving it into variable:

```java
final String android_id;
android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
    Settings.Secure.ANDROID_ID);

```

* Reading theme from **SharedPreferences** and setting it up as well as setting colorString for ActionBar:

```java
SharedPreferences sharedPreferences = getSharedPreferences("night", 0);
final Boolean flag = sharedPreferences.getBoolean("night_mode", false);
ColorDrawable colorDrawable;
final String colorString;

if (flag) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    colorString = "#0E0E0E";
} else {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    colorString = "#C62828";
}
colorDrawable = new ColorDrawable(Color.parseColor(colorString));
//-------------
ActionBar actionBar = getSupportActionBar();
actionBar.setDisplayShowTitleEnabled(true);
actionBar.setBackgroundDrawable(colorDrawable);
```
* Checking if App can use permissions and if not - asks for them:

```java
if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED ){
    ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.INTERNET}, 1);
    ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.USE_BIOMETRIC}, 1);
}
```
* Redirecting to login and register forms with extra arguments in bundle:

```java
// redirect to register form
Button btnRegister = findViewById(R.id.btnRegistrationDB);
btnRegister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        intent.putExtra("androidID", android_id);
        intent.putExtra("colorString", colorString);
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
        intent.putExtra("colorString", colorString);
        startActivity(intent);
    }
});
```




