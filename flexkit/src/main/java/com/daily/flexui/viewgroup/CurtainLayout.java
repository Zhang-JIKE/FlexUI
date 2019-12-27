package com.daily.flexui.viewgroup;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.daily.flexui.interpolator.BezierInterpolator;
import com.daily.flexui.interpolator.ViscousFluidInterpolator;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class CurtainLayout extends FrameLayout {

    private float oldY;
    private float recentY;

    private boolean isCurtainVisible = true;

    private ValueAnimator sAnimator;

    public CurtainLayout(Context context) {
        super(context);
    }

    public CurtainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(!(getChildCount()>1)){
            throw new IllegalArgumentException("This Layout must have more than 2 children");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getCurtainView().setZ(10);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isCurtainVisible) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(sAnimator != null){
                    sAnimator.cancel();
                    sAnimator.removeAllUpdateListeners();
                }
                oldY = event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                View view = getCurtainView();
                float deltaY = event.getY() - oldY;
                float nowY = deltaY + recentY;
                Log.e("delta", "" + nowY);
                if (nowY > 0) {
                    nowY = 0;
                }
                if (nowY < -view.getBottom()) {
                    nowY = -view.getBottom();
                    isCurtainVisible = false;
                }
                view.setY(nowY);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                float deltaY = event.getY() - oldY;
                recentY = getCurtainView().getY();
                if(deltaY < -100){
                    startReveal(true);
                }else {
                    startReveal(false);
                }
            }
        }
        return isCurtainVisible;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCurtainVisible;
    }

    private View getCurtainView(){
        return getChildAt(0);
    }

    private int getCurtainViewHeight(){
        return getCurtainView().getBottom()-getCurtainView().getTop();
    }

    private void startReveal(final boolean isUp){
        if(sAnimator != null){
            sAnimator.cancel();
            sAnimator.removeAllUpdateListeners();
            sAnimator.removeAllListeners();
        }

        if(isUp){
            sAnimator = ValueAnimator.ofFloat(getCurtainView().getY(),-getCurtainView().getBottom());
        }else {
            sAnimator = ValueAnimator.ofFloat(getCurtainView().getY(),0);
        }

        sAnimator.setDuration(450);
        sAnimator.setInterpolator(new ViscousFluidInterpolator());
        sAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                getCurtainView().setY(v);
                recentY = v;

                if (getCurtainView().getY()<=-getCurtainView().getBottom()+100) {
                    isCurtainVisible = false;
                }else {
                    isCurtainVisible = true;
                }
            }
        });
        sAnimator.start();

    }

}
