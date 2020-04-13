package com.jike.flexui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daily.flexui.BaseActivity;
import com.daily.flexui.flexviewpager.EndlessAdapter;
import com.daily.flexui.flexviewpager.EndlessViewPager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(getApplicationContext(),SplashActivity.class);
        startActivity(i);

        EndlessViewPager viewPager = findViewById(R.id.banner);


        final ArrayList<Integer> res = new ArrayList<>();
        res.add(com.daily.flexui.R.layout.item_banner);
        res.add(com.daily.flexui.R.layout.item_banner2);
        res.add(com.daily.flexui.R.layout.item_banner);
        res.add(com.daily.flexui.R.layout.item_banner2);

        EndlessAdapter<ArrayList<Integer>> endlessAdapter = new EndlessAdapter<>(res,
                res, new EndlessAdapter.OnCreateItemListener() {
            @Override
            public void onCreateItem(View view, int pos) {

            }
        });

        viewPager.initViewPager(res,endlessAdapter);
    }
}
