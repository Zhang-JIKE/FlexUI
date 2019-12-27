package com.daily.flexui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.daily.flexui.R;
import com.daily.flexui.util.DisplayUtils;


public class TagView extends AppCompatTextView {

    int textColor;

    int backColor;

    int horizontalPadding;

    int vertiPadding;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textColor = getTextColors().getDefaultColor();
        backColor = Color.argb(30,Color.red(textColor),Color.green(textColor),Color.blue(textColor));

        horizontalPadding = DisplayUtils.dp2px(12);
        vertiPadding = DisplayUtils.dp2px(3.5f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setLetterSpacing(0.06f);
        }

        Typeface typeface = Typeface.create("sans-serif-black", Typeface.NORMAL);
        setTypeface(typeface);

        setPadding(horizontalPadding,vertiPadding,horizontalPadding,vertiPadding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setForeground(getResources().getDrawable(R.drawable.ripple));
        }
        setBackgroundResource(R.drawable.shape_tab);
        getBackground().setColorFilter(backColor, PorterDuff.Mode.SRC);

        setClickable(true);
    }

}
