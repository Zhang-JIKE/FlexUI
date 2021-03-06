package com.daily.flexui.flexviewpager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.daily.flexui.R;
import com.daily.flexui.util.AppContextUtils;

import java.util.ArrayList;
import java.util.List;

public class EndlessAdapter extends PagerAdapter {

    private List<Integer> layoutIds;

    public interface OnCreateItemListener{
        void onCreateItem(View view,int pos);
    }

    private OnCreateItemListener onCreateItemListener;

    public void setOnCreateItemListener(OnCreateItemListener onCreateItemListener) {
        this.onCreateItemListener = onCreateItemListener;
    }

    public EndlessAdapter(List<Integer> layoutIds, OnCreateItemListener onCreateItemListener){
        this.layoutIds = layoutIds;
        this.onCreateItemListener = onCreateItemListener;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getRealCount(){
        return layoutIds.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("pos",""+position);
        int pos = position % getRealCount();

        View view = LayoutInflater.from(AppContextUtils.getAppContext()).inflate(layoutIds.get(pos), null);
        container.addView(view);
        if(onCreateItemListener!=null){
            onCreateItemListener.onCreateItem(view,pos);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
