package com.daily.flexui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class CircleImageView extends AppCompatImageView {

    private Paint backPaint,borderPaint;

    private int backColor;

    protected int width;
    protected int height;

    protected int startColor;
    protected int endColor;

    private float radius;
    private boolean isRound;

    private float borderWidth;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);
        borderWidth = array.getDimension(R.styleable.CircleImageView_circleimageview_borderWidth, DisplayUtils.dp2px(1.5f));
        radius = array.getDimension(R.styleable.CircleImageView_circleimageview_radius, DisplayUtils.dp2px(4));
        isRound = array.getBoolean(R.styleable.CircleImageView_circleimageview_isround, false);
        startColor = array.getColor(R.styleable.CircleImageView_circleimageview_startcolor,Color.parseColor("#04d1fe"));
        endColor = array.getColor(R.styleable.CircleImageView_circleimageview_endcolor,Color.parseColor("#4894e8"));
        backColor = Color.parseColor("#eaeaea");

        array.recycle();
        init();
    }

    private void init(){
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(backColor);
        backPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(backColor);
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;

        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);

        borderPaint.setShader(mShader);
        backPaint.setXfermode(null);

        if(isRound){
            radius = height/2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap src = Bitmap.createBitmap(width,height,
                Bitmap.Config.ARGB_8888);
        Canvas srcCanvas = new Canvas(src);
        super.onDraw(srcCanvas);

        Bitmap dst = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas dstCanvas = new Canvas(dst);

        backPaint.setXfermode(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dstCanvas.drawRoundRect(0,0,width,height,radius, radius,
                    backPaint);
        }

        canvas.drawBitmap(src, 0.0f, 0.0f, backPaint);

        backPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        srcCanvas.drawBitmap(dst, 0.0f, 0.0f, backPaint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(borderWidth/2,borderWidth/2,width-borderWidth/2,height-borderWidth/2,radius-borderWidth,radius-borderWidth,borderPaint);
        }
    }

}
