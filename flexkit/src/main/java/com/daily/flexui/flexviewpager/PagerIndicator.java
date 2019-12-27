package com.daily.flexui.flexviewpager;

import android.content.Context;
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

    private float indicatorWidth;

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
        return super.getWrapContentHeight() + DisplayUtils.dp2px(4f);
    }

    @Override
    public int getWrapContentWidth() {
        return super.getWrapContentWidth()+DisplayUtils.dp2px(80);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(Color.WHITE);
        backPaint.setAlpha(40);

        forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        forePaint.setColor(Color.WHITE);
        forePaint.setAlpha(200);
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
            canvas.drawRoundRect(0, 0, width, height, height / 2, height / 2, backPaint);
            canvas.drawRoundRect(offSetX * (width / pageCount), 0, offSetX * (width / pageCount) + (width / pageCount), height, height / 2, height / 2, forePaint);
        }
    }
}
