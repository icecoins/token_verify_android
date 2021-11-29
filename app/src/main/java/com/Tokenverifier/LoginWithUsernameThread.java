package com.Tokenverifier;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * NOTICE:
 * Most of these connection threads are the same,
 * which is very simple and easy to understand.
 * Please understand by yourself.
 * */
public class LoginWithUsernameThread extends Thread{
    @Override
    public void run() {
        HttpsURLConnection connection;
        OutputStreamWriter outputStreamWriter;
        JSONObject sendMsg, recvMsg;
        try {
            sendMsg = new JSONObject();
            sendMsg.put("username", UserInfo.username);
            sendMsg.put("password", UserInfo.password);

            connection = (HttpsURLConnection) new URL( Api.url + "/api/getUser").openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setConnectTimeout(1000);
            connection.connect();

            outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(sendMsg.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            recvMsg = Api.getJson(connection);

            if(!recvMsg.getString("token").equals("")){
                UserInfo.token = recvMsg.getString("token");
                Api.saveData(Api.SAVE_TOKEN);
                UserInfo.loginSucceed = true;
            }
            else {
                UserInfo.loginSucceed = false;
            }
            connection.disconnect();
        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
    }
}
