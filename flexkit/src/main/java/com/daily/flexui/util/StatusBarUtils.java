package com.daily.flexui.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StatusBarUtils {

    public static void setDarkModeBar(Activity activity){
        setStatusBar(false);
    }

    public static void setLightModeBar(Activity activity){
        setStatusBar(true);
    }

    private static void setStatusBar(boolean isLightMode){
        Activity activity = AppContextUtils.getAppActivity();
        if(isLightMode){
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


}
