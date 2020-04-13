package com.daily.flexui.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;


public class NeonLayout extends FrameLayout {

    private int width = DisplayUtils.dp2px(6);

    private int height = DisplayUtils.dp2px(6);

    private int lightRadius = DisplayUtils.dp2px(3);

    private int radius = 0;

    private int color;

    private boolean isSolid;
    private boolean isRound;

    private Paint paint,paintForeGround;

    public NeonLayout(Context context) {
        super(context);
    }

    public NeonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NeonLayout, 0, 0);
        lightRadius = (int) array.getDimension(R.styleable.NeonLayout_neonlayout_light_radius,lightRadius);
        radius = (int) array.getDimension(R.styleable.NeonLayout_neonlayout_radius,0);
        color = array.getColor(R.styleable.NeonLayout_neonlayout_lightcolor, getResources().getColor(R.color.back0));
        isSolid = array.getBoolean(R.styleable.NeonLayout_neonlayout_issolid,false);
        isRound = array.getBoolean(R.styleable.NeonLayout_neonlayout_isround,false);

        array.recycle();

        setClipChildren(false);
        setWillNotDraw(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setMaskFilter(new BlurMaskFilter(lightRadius, BlurMaskFilter.Blur.OUTER));
        paint.setAlpha(100);

        paintForeGround = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForeGround.setColor(color);
}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();

        width = measureWidth(minimumWidth, widthMeasureSpec);
        height = measureHeight(minimumHeight, heightMeasureSpec);

        if(isRound){
            radius = height/2;
        }

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int defaultWidth, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = getMeasuredWidth()+lightRadius*2;
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize+lightRadius*2;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = getMeasuredHeight()+lightRadius*2;
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize+lightRadius*2;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                break;
        }
        return defaultHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(lightRadius,lightRadius,width-lightRadius,height-lightRadius,radius,radius,paint);

            if(isSolid){
                canvas.drawRoundRect(lightRadius,lightRadius,width-lightRadius,height-lightRadius,radius,radius,paintForeGround);
            }
        }
    }
}
