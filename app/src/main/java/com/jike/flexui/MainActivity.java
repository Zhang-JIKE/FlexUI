package com.jike.flexui;

import android.content.Intent;
import android.os.Bundle;

import com.daily.flexui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(getApplicationContext(),SplashActivity.class);
        startActivity(i);
    }
}
