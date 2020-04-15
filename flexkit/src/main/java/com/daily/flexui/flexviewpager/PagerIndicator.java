package com.daily.flexui.flexviewpager;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.daily.flexui.util.DisplayUtils;
import com.daily.flexui.view.BaseGradientView;

public class PagerIndicator extends BaseGradientView {

    private int pageCount;

    private int lightRadius = 12;

    private float offSetX;

    private Paint backPaint,forePaint;

    public PagerIndicator(Context context) {
        super(context);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getWrapContentHeight() {
        return super.getWrapContentHeight() + DisplayUtils.dp2px(4f) + lightRadius*2;
    }

    @Override
    public int getWrapContentWidth() {
        return super.getWrapContentWidth()+DisplayUtils.dp2px(80)+lightRadius*2;
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(Color.WHITE);
        backPaint.setAlpha(40);

        forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        forePaint.setColor(Color.WHITE);
        forePaint.setAlpha(200);
        forePaint.setShadowLayer(12,0,0,Color.WHITE);
    }

    public void setProgressColor(int color){
        forePaint.setColor(color);
        invalidate();
    }

    public void setProgressAlpha(int alpha){
        forePaint.setAlpha(alpha);
        invalidate();
    }

    public void setProgressLight(int lightRadius, int color){
        forePaint.setShadowLayer(lightRadius,0,0,color);
        this.lightRadius = lightRadius;
        invalidate();
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
        invalidate();
    }

    public void setOffSetX(float offSetX) {
        this.offSetX = offSetX;

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, lightRadius, width, height-lightRadius, height / 2, height / 2, backPaint);
            canvas.drawRoundRect(offSetX * (width / pageCount), lightRadius, offSetX * (width / pageCount) + (width / pageCount), height-lightRadius, height / 2, height / 2, forePaint);
        }
    }
}
