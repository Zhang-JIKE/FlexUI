package com.daily.flexui.viewgroup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.ViscousFluidInterpolator;
import com.daily.flexui.util.DisplayUtils;

public class BottomSheetLayout extends ViewGroup {

    private View slideView;

    private View mainView;

    private float oldY;

    private float recentY;

    private float sheetHeight;

    private boolean isOpen = true;

    float parentHeight;
    float childHeight;

    private Paint maskPaint;

    private ValueAnimator animator;

    public BottomSheetLayout(Context context) {
        super(context);
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.BottomSheetLayout, 0, 0);
        sheetHeight = array.getDimension(R.styleable.BottomSheetLayout_bottomsheetlayout_sheetheight, DisplayUtils.dp2px(60));
        array.recycle();
        init();
    }

    private void init(){
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint.setColor(Color.argb(40,0,0,0));
        setWillNotDraw(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        parentHeight = getMeasuredHeight();
        childHeight = slideView.getMeasuredHeight();

        mainView.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
        slideView.layout(0, (int) (parentHeight-sheetHeight),getMeasuredWidth(), (int) ((int) (parentHeight-sheetHeight)+childHeight));

        recentY = slideView.getY();
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()!=2){
            throw new IllegalArgumentException("This layout must have 2 children");
        }else {
            mainView = getChildAt(0);
            slideView = getChildAt(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                slideView.setZ(10);
            }
            slideView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int ac = ev.getAction();
        if(ac == MotionEvent.ACTION_DOWN){
            oldY = (int) ev.getY();
            if(animator!=null){
                animator.cancel();
                animator.removeAllUpdateListeners();
            }
        }else if(ac == MotionEvent.ACTION_MOVE){
            float delta = (ev.getY() - oldY);
            float nowY = delta+recentY;
            if(nowY < (parentHeight-childHeight)){
                nowY = (parentHeight-childHeight);
            }
            if(nowY > (parentHeight-sheetHeight)){
                nowY = (parentHeight-sheetHeight);
            }
            slideView.setY(nowY);
        }else if(ac == MotionEvent.ACTION_UP){
            recentY = slideView.getY();
            if(ev.getY()-oldY>100){
                smoothToClose();
            }else {
                smoothToOpen();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void smoothToOpen(){
        if(animator!=null){
            animator.cancel();
            animator.removeAllUpdateListeners();
        }
        final float startValue = recentY;
        animator = ValueAnimator.ofFloat(startValue, parentHeight - childHeight);
        animator.setDuration(450);
        animator.setInterpolator(new ViscousFluidInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v =(float)animation.getAnimatedValue();
                recentY = v;
                slideView.setY(recentY);
            }
        });
        animator.start();
    }

    public void smoothToClose(){
        if(animator!=null){
            animator.cancel();
            animator.removeAllUpdateListeners();
        }
        final float startValue = recentY;
        animator = ValueAnimator.ofFloat(startValue, parentHeight - sheetHeight);
        animator.setDuration(450);
        animator.setInterpolator(new ViscousFluidInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v =(float)animation.getAnimatedValue();
                recentY = v;
                slideView.setY(recentY);
            }
        });
        animator.start();
    }

}
