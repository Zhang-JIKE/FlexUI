package com.daily.flexui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.BezierInterpolator;
import com.daily.flexui.interpolator.ViscousFluidInterpolator;
import com.daily.flexui.util.BitmapUtils;
import com.daily.flexui.util.DisplayUtils;

public class CheckButton extends BaseGradientView {

    private float iconWidth;

    private float iconScale = 0;

    private float borderWidth;

    private Paint iconPaint,backPaint,fillPaint;

    private Bitmap icon;

    private float progress = 0;

    private float backFillWidth = 0;

    private boolean isChecked;

    private ValueAnimator roundAnim,fillAnim,iconAnim;

    private Rect dst,src;

    public CheckButton(Context context) {
        super(context);
    }

    public CheckButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CheckButton, 0, 0);
        iconWidth = array.getDimension(R.styleable.CheckButton_checkbutton_iconwidth, DisplayUtils.dp2px(22));
        borderWidth = array.getDimension(R.styleable.CheckButton_checkbutton_borderwidth, DisplayUtils.dp2px(2));
        backFillWidth = borderWidth;
        icon = BitmapUtils.getBitmapById(getContext(), array.getResourceId(R.styleable.IconView_iconview_src, R.drawable.ic_check));
        array.recycle();

        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconPaint.setColor(Color.WHITE);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(baseColor);
        backPaint.setStrokeWidth(borderWidth);
        backPaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(startColor);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(backFillWidth);

        dst = new Rect();
        src = new Rect();
        src.set(0,0,icon.getWidth(),icon.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);
        fillPaint.setShader(mShader);

        dst.set((int)((width-iconWidth*iconScale)/2),(int)((height-iconWidth*iconScale)/2),
                (int)((width+iconWidth*iconScale)/2),(int)((height+iconWidth*iconScale)/2));

        initAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(width/2,height/2,width/2-borderWidth/2,backPaint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(backFillWidth/2,backFillWidth/2,width-backFillWidth/2,height-backFillWidth/2,-90,progress,false,fillPaint);
        }

        canvas.drawBitmap(icon,src,dst,iconPaint);
    }

    @Override
    public int getWrapContentHeight() {
        return DisplayUtils.dp2px(34);
    }

    @Override
    public int getWrapContentWidth() {
        return DisplayUtils.dp2px(34);
    }

    private void initAnim(){
        iconAnim = ValueAnimator.ofFloat(0f,1f);
        iconAnim.setDuration(300);
        iconAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                iconScale = v;
                dst.set((int)((width-iconWidth*iconScale)/2),(int)((height-iconWidth*iconScale)/2),
                        (int)((width+iconWidth*iconScale)/2),(int)((height+iconWidth*iconScale)/2));
                invalidate();
            }
        });

        fillAnim = ValueAnimator.ofFloat(borderWidth, width/2);
        fillAnim.setDuration(300);
        fillAnim.setInterpolator(new BezierInterpolator(.25,.1,.25,1));
        fillAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                backFillWidth = v;
                fillPaint.setStrokeWidth(backFillWidth);
                invalidate();
            }
        });
        fillAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iconAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        roundAnim = ValueAnimator.ofFloat(progress, 360);
        roundAnim.setDuration(400);
        roundAnim.setInterpolator(new ViscousFluidInterpolator());
        roundAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                progress = v;
                invalidate();
            }
        });
        roundAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fillAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void animCheck(){
        progress = 0;
        backFillWidth = borderWidth;
        iconScale = 0;
        dst.set((int)((width-iconWidth*iconScale)/2),(int)((height-iconWidth*iconScale)/2),
                (int)((width+iconWidth*iconScale)/2),(int)((height+iconWidth*iconScale)/2));
        fillPaint.setStrokeWidth(backFillWidth);


        if(fillAnim != null){
            fillAnim.cancel();
        }
        if(iconAnim != null){
            iconAnim.cancel();
        }
        if(roundAnim != null){
            roundAnim.cancel();
            roundAnim.start();
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(new Runnable() {
            @Override
            public void run() {
                animCheck();
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animCheck();
            }
        });
    }
}
