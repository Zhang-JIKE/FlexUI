package com.daily.flexui.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;
import com.daily.flexui.util.FunctionUtils;
import com.daily.flexui.viewgroup.OverScrollView;

public class TitleLayout extends LinearLayout {

    private int width;
    private int height;

    private String text;
    private int textColor;

    private float minTitleScale;
    private float maxTitleScale;
    private float textSize;

    private int textMarginLeft;
    private int textMarginTop;
    private int contentMarginTop;

    private Paint textPaint;

    private TextView textView;
    private OverScrollView scrollView;

    public TitleLayout(Context context) {
        super(context);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TitleLayout, 0, 0);
        text = array.getString(R.styleable.TitleLayout_titlelayout_text);
        textColor = array.getColor(R.styleable.TitleLayout_titlelayout_text_color, getResources().getColor(R.color.black));

        minTitleScale = array.getDimension(R.styleable.TitleLayout_titlelayout_text_minscale,0.97f);
        maxTitleScale = array.getDimension(R.styleable.TitleLayout_titlelayout_text_maxscale,1.03f);
        textSize = array.getDimension(R.styleable.TitleLayout_titlelayout_text_size,32);

        textMarginLeft = (int) array.getDimension(R.styleable.TitleLayout_titlelayout_title_marginleft, DisplayUtils.dp2px(24));
        textMarginTop = (int) array.getDimension(R.styleable.TitleLayout_titlelayout_title_margintop, DisplayUtils.dp2px(50));
        contentMarginTop = (int) array.getDimension(R.styleable.TitleLayout_titlelayout_content_margintop, DisplayUtils.dp2px(50));

        array.recycle();

        init();
    }

    public void init() {
        setOrientation(VERTICAL);
        setWillNotDraw(false);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        textView = new TextView(getContext());
        params.topMargin = textMarginTop;
        params.leftMargin = textMarginLeft;
        params.bottomMargin = contentMarginTop;

        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setMaxLines(1);
        textView.setTextSize(DisplayUtils.px2sp(textSize));
        textView.setLayoutParams(params);
        textView.setPivotX(0);
        textView.setPivotY(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setLetterSpacing(0.05f);
        }
        textView.setTextColor(Color.parseColor("#6f717d"));
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ProximaSansMedium.ttf"));
        addView(textView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() != 2){
            throw new IllegalArgumentException("This Layout must have just one child");
        } else {
           View view = getChildAt(1);
           if(view != null) {
               if (view instanceof OverScrollView) {
                   scrollView = (OverScrollView) view;
                   scrollView.setOnOverScrollLisener(new OverScrollView.OnOverScrollLisener() {
                       @Override
                       public void onOverScroll(int offset) {
                           float ts = FunctionUtils.getFunctionValue(offset, 200, -200, maxTitleScale, minTitleScale,1);
                           //Log.e("OFFSET","offset:"+offset+" size:"+ts);
                           textView.setScaleX(ts);
                           textView.setScaleY(ts);
                       }
                   });
               } else {
                   throw new IllegalArgumentException("This Layout must have just one child and must be OverScrollView");
               }
           } else {
               throw new IllegalArgumentException("This Layout must have just one child");
           }
        }
    }
}
