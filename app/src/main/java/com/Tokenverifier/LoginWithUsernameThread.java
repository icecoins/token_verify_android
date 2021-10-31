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
public class LoginWithUsernameThread extends Thread{
    @Override
    public void run() {
        HttpURLConnection connection;
        try {
            String name = api.convertMD5(UserInfo.username), psw = api.convertMD5(UserInfo.password);
            connection = (HttpURLConnection) new URL( api.url + "/api/getUser/"+
                    name +"/"+ psw).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setConnectTimeout(1000);
            connection.connect();

            JSONObject jsonObject = api.getJson(connection);
            if(!jsonObject.getString("token").equals("")){
                UserInfo.token = jsonObject.getString("token");
                api.saveData(api.SAVE_TOKEN);
                UserInfo.loginSucceed = true;
            }
            connection.disconnect();
        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
    }
}
