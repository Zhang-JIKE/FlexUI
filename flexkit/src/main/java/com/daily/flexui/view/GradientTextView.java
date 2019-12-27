package com.daily.flexui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;

public class GradientTextView extends BaseGradientView {

    private String text;
    private int textSize;
    private String fontName;
    private float boldSize;
    private boolean fakeBold;

    protected Paint textPaint;
    private Paint.FontMetricsInt fontMetrics;
    private Rect rect=new Rect();

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(AttributeSet attrs) {
        super.init(attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.GradientTextView, 0, 0);
        text = array.getString(R.styleable.GradientTextView_gradienttextview_text);
        textSize = (int) array.getDimension(R.styleable.GradientTextView_gradienttextview_textsize, DisplayUtils.sp2px(14));
        fontName = array.getString(R.styleable.GradientTextView_gradienttextview_fontassets);
        boldSize = array.getFloat(R.styleable.GradientTextView_gradienttextview_boldsize,2);
        fakeBold = array.getBoolean(R.styleable.GradientTextView_gradienttextview_fakebold,true);

        array.recycle();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(baseColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(boldSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.setLetterSpacing(0.05f);
        }
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setFakeBoldText(fakeBold);
        if(!TextUtils.isEmpty(fontName)) {
            textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontName));
        }
        fontMetrics = textPaint.getFontMetricsInt();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Shader mShader = new LinearGradient(0,0,width,height,
                new int[]{startColor,endColor},
                null,Shader.TileMode.MIRROR);
        textPaint.setShader(mShader);

        rect.set(0,0,width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text,width/2,baseline,textPaint);
    }

    @Override
    public int getWrapContentHeight() {
        return fontMetrics.descent-fontMetrics.ascent;
    }

    @Override
    public int getWrapContentWidth() {
        return (int) (textPaint.measureText(text));
    }
}
