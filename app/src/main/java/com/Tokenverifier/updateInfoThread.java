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
public class updateInfoThread extends Thread{
    @Override
    public void run() {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(api.url + "/api/getInfo/"+
                    UserInfo.username).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Via", UserInfo.token);
            connection.setConnectTimeout(2 * 1000);
            connection.connect();

            JSONObject jsonObject = api.getJson(connection);
            if(!jsonObject.getString("id").equals("") &&
                    !jsonObject.getString("username").equals("") &&
                    !jsonObject.getString("info").equals("")){
                UserInfo.info = jsonObject.getString("info");
                UserInfo.id = jsonObject.getString("id");
            }
            connection.disconnect();


            connection = (HttpURLConnection) new URL("http://192.168.137.1:8848/api/getProperty/"+
                    UserInfo.username).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Via", UserInfo.token);
            connection.setConnectTimeout(2 * 1000);
            connection.connect();

            jsonObject = api.getJson(connection);
            if(!jsonObject.getString("coin").equals("") &&
                    !jsonObject.getString("level").equals("") &&
                    !jsonObject.getString("exp").equals("")){
                UserInfo.coin = jsonObject.getString("coin");
                UserInfo.level = jsonObject.getString("level");
                UserInfo.exp = jsonObject.getString("exp");
            }
            connection.disconnect();

        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
    }
}
