package com.Tokenverifier;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


/**
 * NOTICE:
 * Most of these connection threads are the same,
 * which is very simple and easy to understand.
 * Please understand by yourself.
 * */
public class UpdateInfoThread extends Thread{
    @Override
    public void run() {
        HttpsURLConnection connection;
        try {
            connection = (HttpsURLConnection) new URL(Api.url + "/api/getInfo/"+
                    UserInfo.username).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Via", UserInfo.token);
            connection.setConnectTimeout(2 * 1000);
            connection.connect();

            JSONObject jsonObject = Api.getJson(connection);
            if(!jsonObject.getString("id").equals("") &&
                    !jsonObject.getString("username").equals("") &&
                    !jsonObject.getString("info").equals("")){
                UserInfo.info = jsonObject.getString("info");
                UserInfo.id = jsonObject.getString("id");
            }
            connection.disconnect();

            connection = (HttpsURLConnection) new URL(Api.url + "/api/checkIn/"+
                    UserInfo.username).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Via", UserInfo.token);
            connection.setConnectTimeout(2 * 1000);
            connection.connect();
            jsonObject = Api.getJson(connection);
            Log.e("check", jsonObject.toString());
            connection.disconnect();




            connection = (HttpsURLConnection) new URL(Api.url + "/api/getProperty/"+
                    UserInfo.username).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Via", UserInfo.token);
            connection.setConnectTimeout(2 * 1000);
            connection.connect();

            jsonObject = Api.getJson(connection);
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
