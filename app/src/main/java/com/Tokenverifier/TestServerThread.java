package com.Tokenverifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * NOTICE:
 * Most of these connection threads are the same,
 * which is very simple and easy to understand.
 * Please understand by yourself.
 * */
public class TestServerThread extends Thread{
    @Override
    public void run(){
        HttpURLConnection connection;
        while(!UserInfo.isServerAvailable){
            if(Thread.currentThread().isInterrupted()){
                break;
            }
            try {
                connection = (HttpURLConnection) new URL(Api.url + "/api/time").openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setConnectTimeout(1000);
                connection.connect();

                JSONObject jsonObject = Api.getJson(connection);
                if(!jsonObject.getString("time").equals("")){
                    UserInfo.isServerAvailable = true;
                    break;
                }else {
                    Thread.sleep(1000);
                }
                connection.disconnect();
            } catch (IOException | InterruptedException | JSONException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
