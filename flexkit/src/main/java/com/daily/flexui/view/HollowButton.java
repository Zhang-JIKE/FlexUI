package com.daily.flexui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class HollowButton extends BaseGradientView {

    private Paint backPaint,textPaint;

    private Paint.FontMetricsInt fontMetrics;

    private int backColor;

    private float textSize;

    private String text;

    private float radius;

    private boolean isGradient;
    private boolean isRound;

    private Rect rect=new Rect();


    public HollowButton(Context context) {
        super(context);
    }

    public HollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HollowButton, 0, 0);
        text = array.getString(R.styleable.HollowButton_hollowbutton_text);
        textSize = array.getDimension(R.styleable.HollowButton_hollowbutton_textsize, DisplayUtils.px2sp(14));
        radius = array.getDimension(R.styleable.HollowButton_hollowbutton_radius, DisplayUtils.px2sp(14));
        backColor = array.getColor(R.styleable.HollowButton_hollowbutton_backcolor, Color.WHITE);
        isGradient = array.getBoolean(R.styleable.HollowButton_hollowbutton_isgradient, false);
        isRound = array.getBoolean(R.styleable.HollowButton_hollowbutton_isgradient, true);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(baseColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.setLetterSpacing(0.1f);
        }
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        fontMetrics = textPaint.getFontMetricsInt();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0,0,width,height,radius,radius,backPaint);
        }
        int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text,width/2,baseline,textPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(backColor);
        if(isGradient){
            Shader mShader = new LinearGradient(0,0,width,height,
                    new int[]{startColor,endColor},
                    null,Shader.TileMode.MIRROR);
            backPaint.setShader(mShader);
        }

        if(isRound){
            radius = height/2;
        }

        rect.set(0,0,width,height);
    }

    @Override
    public int getWrapContentHeight() {
        return fontMetrics.descent-fontMetrics.ascent+DisplayUtils.dp2px(6);
    }

    @Override
    public int getWrapContentWidth() {
        return (int) (textPaint.measureText(text)+DisplayUtils.dp2px(16));
    }

}
