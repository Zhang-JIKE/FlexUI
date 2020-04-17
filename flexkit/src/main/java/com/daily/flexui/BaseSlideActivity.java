package com.daily.flexui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daily.flexui.activity.SlideActivity;
import com.daily.flexui.util.AppContextUtils;
import com.daily.flexui.util.StatusBarUtils;


public class BaseSlideActivity extends SlideActivity {

    private static BaseSlideActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContextUtils.setAppActivity(this);
        AppContextUtils.setAppContext(this);
        StatusBarUtils.setLightModeBar(this);

        mActivity = this;
    }

    public static Activity getInstance(){
        return mActivity;
    }

    public void start(){
        Intent intent = new Intent(AppContextUtils.getAppContext(), BaseSlideActivity.class);
        AppContextUtils.getAppContext().startActivity(intent);
    }
}
