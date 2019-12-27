package com.daily.flexui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daily.flexui.activity.SlideActivity;
import com.daily.flexui.util.AppContextUtils;
import com.daily.flexui.util.StatusBarUtils;


public class BaseSlideActivity extends SlideActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContextUtils.setAppActivity(this);
        AppContextUtils.setAppContext(this);
        StatusBarUtils.setLightModeBar(this);
    }
}
