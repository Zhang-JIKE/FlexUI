package com.daily.flexui.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

public abstract class BaseViewGroup extends ViewGroup {

    protected int width;
    protected int height;

    public BaseViewGroup(Context context) {
        super(context);

    }

    public BaseViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public abstract void init(AttributeSet attrs);

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();

        width = measureWidth(minimumWidth, widthMeasureSpec);
        height = measureHeight(minimumHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    private int measureWidth(int defaultWidth, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = getWrapContentWidth();
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = getWrapContentWidth();
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = getWrapContentHeight();
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = getWrapContentHeight();
                break;
        }
        return defaultHeight;
    }

    public abstract int getWrapContentWidth();
    public abstract int getWrapContentHeight();

}
