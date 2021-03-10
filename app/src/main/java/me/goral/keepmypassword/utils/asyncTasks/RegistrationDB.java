package me.goral.keepmypassword.utils.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.net.HttpURLConnection;

import me.goral.keepmypassword.utils.asyncTasks.functions.OpenHTTP;
import me.goral.keepmypassword.utils.asyncTasks.functions.RequestHTTP;
import me.goral.keepmypassword.utils.FormsClasses;

public class RegistrationDB extends AsyncTask<FormsClasses.RegisterParams, Void, String> {

    ProgressBar pb;
    Activity parentActivity;
    EditText username, email, password, password2;

    public void setProgressBar(ProgressBar progressBar){
        this.pb = progressBar;
    }

    public void getParentActivity(Activity parentActivity){
        this.parentActivity = parentActivity;
    }

    public void getAllInputs (EditText... editTexts){
        this.username = editTexts[0];
        this.email = editTexts[1];
        this.password = editTexts[2];
        this.password2 = editTexts[3];

    }

    public void clearInputs(EditText username, EditText email, EditText password){
        username.setText("");
        email.setText("");
        password.setText("");
        password.clearFocus();
    }

    @Override
    public void onPreExecute(){
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
              WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        super.onPreExecute();
        clearInputs(username, email, password);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(FormsClasses.RegisterParams... params) {
        //send POST request
        try {
            HttpURLConnection httpConn = OpenHTTP.prepareConnection("http://192.168.77.17/kmpApi/register.php");
            String jsonInputString = "{ \"username\": \"" + params[0].username +"\", \"email\": \"" + params[0].email
                    + "\", \"password\": \"" + params[0].password + "\", \"androidID\": \"" + params[0].androidID + "\" }";
            //send data to php
            RequestHTTP.sendData(jsonInputString, httpConn);
            //receive data from php
            return RequestHTTP.receiveData(httpConn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        pb.setVisibility(View.GONE);
        Toast.makeText(parentActivity, s, Toast.LENGTH_SHORT).show();
        parentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        super.onPostExecute(s);
    }
}