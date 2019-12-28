package com.daily.flexui.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.graphics.drawable.DrawableCompat;

public class BitmapUtils {

    public static Bitmap getBitmapById(Context context, int drawableId) {
        @SuppressLint("RestrictedApi")
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        drawable = (DrawableCompat.wrap(drawable)).mutate();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap blur(){
        return null;
    }
}
