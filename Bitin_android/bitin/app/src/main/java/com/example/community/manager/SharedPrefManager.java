package com.example.community.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences("bitin", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static String getUserName() {
        return sharedPreferences.getString("userName", "");
    }

    public static void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.commit();
    }

    public static String getUpbitAccessApiKey() {
        return sharedPreferences.getString("upbitAccessApiKey", "");
    }

    public static void setUpbitAccessApiKey(String key) {
        editor.putString("upbitAccessApiKey", key);
        editor.commit();
    }

    public static String getUpbitSecretApiKey() {
        return sharedPreferences.getString("upbitSecretApiKey", "");
    }

    public static void setUpbitSecretApiKey(String key) {
        editor.putString("upbitSecretApiKey", key);
        editor.commit();
    }

    public static boolean isAutoBuyOn() {
        return sharedPreferences.getBoolean("autoBuyState", false);
    }

    public static void setAutoBuyState(boolean state) {
        editor.putBoolean("autoBuyState", state);
        editor.commit();
    }

    public static boolean isAutoSellOn() {
        return sharedPreferences.getBoolean("autoSellState", false);
    }

    public static void setAutoSellState(boolean state) {
        editor.putBoolean("autoSellState", state);
        editor.commit();
    }

}
