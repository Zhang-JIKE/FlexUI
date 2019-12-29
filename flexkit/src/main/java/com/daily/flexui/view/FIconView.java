package com.daily.flexui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.daily.flexui.R;
import com.daily.flexui.interpolator.BezierInterpolator;
import com.daily.flexui.interpolator.uiengine.FlexUiEngine;
import com.daily.flexui.util.BitmapUtils;
import com.daily.flexui.util.DisplayUtils;
import com.daily.flexui.viewgroup.NeonLayout;

public class FIconView extends BaseGradientView {

    private boolean isNeon;
    private boolean isBackSolid;
    private boolean isRound;

    private int backSColor,backEColor,backNormalColor;
    private int iconSColor,iconEColor,iconNormalColor;

    private int neonColor;

    private float radius;
    private float lightDistance;
    private float iconWidth;

    private Bitmap icon;

    private Paint neonPaint,backPaint,iconPaint,backNPaint,iconNPaint;

    private Rect srcRect,dstRect;

    private Shader backShader,iconShader,backNShader,iconNShader;

    private ValueAnimator animatorCheck,animatorUnCheck;

    private boolean isChecked;

    public interface OnCheckListener{
        void onCheck(boolean isChecked);
    }

    private OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public FIconView(Context context) {
        super(context);
    }

