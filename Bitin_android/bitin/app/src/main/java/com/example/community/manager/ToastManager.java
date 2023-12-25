package com.example.community.manager;

import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;

public class ToastManager {

    public static void showDefault(Context context, String message) {
        FancyToast.makeText(context, message, FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show();
    }

    public static void showSuccess(Context context, String message) {
        FancyToast.makeText(context, message, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
    }

    public static void showError(Context context, String message) {
        FancyToast.makeText(context, message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
    }

}
