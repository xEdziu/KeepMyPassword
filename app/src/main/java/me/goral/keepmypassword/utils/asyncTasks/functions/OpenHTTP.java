package me.goral.keepmypassword.utils.asyncTasks.functions;

import java.net.HttpURLConnection;
import java.net.URL;

public class OpenHTTP {
    //provide URL to external file that you want make POST request to
   public static HttpURLConnection prepareConnection(String URL){
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
