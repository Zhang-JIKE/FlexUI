package com.daily.flexui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daily.flexui.util.AppContextUtils;
import com.daily.flexui.util.StatusBarUtils;


public class BaseActivity extends AppCompatActivity {

    private static BaseActivity mActivity;

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
        Intent intent = new Intent(AppContextUtils.getAppContext(), BaseActivity.class);
        AppContextUtils.getAppContext().startActivity(intent);
    }
}
