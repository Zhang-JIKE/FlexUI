package com.daily.flexui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.daily.flexui.activity.SlideActivity;
import com.daily.flexui.activity.SwipeActivity;
import com.daily.flexui.util.AppContextUtils;
import com.daily.flexui.util.StatusBarUtils;


public class BaseSwipeActivity extends SwipeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContextUtils.setAppActivity(this);
        AppContextUtils.setAppContext(this);
        StatusBarUtils.setLightModeBar(this);
    }
}
