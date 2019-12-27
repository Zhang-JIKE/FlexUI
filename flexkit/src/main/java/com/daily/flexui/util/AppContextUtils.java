package com.daily.flexui.util;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class AppContextUtils {

    public static Context appContext;

    public static Activity appActivity;

    public static void setAppContext(Context appContext) {
        AppContextUtils.appContext = appContext;
    }

    public static Context getAppContext() {
        if (appContext == null){
            new Exception("AppContext is null");
        }
        return appContext;
    }

    public static void setAppActivity(Activity appActivity) {
        AppContextUtils.appActivity = appActivity;
    }

    public static Activity getAppActivity() {
        if (appActivity == null){
            new Exception("AppActivity is null");
        }
        return appActivity;
    }
}
