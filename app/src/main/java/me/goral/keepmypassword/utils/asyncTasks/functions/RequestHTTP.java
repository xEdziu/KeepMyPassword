package me.goral.keepmypassword.utils.asyncTasks.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

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
