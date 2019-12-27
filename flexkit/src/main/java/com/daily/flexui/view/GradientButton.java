package com.daily.flexui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class GradientButton extends GradientTextView {

    private boolean isSolid;

    private boolean isround;

    private Paint backPaint;

    private int strokeWidth;

    public GradientButton(Context context) {
        super(context);
    }

    public GradientButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.GradientButton, 0, 0);
        isSolid = array.getBoolean(R.styleable.GradientButton_gradientbutton_issolid, true);
        radius = array.getDimension(R.styleable.GradientButton_gradientbutton_radius, DisplayUtils.dp2px(4));
        isround = array.getBoolean(R.styleable.GradientButton_gradientbutton_isround, true);

        strokeWidth = DisplayUtils.dp2px(1.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(Color.WHITE);
        backPaint.setStrokeWidth(strokeWidth);
        if(isSolid){
            backPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            textPaint.setShader(null);
            textPaint.setColor(Color.WHITE);
        }else {
            backPaint.setStyle(Paint.Style.STROKE);
            textPaint.setShader(mShader);
        }
        backPaint.setShader(mShader);

        if(isround){
            radius = height/2;
        }
    }

    @Override
    public int getWrapContentWidth() {
        return super.getWrapContentWidth() + DisplayUtils.dp2px(16);
    }

    @Override
    public int getWrapContentHeight() {
        return super.getWrapContentHeight() + DisplayUtils.dp2px(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(strokeWidth,strokeWidth,width-strokeWidth,height-strokeWidth,radius,radius,backPaint);
        }
        super.onDraw(canvas);
    }
}
