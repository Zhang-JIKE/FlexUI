package com.daily.flexui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.BezierInterpolator;
import com.daily.flexui.interpolator.ViscousFluidInterpolator;
import com.daily.flexui.interpolator.uiengine.FlexUiEngine;
import com.daily.flexui.util.DisplayUtils;


public class SwitchButton extends BaseGradientView {

    private boolean isChecked;

    private Paint backPaint,fillPaint,togglePaint;

    private int borderWidth = DisplayUtils.dp2px(2f);
    private int toggleRadius;

    private int ls,le,rs,re;

    private int deltaL,deltaR;

    private ValueAnimator animatorL,animatorR;

    public interface OnSwitchChangedListner {
        void onSwitchChanged(boolean isChecked);
    }

    private OnSwitchChangedListner onSwitchChangedListner;

    public void setOnSwitchChangedListner(OnSwitchChangedListner onSwitchChangedListner) {
        this.onSwitchChangedListner = onSwitchChangedListner;
    }

    public SwitchButton(Context context) {
        super(context);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchButton, 0, 0);
        isChecked = array.getBoolean(R.styleable.SwitchButton_switchbutton_ischecked, false);
        array.recycle();
    }

    @Override
    public int getWrapContentWidth() {
        return DisplayUtils.dp2px(38);
    }

    @Override
    public int getWrapContentHeight() {
        return DisplayUtils.dp2px(22);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        toggleRadius = (height - 2*borderWidth)/2;
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(startColor);
        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);
        fillPaint.setShader(mShader);
        fillPaint.setAlpha(0);

        togglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        togglePaint.setColor(Color.WHITE);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(baseColor);

        radius = height/2;

        ls = borderWidth;
        re = width - borderWidth;
        le = width - borderWidth - 2*toggleRadius;
        rs = borderWidth + 2*toggleRadius;

        if(isChecked){
            deltaL = le;
            deltaR = re;
            fillPaint.setAlpha(255);
        }else {
            deltaL = ls;
            deltaR = rs;
            fillPaint.setAlpha(0);
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked){
                    animUnCheck();
                }else {
                    animCheck();
                }
                if(onSwitchChangedListner!=null){
                    onSwitchChangedListner.onSwitchChanged(isChecked);
                }
            }
        });
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        if(isChecked){
            animUnCheck();
        }else {
            animCheck();
        }
        if(onSwitchChangedListner!=null){
            onSwitchChangedListner.onSwitchChanged(isChecked);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, width, height, radius, radius, backPaint);
            canvas.drawRoundRect(0, 0, width, height, radius, radius, fillPaint);
            canvas.drawRoundRect(deltaL, borderWidth, deltaR, height - borderWidth, toggleRadius, toggleRadius, togglePaint);
        }
    }

    private void animCheck(){
        isChecked = true;
        if(animatorL != null){
            animatorL.cancel();
            animatorL.removeAllUpdateListeners();
            animatorL.removeAllListeners();
        }

        if (animatorR != null){
            animatorR.cancel();
            animatorR.removeAllUpdateListeners();
            animatorR.removeAllListeners();
        }

        animatorL=ValueAnimator.ofInt(deltaL,le);
        animatorL.setDuration(300);
        animatorL.setStartDelay(150);
        animatorL.setInterpolator(FlexUiEngine.EaseInterpolator());
        animatorL.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v = (int) animation.getAnimatedValue();
                deltaL = v;
                invalidate();
            }
        });

        final int startValue = deltaR;
        animatorR=ValueAnimator.ofInt(deltaR,re);
        animatorR.setDuration(300);
        animatorR.setInterpolator(FlexUiEngine.EaseInterpolator());
        animatorR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v = (int) animation.getAnimatedValue();
                deltaR = v;
                fillPaint.setAlpha((int) (255*(v-startValue)/(re-startValue)));
                invalidate();
            }
        });
        animatorR.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorL.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorR.start();

    }

    private void animUnCheck(){
        isChecked = false;
        if(animatorL != null){
            animatorL.cancel();
            animatorL.removeAllUpdateListeners();
            animatorL.removeAllListeners();
        }

        if (animatorR != null){
            animatorR.cancel();
            animatorR.removeAllUpdateListeners();
            animatorR.removeAllListeners();
        }

        animatorR=ValueAnimator.ofInt(deltaR,rs);
        animatorR.setDuration(300);
        animatorR.setStartDelay(150);
        animatorR.setInterpolator(FlexUiEngine.EaseInterpolator());
        animatorR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v = (int) animation.getAnimatedValue();
                deltaR = v;
                invalidate();
            }
        });

        final int startValue = deltaL;
        animatorL=ValueAnimator.ofInt(deltaL,ls);
        animatorL.setDuration(300);
        animatorL.setInterpolator(FlexUiEngine.EaseInterpolator());
        animatorL.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v = (int) animation.getAnimatedValue();
                deltaL = v;
                fillPaint.setAlpha((int) (255-255*(v-startValue)/(ls-startValue)));
                invalidate();
            }
        });

        animatorL.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorR.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorL.start();

    }

}
