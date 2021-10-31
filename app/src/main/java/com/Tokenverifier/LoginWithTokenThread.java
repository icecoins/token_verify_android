package com.Tokenverifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginWithTokenThread extends Thread{
    @Override
    public void run() {
        HttpURLConnection connection;
        try {
            //connect to remote server via special interface
            connection = (HttpURLConnection) new URL(Api.url + "/api/getUser/"+
                    UserInfo.username).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            //"Via" is the name of token, u can change it,
            //but must consistent with the server
            connection.setRequestProperty("Via", UserInfo.token);
            connection.setConnectTimeout(1000);
            //if connect failed, run() method will jump to IOEXCEPTION
            connection.connect();

            //if the token is available, it's expected to get
            //JSONObject with message as 200 inside
            JSONObject jsonObject = Api.getJson(connection);
            if(jsonObject.getString("message").equals("200")){
                UserInfo.loginSucceed = true;
            }else{
                //token expired
                Api.saveData(Api.CLEAR_TOKEN);
                UserInfo.token = "";
            }
            connection.disconnect();
        } catch (IOException e) {
            //connect failed
            e.printStackTrace();
        }catch (JSONException e){
            //token expired
            Api.saveData(Api.CLEAR_TOKEN);
            UserInfo.loginSucceed = false;
            UserInfo.token = "";
            e.printStackTrace();
        }
    }
}