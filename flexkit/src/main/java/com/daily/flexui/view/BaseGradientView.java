package com.daily.flexui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.BezierInterpolator;
import com.daily.flexui.interpolator.uiengine.FlexUiEngine;
import com.daily.flexui.util.DisplayUtils;
import com.daily.flexui.view.abstractview.BaseView;

public class BaseGradientView extends BaseView {

    protected int width;
    protected int height;

    protected int startColor;
    protected int endColor;

    protected int baseColor;
    protected int darkBaseColor;

    protected float pressedAlpha = 0.6f;

    protected float radius;

    protected int animCategory;

    public BaseGradientView(Context context) {
        super(context);
    }

    public BaseGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        TypedArray array = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.BaseGradientView, 0, 0);
        startColor = array.getColor(R.styleable.BaseGradientView_baseview_startcolor, Color.parseColor("#04d1fe"));
        endColor = array.getColor(R.styleable.BaseGradientView_baseview_endcolor, Color.parseColor("#4894e8"));
        baseColor = array.getColor(R.styleable.BaseGradientView_baseview_basecolor, getResources().getColor(R.color.baseColor));
        radius = array.getDimension(R.styleable.BaseGradientView_baseview_radius, DisplayUtils.dp2px(4));
        darkBaseColor = getResources().getColor(R.color.baseDarkColor);
        animCategory = array.getInt(R.styleable.BaseGradientView_baseview_animin,0);
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

    @Override
    public int getWrapContentWidth() {
        return 0;
    }

    @Override
    public int getWrapContentHeight() {
        return 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(animCategory !=0){
            setAlpha(0);
            if(animCategory == 1){
                setTranslationY(100);
                this.animate().alpha(1).translationY(0)
                        .setInterpolator(new BezierInterpolator(.42,.42,.32,1)).setDuration(900).start();
            }else if(animCategory ==2){
                setTranslationY(-100);
                this.animate().alpha(1).translationY(0)
                        .setInterpolator(new BezierInterpolator(.42,.42,.32,1)).setDuration(900).start();
            }else if(animCategory ==3){
                setTranslationX(-100);
                this.animate().alpha(1).translationX(0)
                        .setInterpolator(new BezierInterpolator(.42,.42,.32,1)).setDuration(900).start();
            }else if(animCategory ==4){
                setTranslationX(100);
                this.animate().alpha(1).translationX(0)
                        .setInterpolator(new BezierInterpolator(.42,.42,.32,1)).setDuration(900).start();
            }else if(animCategory ==5){
                this.animate().alpha(1)
                        .setInterpolator(new BezierInterpolator(.42,.42,.32,1)).setDuration(900).start();
            }else if(animCategory ==6){
                setRotation(90);
                this.animate().alpha(1).rotation(0)
                        .setInterpolator(new BezierInterpolator(.42,.42,.32,1)).setDuration(900).start();
            }
        }
    }


}
