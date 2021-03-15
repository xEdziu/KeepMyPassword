package me.goral.keepmypassword.utils.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import java.net.HttpURLConnection;
import me.goral.keepmypassword.utils.asyncTasks.functions.OpenHTTP;
import me.goral.keepmypassword.utils.asyncTasks.functions.RequestHTTP;

public class GetContentDB extends AsyncTask<String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public GetContentDB (AsyncResponse delegate) {this.delegate = delegate;}

    ProgressBar pb;
    Activity parentActivity;

    public void setProgressBar(ProgressBar progressBar) {this.pb = progressBar;}

    public void setParentActivity(Activity parentActivity) {this.parentActivity = parentActivity;}

    @Override
    public void onPreExecute(){
        parentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            HttpURLConnection httpConn = OpenHTTP.prepareConnection("http://192.168.77.17/kmpApi/getContent.php");
            String jsonInputString = "{\"uid\": \"" + strings[0] +  "\" }";
            RequestHTTP.sendData(jsonInputString, httpConn);
            return RequestHTTP.receiveData(httpConn);

        } catch (Exception e){
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
