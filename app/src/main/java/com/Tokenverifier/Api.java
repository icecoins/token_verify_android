package com.Tokenverifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Objects;

public class Api {
    //global Toast
    private static Toast t1;
    //global SharedPreferences
    public static SharedPreferences sharedPreferences;
    //global url, u'd better change it,
    // like "http://example.com:5678" and so on
    public static String url = "https://example.com";
    //it's easy to understand
    public static final int
                    SAVE_USERNAME = 1, SAVE_PASSWORD = 2, SAVE_TOKEN = 3,
                    SAVE_AVAILABLE_TRUE = 4, SAVE_AVAILABLE_FALSE = 5, SAVE_TIME = 6,
                    CLEAR_TOKEN = 7, SAVE_ID = 8, SAVE_INFO = 9;
    /**
     * sava the data to local file
     * */
    public static void saveData(int mode){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        switch (mode){
            case SAVE_USERNAME:
                ed.putString("username", UserInfo.username);
                break;
            case SAVE_PASSWORD:
                ed.putString("password", UserInfo.password);
                break;
            case SAVE_TOKEN:
                ed.putString("token", UserInfo.token);
                break;
            case SAVE_AVAILABLE_TRUE:
                ed.putBoolean("available", true);
                break;
            case SAVE_AVAILABLE_FALSE:
                ed.putBoolean("available", false);
                break;
            case SAVE_TIME:
                ed.putLong("updateTime", new Date().getTime());
                break;
            case CLEAR_TOKEN:
                ed.putString("token", "");
                break;
            case SAVE_ID:
                ed.putString("id", UserInfo.id);
                break;
            case SAVE_INFO:
                ed.putString("info", UserInfo.info);
                break;
            default:
                break;
        }
        ed.apply();
    }

    /**
     * try to receive some messages from remote server
     * return a JSONObject
     * */
    public static JSONObject getJson(HttpURLConnection connection) throws JSONException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder ret = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            ret.append(s);
        }
        return new JSONObject(ret.toString());
    }

    /**
     show some message on any activity
     * */
    public static void showMsg(Context ct, String s){
        try{
            if(Looper.myLooper() == null){
                Looper.prepare();
                if (t1 != null) {
                    t1.cancel();
                    t1 = null;
                }
                t1= Toast.makeText(ct,s, Toast.LENGTH_SHORT);
                t1.show();
                Looper.loop();
            }else{
                if (t1 != null) {
                    t1.cancel();
                    t1 = null;
                }
                t1= Toast.makeText(ct,s, Toast.LENGTH_SHORT);
                t1.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     set the activity to be fullscreen
     * */
    public static void setFullscreen(AppCompatActivity activity) {

        //hide ActionBar
        Objects.requireNonNull(activity.getSupportActionBar()).hide();

        //try to expand the layout to the camera area while landscape the screen in the special-shaped screen
        if (activity.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE && Build.VERSION.SDK_INT >= 28){
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        //hide virtual keys
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_IMMERSIVE
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //hide top status bar
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * encrypt String through a easy way
     * u are suppose to optimize this function
     **/
    public static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }
    public static String convertMD5(String username, String password) {
        char[] a = username.toCharArray(),
                b = password.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        for (int i = 0; i < b.length; i++) {
            b[i] = (char) (b[i] ^ 't');
        }
        return new String(a) + "/" + new String(b);
    }
}