    public FIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.FIconView, 0, 0);

        isNeon = array.getBoolean(R.styleable.FIconView_ficonview_isneon, true);
        isBackSolid = array.getBoolean(R.styleable.FIconView_ficonview_isbacksolid, true);
        isRound = array.getBoolean(R.styleable.FIconView_ficonview_isround, true);

        backNormalColor = array.getColor(R.styleable.FIconView_ficonview_back_normal_color, baseColor);
        iconNormalColor = array.getColor(R.styleable.FIconView_ficonview_icon_normal_color, darkBaseColor);

        backSColor = array.getColor(R.styleable.FIconView_ficonview_back_start_color, startColor);
        backEColor = array.getColor(R.styleable.FIconView_ficonview_back_end_color, endColor);
        iconSColor = array.getColor(R.styleable.FIconView_ficonview_icon_start_color, Color.WHITE);
        iconEColor = array.getColor(R.styleable.FIconView_ficonview_icon_end_color, Color.WHITE);

        neonColor = array.getColor(R.styleable.FIconView_ficonview_neoncolor, backSColor);
        radius = array.getDimension(R.styleable.FIconView_ficonview_radius, DisplayUtils.dp2px(4));
        lightDistance = array.getDimension(R.styleable.FIconView_ficonview_lightdistance, DisplayUtils.dp2px(4));
        iconWidth = array.getDimension(R.styleable.FIconView_ficonview_icon_width,DisplayUtils.dp2px(20));
        icon = BitmapUtils.getBitmapById(getContext(),
                array.getResourceId(R.styleable.FIconView_ficonview_icon, R.drawable.ic_phone));

        isChecked = array.getBoolean(R.styleable.FIconView_ficonview_icon_ischecked, true);

        array.recycle();

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        neonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        neonPaint.setColor(neonColor);
        neonPaint.setMaskFilter(new BlurMaskFilter(lightDistance, BlurMaskFilter.Blur.OUTER));
        neonPaint.setAlpha(120);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(backNormalColor);

        backNPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backNPaint.setColor(backNormalColor);

        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        iconNPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        srcRect=new Rect(0,0,icon.getWidth(),icon.getHeight());

        dstRect=new Rect((int)((width-iconWidth)/2),(int)((height-iconWidth)/2),
                (int)((width+iconWidth)/2),(int)((height+iconWidth)/2));

        backShader = new LinearGradient(0, 0, width, height,
                new int[]{backSColor, backEColor},
                null, Shader.TileMode.MIRROR);

        iconShader = new LinearGradient(0, 0, dstRect.width(), dstRect.height(),
                new int[]{iconSColor, iconEColor},
                null, Shader.TileMode.MIRROR);
        backNShader = new LinearGradient(0, 0, width, height,
                new int[]{backNormalColor, backNormalColor},
                null, Shader.TileMode.MIRROR);


        backPaint.setShader(backShader);
        iconPaint.setShader(iconShader);

        backNPaint.setShader(backNShader);
        iconNPaint.setColorFilter(new PorterDuffColorFilter(iconNormalColor, PorterDuff.Mode.SRC_ATOP));

        if(isChecked) {
            backNPaint.setAlpha(0);
            iconNPaint.setAlpha(0);
            neonPaint.setAlpha(100);
            backPaint.setAlpha(255);
            iconPaint.setAlpha(255);
        }else {
            backNPaint.setAlpha(255);
            iconNPaint.setAlpha(255);
            backPaint.setAlpha(0);
            iconPaint.setAlpha(0);
            neonPaint.setAlpha(0);
        }

        if(isRound){
            radius = height/2;
        }

        initAnim();
    }

    private void initAnim(){
        animatorCheck = ValueAnimator.ofInt(backPaint.getAlpha(),255);
        animatorCheck.setDuration(520);
        animatorCheck.setInterpolator(new BezierInterpolator(.26,.75,.32,1));
        animatorCheck.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v=(int)animation.getAnimatedValue();
                backPaint.setAlpha(v);
                iconPaint.setAlpha(v);
                neonPaint.setAlpha(v*100/255);

                backNPaint.setAlpha(255-v);
                iconNPaint.setAlpha(255-v);

                invalidate();
            }
        });

        animatorUnCheck = ValueAnimator.ofInt(backPaint.getAlpha(),0);
        animatorUnCheck.setDuration(520);
        animatorUnCheck.setInterpolator(new BezierInterpolator(.26,.75,.32,1));
        animatorUnCheck.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int v=(int)animation.getAnimatedValue();
                backPaint.setAlpha(v);
                iconPaint.setAlpha(v);
                neonPaint.setAlpha(v*100/255);

                backNPaint.setAlpha(255-v);
                iconNPaint.setAlpha(255-v);

                invalidate();
            }
        });
    }

    public void animCheck(){
        isChecked = true;
        if(animatorCheck !=null){
            animatorCheck.cancel();
        }
        if(animatorUnCheck !=null){
            animatorUnCheck.cancel();
        }
        initAnim();
        animatorCheck.start();
    }

    public void animUnCheck(){
        isChecked=false;
        if(animatorCheck !=null){
            animatorCheck.cancel();
        }
        if(animatorUnCheck !=null){
            animatorUnCheck.cancel();
        }
        initAnim();
        animatorUnCheck.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isNeon){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(lightDistance,lightDistance,width-lightDistance,height-lightDistance,radius,radius,neonPaint);
            }
        }

        if(isBackSolid){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(lightDistance,lightDistance,width-lightDistance,height-lightDistance,radius,radius,backNPaint);
                canvas.drawRoundRect(lightDistance,lightDistance,width-lightDistance,height-lightDistance,radius,radius,backPaint);
            }
        }

        canvas.drawBitmap(icon,srcRect,dstRect,iconNPaint);

        canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(dstRect,iconPaint);
        canvas.drawBitmap(icon,srcRect,dstRect,iconPaint);
    }

    @Override
    public int getWrapContentWidth() {
        return (int) (iconWidth+DisplayUtils.dp2px(18)+lightDistance*2);
    }

    @Override
    public int getWrapContentHeight() {
        return (int) (iconWidth+DisplayUtils.dp2px(18)+lightDistance*2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked){
                    animUnCheck();
                }else {
                    animCheck();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            animate().scaleX(0.8f).scaleY(0.8f).setDuration(270)
                    .setInterpolator(FlexUiEngine.EaseInterpolator()).start();
        }else {
            if (isChecked) {
                animUnCheck();
                if(onCheckListener!=null){
                    onCheckListener.onCheck(isChecked);
                }
            } else {
                animCheck();
                if(onCheckListener!=null){
                    onCheckListener.onCheck(isChecked);
                }
            }
            animate().scaleX(1f).scaleY(1).setDuration(270)
                    .setInterpolator(FlexUiEngine.OverInterpolator()).start();
        }
        return true;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
