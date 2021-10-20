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
*   + ```Toasts.java```
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

> Note: Below there are described only the most essential files that fulfill specific function that identifies its' usage from other files.

#### MainActivity.java

The first activity file when App is opened is responsible for:
* Reading unique **AndroidID** and saving it into variable:

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

* Checking if App has permissions and if not - asks for them:

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

#### Login.java & Register.Java

* Both this files fulfill a similar function - collect data from EditTexts, e.g.:

```java
final EditText userNameEditText = findViewById(R.id.loginUsername);
String username = String.valueOf(userNameEditText.getText());
```
* And pass values to files responsible for handling asynchronous tasks: ```LoginDB.java``` and ```RegisterDB.java```:

```java
LoginDB async = //...
async.execute(params);
```
* For more information, look at ```Login.java``` or ```Register.java``` file.

#### LoggedMain.java

* This is the main activity after logging in.

* It has special ```<FrameLayout>``` tag that holds and displays Fragments (```AboutFragment.java``` and so on) iniside this tag:

```xml
<FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/fragment_container">
</FrameLayout>
```

* Moreover it hosts whole **Navigation Drawer** and uses ```switch``` function to change currently displayed Fragments :

```xml
<com.google.android.material.navigation.NavigationView
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:layout_gravity="start"
    android:background="@color/colorBackground"
    app:itemTextColor="@color/primaryText"
    android:id="@+id/nav_view"
    app:headerLayout="@layout/nav_header"
    app:menu="@menu/drawer_menu"/>
```

```java
drawer = findViewById(R.id.drawer_layout);
NavigationView navigationView = findViewById(R.id.nav_view);
navigationView.setNavigationItemSelectedListener(this);

ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
drawer.addDrawerListener(toggle);
toggle.syncState();
```
For ```switch``` usage look into ```LoggedMain.java``` file.

#### PasswordsFragment.java

This file generates main feature of the App - Descriptions and Passwords storaged in external database.

> Note: Offline mode not suported yet! (Status for 16.03.2021)

* Main functionality is to generate header, content and buttons to refresh content, add and delete records to and from database:

```java
table = view.findViewById(R.id.table);
setHeader(table);
setContent();
```

Buttons are sending asynchronous requests to perform intended action.
For full body of functions and buttons funcionality look into ```PasswordsFragment.java``` file.

#### SettingsFragment.java

This unique fragment is responsible for changing Theme preferences - Dark Mode, Night Mode:
![Whole Themes Screenshot](https://github.com/xEdziu/KeepMyPassword/blob/main/whole.png)

As well as deleting passwords and user account from database.
For full code visit page with file ```SettingsFragment.java```

#### AsyncTasks

Each AsyncTask file is responsible for creating connection with external server using ```OpenHTTP.class```, and then sending POST request and recieving callback from external server using ```RequestHTTP.java``` class:

```java
public class RequestHTTP {
    //sending request
    public static void sendData(String json, HttpURLConnection urlConnection){
        try(OutputStream os = urlConnection.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //recieving data from request
    public static String receiveData(HttpURLConnection urlConnection){
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
````
## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## Bibliography

For bibliography check ```biblio.txt``` file.

## Contact

You can contact me via email: 
<adrian.goral@gmail.com>

## License
KeepMyPassword by Adrian Goral is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International.
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/

## Also check it out
https://youtu.be/dQw4w9WgXcQ?t=43
