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

public class LoginDB extends AsyncTask<FormsClasses.LoginParams, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }
    public AsyncResponse delegate = null;

    public LoginDB (AsyncResponse delegate){
        this.delegate = delegate;
    }

    ProgressBar pb;
    Activity parentActivity;
    EditText username, password;

    public void setProgressBar(ProgressBar progressBar){
        this.pb = progressBar;
    }

    public void getParentActivity(Activity parentActivity){
        this.parentActivity = parentActivity;
    }

    public void getAllInputs (EditText... editTexts){
        this.username = editTexts[0];
        this.password = editTexts[1];
    }

    public void clearInputs(EditText username, EditText password){
        username.setText("");
        password.setText("");
        password.clearFocus();
    }

    @Override
    public void onPreExecute(){
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        super.onPreExecute();
        clearInputs(username, password);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(FormsClasses.LoginParams... params) {
        //send POST request
        try {
            HttpURLConnection httpConn = OpenHTTP.prepareConnection("http://192.168.77.17/kmpApi/login.php");
            String jsonInputString = "{\"username\": \"" + params[0].username +"\",  \"password\": \""
                    + params[0].password + "\" }";
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
        parentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        delegate.processFinish(s);
    }
}
