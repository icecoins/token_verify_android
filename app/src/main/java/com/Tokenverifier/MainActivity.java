package com.Tokenverifier;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LoginWithUsernameThread loginWithUsernameThread;
    LoginWithTokenThread loginWithTokenThread;
    TestServerThread testServerThread;
    TextView loginInfo;
    EditText name, psw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the app to be fullscreen
        Api.setFullscreen(this);
        setContentView(R.layout.activity_main);
        UserInfo.isServerAvailable = false;
        UserInfo.loginSucceed = false;
        loginInfo = findViewById(R.id.loginInfo);
        testServerThread = new TestServerThread();
        testServerThread.start();

        //set current SharedPreferences
        Api.sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.login).setOnClickListener(this);

        //if there's token, login directly with the username
        if(!(getLocalToken() == null || getLocalToken().equals("")) &&
                !(getLocalUsername() == null || getLocalUsername().equals(""))){
            UserInfo.token = getLocalToken();
            UserInfo.username = getLocalUsername();
            loginWithTokenThread = new LoginWithTokenThread();
            loginWithTokenThread.start();
            try {
                loginWithTokenThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(UserInfo.loginSucceed){
                //stop the test thread, jump to the second activity
                testServerThread.interrupt();
                Intent it = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(it);
            }
            else{
                if(UserInfo.isServerAvailable){
                    //the token is unavailable
                    Api.showMsg(this, "登录期已过，请重新登录");
                }else {
                    //the server is unavailable
                    Api.showMsg(this, "服务器连接失败，请稍后再试");
                }
            }
        }else{
            //there's no token
            Api.showMsg(this, "无本地token，请使用用户名密码登录");
        }


        //test whether the server is online
        //and show the message
        DateFormat df = SimpleDateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        new Thread(()->{
            while (!UserInfo.isServerAvailable){
                loginInfo.setText("");
                loginInfo.append("The server is unavailable\nat: " + df.format(new Date()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            loginInfo.setText("");
            loginInfo.append("The server is available");
        }).start();

        //find EDITTEXT and set filters
        name = findViewById(R.id.username);
        psw = findViewById(R.id.password);
        name.setFilters(getSpaceFilter());
        psw.setFilters(getSpaceFilter());
    }

    public String getLocalToken(){
        return Api.sharedPreferences.getString("token", null);
    }

    public String getLocalUsername(){
        return Api.sharedPreferences.getString("username", null);
    }

    //clear the space and the line break
    public InputFilter[] getSpaceFilter(){
        InputFilter filter= (source, start, end, dest, distort, denied) -> {
            if(source.equals(" ")||source.toString().contentEquals("\n")){
                return "";
            }
            else {
                return null;
            }
        };
        return new InputFilter[]{filter};
    }

    /**
     * try to login with username and password
     * */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login){
            if(name.getText().toString().trim().equals("") ||
                    psw.getText().toString().trim().equals("")){
                Api.showMsg(MainActivity.this, "username or password cannot be blank");
            }else{
                if(loginWithUsernameThread == null){
                    loginWithUsernameThread = new LoginWithUsernameThread();
                    UserInfo.username = name.getText().toString().trim();
                    UserInfo.password = psw.getText().toString().trim();
                    try {
                        //try to login, and wait for the result
                        loginWithUsernameThread.start();
                        loginWithUsernameThread.join();
                        loginWithUsernameThread = null;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if login succeeded, save some information
                    // and jump to the second activity
                    if(UserInfo.loginSucceed){
                        testServerThread.interrupt();
                        Api.saveData(Api.SAVE_USERNAME);
                        Api.saveData(Api.SAVE_PASSWORD);
                        //api.saveData(api.SAVE_AVAILABLE_TRUE);
                        Intent it = new Intent(MainActivity.this, UserInfoActivity.class);
                        startActivity(it);
                    }else{
                        Api.showMsg(MainActivity.this, "Login failed");
                    }
                }
            }
        }
    }
}