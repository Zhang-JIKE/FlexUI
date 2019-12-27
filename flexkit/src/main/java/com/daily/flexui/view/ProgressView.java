package com.daily.flexui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class ProgressView extends BaseGradientView {

    private int progress;

    private Paint backPaint,fillPaint;

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressView, 0, 0);
        progress = array.getInt(R.styleable.ProgressView_progressview_progress, 40);
        array.recycle();

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(baseColor);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(startColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);
        fillPaint.setShader(mShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int value = width*progress/100;

        if(value<height){
            value=height;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0,0,width,height,height/2,height/2,backPaint);
            canvas.drawRoundRect(0,0,value,height,height/2,height/2,fillPaint);
        }
    }


    @Override
    public int getWrapContentHeight() {
        return DisplayUtils.dp2px(5);
    }

    @Override
    public int getWrapContentWidth() {
        return DisplayUtils.dp2px(36);
    }
}
