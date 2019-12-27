package com.daily.flexui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class LoadingView extends BaseGradientView {

    private Paint paint;

    private float space;

    private float dotSize;

    private float dotSizes[];

    private boolean isPlaying;

    private ValueAnimator animator[]=new ValueAnimator[3];

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchButton, 0, 0);
        space = (int) array.getDimension(R.styleable.LoadingView_loadingview_space, DisplayUtils.dp2px(3));
        dotSize = (int) array.getDimension(R.styleable.LoadingView_loadingview_dotsize, DisplayUtils.dp2px(8));
        dotSizes = new float[]{dotSize/2,dotSize/2,dotSize/2};
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(startColor);
        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);
        paint.setShader(mShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(dotSize/2, height/2, dotSizes[0] ,paint);
        canvas.drawCircle(dotSize*3/2+space,height/2,dotSizes[1],paint );
        canvas.drawCircle(dotSize*5/2+space*2, height/2, dotSizes[2], paint);
    }

    @Override
    public int getWrapContentWidth() {
        return (int) (space*2+dotSize*3);
    }

    @Override
    public int getWrapContentHeight() {
        return (int) dotSize;
    }

    public void play(){
        isPlaying = true;
        for(int i=0;i<3;i++){
            if(animator[i]!=null){
                animator[i].cancel();
                animator[i].removeAllListeners();
                animator[i].removeAllUpdateListeners();
            }

            animator[i] = ValueAnimator.ofFloat(dotSize/2,dotSize/5);
            animator[i].setDuration(600);
            animator[i].setStartDelay(i*200);
            animator[i].setRepeatCount(-1);
            animator[i].setInterpolator(new DecelerateInterpolator());
            animator[i].setRepeatMode(ValueAnimator.REVERSE);
            final int finalI = i;
            animator[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    dotSizes[finalI] = v;
                    invalidate();
                }
            });
            animator[i].start();
        }
    }

    public void stop(){
        isPlaying = false;
        for(int i=0;i<3;i++) {
            if (animator[i] != null) {
                animator[i].cancel();
                animator[i].removeAllListeners();
                animator[i].removeAllUpdateListeners();
            }

            animator[i] = ValueAnimator.ofFloat(dotSizes[i], 0);
            animator[i].setDuration(600);
            animator[i].setStartDelay(i * 200);
            animator[i].setInterpolator(new DecelerateInterpolator());
            final int finalI = i;
            animator[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    dotSizes[finalI] = v;
                    invalidate();
                }
            });
            animator[i].start();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        play();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    stop();
                }else {
                    play();
                }
            }
        });
    }
}
