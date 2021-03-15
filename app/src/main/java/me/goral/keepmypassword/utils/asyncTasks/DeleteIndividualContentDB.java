package me.goral.keepmypassword.utils.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.WindowManager;
import java.net.HttpURLConnection;
import me.goral.keepmypassword.utils.asyncTasks.functions.OpenHTTP;
import me.goral.keepmypassword.utils.asyncTasks.functions.RequestHTTP;

public class DeleteIndividualContentDB extends AsyncTask<String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    Activity parentActivity;

    public AsyncResponse delegate = null;
    public DeleteIndividualContentDB (AsyncResponse delegate) {this.delegate = delegate;}

    public void setParentActivity(Activity parentActivity) {this.parentActivity = parentActivity;}

    @Override
    public void onPreExecute(){
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            HttpURLConnection httpConn = OpenHTTP.prepareConnection("http://192.168.77.17/kmpApi/deleteOneContent.php");
            String json = "{\"rid\": \"" + strings[0] +  "\" }";
            RequestHTTP.sendData(json, httpConn);
            return  RequestHTTP.receiveData(httpConn);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        parentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        delegate.processFinish(s);
    }
}

