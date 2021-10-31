package com.Tokenverifier;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity extends AppCompatActivity {
    TextView infoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Api.setFullscreen(this);

        //set current SharedPreferences
        Api.sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        setContentView(R.layout.activity_info);

        //update information
        UpdateInfoThread updateInfoThread = new UpdateInfoThread();
        updateInfoThread.start();

        try {
            updateInfoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //show something
        infoView = findViewById(R.id.infoText);
        infoView.setText("");
        infoView.append("Your name: "+UserInfo.username);
        infoView.append("\n\nYour coins: "+UserInfo.coin);
        infoView.append("\n\nYour level: "+UserInfo.level);
        infoView.append("\n\nYour exp: "+UserInfo.exp);
        infoView.append("\n\nYour info: "+UserInfo.info);
    }
}