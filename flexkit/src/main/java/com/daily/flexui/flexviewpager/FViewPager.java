package com.daily.flexui.flexviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FViewPager extends ViewPager {

    private int realCount;

    //理论位置
    private int virtualPos;
    //实际位置
    private int realPos;
    private float oldX;
    private boolean isRightSlide;

    public interface OnScrollListener{
        void onScroll(float offset, int pos, boolean isRightSlide);
    }

    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public FViewPager(@NonNull Context context) {
        super(context);
    }

    public FViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof EndlessAdapter){
            realCount = ((EndlessAdapter) adapter).getRealCount();
            realPos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % realCount);
            setCurrentItem(realPos);
        }
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        if(getAdapter() instanceof EndlessAdapter) {
            realPos = position;
            virtualPos = position % realCount;

            if(onScrollListener!=null){
                onScrollListener.onScroll(offset+virtualPos, virtualPos, isRightSlide);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            oldX = ev.getX();
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            isRightSlide = !(ev.getX() - oldX > 0);
        }
        return super.dispatchTouchEvent(ev);
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();

        int width = measureWidth(minimumWidth, widthMeasureSpec);
        int height = measureHeight(minimumHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    private int measureWidth(int defaultWidth, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = specSize;
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = specSize;
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = getFirstViewHeight();
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = specSize;
                break;
        }
        return defaultHeight;
    }
    private int getFirstViewHeight(){
        return 1300;
    }*/

}
