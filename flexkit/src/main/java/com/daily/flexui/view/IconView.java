package com.daily.flexui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.graphics.drawable.DrawableCompat;

import com.daily.flexui.R;
import com.daily.flexui.util.BitmapUtils;
import com.daily.flexui.util.DisplayUtils;

public class IconView extends BaseGradientView {

    private boolean isGradient;

    private Bitmap icon;

    private Paint iconPaint;

    private Rect srcRect,dstRect;

    private float scale;

    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IconView, 0, 0);
        isGradient = array.getBoolean(R.styleable.IconView_iconview_isgradient, true);
        icon = BitmapUtils.getBitmapById(getContext(), array.getResourceId(R.styleable.IconView_iconview_src, R.drawable.ic_msg));

        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconPaint.setColor(baseColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(isGradient){
            Shader mShader = new LinearGradient(0,0,width,height,
                    new int[]{startColor,endColor},
                    null,Shader.TileMode.MIRROR);
            iconPaint.setShader(mShader);
            iconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        }

        srcRect=new Rect(0,0,icon.getWidth(),icon.getHeight());

        scale = DisplayUtils.dp2px(24)/srcRect.width();

        dstRect=new Rect(getPaddingLeft(),getPaddingTop(),width-getPaddingRight(),height-getPaddingBottom());


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isGradient) {
            canvas.drawColor(Color.TRANSPARENT);
            canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.drawRect(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom(), iconPaint);
        }
        canvas.drawBitmap(icon,srcRect,dstRect,iconPaint);
    }

    @Override
    public int getWrapContentWidth() {
        return super.getWrapContentWidth()+DisplayUtils.dp2px(24);
    }

    @Override
    public int getWrapContentHeight() {
        return super.getWrapContentHeight()+DisplayUtils.dp2px(24);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
}
