package com.daily.flexui.viewgroup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.BezierInterpolator;
import com.daily.flexui.interpolator.uiengine.FlexUiEngine;

public class OverScrollView extends ScrollView {

    //过滑动阻尼
    private float damping;
    private View inner;
    private float y;
    private Rect normal = new Rect();

    private ValueAnimator animator;

    public interface OnOverScrollLisener{
        void onOverScroll(int offset);
    }

    private OnOverScrollLisener onOverScrollLisener;

    public void setOnOverScrollLisener(OnOverScrollLisener onOverScrollLisener) {
        this.onOverScrollLisener = onOverScrollLisener;
    }

    public OverScrollView(Context context) {
        super(context);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.OverScrollView, 0, 0);
        damping = array.getFloat(R.styleable.OverScrollView_overscrollview_damping, 1.2f);

        init();

    }

    private void init(){
        setOverScrollMode(OVER_SCROLL_NEVER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setScrollBarSize(0);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }*/

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                if(animator != null){
                    animator.cancel();
                    animator.removeAllUpdateListeners();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) ((int) (nowY - preY) / damping);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动。这时移动布局
                if (isNeedMove()) {
                    int yy = inner.getTop() + deltaY;
                    // 移动布局
                    inner.layout(normal.left, yy, normal.right,
                            normal.height() + yy);

                    if(onOverScrollLisener != null){
                        onOverScrollLisener.onOverScroll(inner.getTop());
                    }
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动
    public void animation() {
        // 开启移动动画
        if(animator != null){
            animator.cancel();
            animator.removeAllUpdateListeners();
        }
        animator = ValueAnimator.ofInt(inner.getTop(),normal.top);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
                inner.layout(normal.left, value, normal.right, normal.height()+value);

                if(onOverScrollLisener != null){
                    onOverScrollLisener.onOverScroll(value);
                }
            }
        });
        animator.setDuration(650);
        animator.setInterpolator(FlexUiEngine.ScrollInterpolator());
        animator.start();
    }

    // 是否须要开启动画
    public boolean isNeedAnimation() {
        return inner.getTop()!=normal.top;
    }

    // 是否须要移动布局
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY >= offset) {
            return true;
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        normal.set(inner.getLeft(), inner.getTop(),
                inner.getRight(), inner.getBottom());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int oldx = 0;
        if (ev.getAction()==MotionEvent.ACTION_DOWN){
            oldx = (int) ev.getX();
        }
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            if(Math.abs(ev.getX()-oldx)<20) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}

