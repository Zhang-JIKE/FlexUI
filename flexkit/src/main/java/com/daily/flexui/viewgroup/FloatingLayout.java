package com.daily.flexui.viewgroup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.uiengine.FlexUiEngine;
import com.daily.flexui.util.DisplayUtils;
import com.daily.flexui.view.FloatingGravity;

public class FloatingLayout extends FrameLayout {

    private int floating;

    private int floatingValue;

    private int width;
    private int height;

    public FloatingLayout(Context context) {
        super(context);
    }

    public FloatingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.FloatingLayout, 0, 0);

        floating = array.getInt(R.styleable.FloatingLayout_floatinglayout_floating_type, FloatingGravity.NONE);
        floatingValue = (int) array.getDimension(R.styleable.FloatingLayout_floatinglayout_floating_value, DisplayUtils.dp2px(10));
        array.recycle();
    }

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
                defaultWidth = getMeasuredWidth()+floatingValue*2;
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize+floatingValue*2;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = getMeasuredHeight()+floatingValue*2;
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize+floatingValue*2;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                break;
        }
        return defaultHeight;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(floating!=FloatingGravity.NONE) {
            ValueAnimator animator = ValueAnimator.ofInt(-floatingValue, floatingValue);
            animator.setDuration(1500);
            animator.setRepeatCount(-1);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = (int) animation.getAnimatedValue();
                    if(floating == FloatingGravity.HORIZONTAL) {
                        setX(v);
                    }else if(floating == FloatingGravity.VERTICAL){
                        setY(v);
                    }
                }
            });
            animator.start();
        }
    }
}
