package me.goral.keepmypassword.utils.asyncTasks;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import me.goral.keepmypassword.utils.asyncTasks.functions.OpenHTTP;
import me.goral.keepmypassword.utils.asyncTasks.functions.RequestHTTP;

public class CheckAndroidID extends AsyncTask <String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }
    public AsyncResponse delegate = null;

    public CheckAndroidID(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            HttpURLConnection httpConn = OpenHTTP.prepareConnection("");
            String json = "{ \"androidID\": \"" + strings[0] + "\" }";
            RequestHTTP.sendData(json, httpConn);
            return RequestHTTP.receiveData(httpConn);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }
}
