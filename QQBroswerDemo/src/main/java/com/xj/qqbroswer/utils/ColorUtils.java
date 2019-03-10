package com.xj.qqbroswer.utils;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by jun xu on 19-3-6.
 */
public class ColorUtils {

    private static ArgbEvaluator  mArgbEvaluator = new ArgbEvaluator();;

    public static int argb(float alpha, float red, float green, float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }

    public static int argb(
            @IntRange(from = 0, to = 255) int alpha,
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int setColorAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int gradientColor(float ratio, int color1, int color2){
        return (int) mArgbEvaluator.evaluate(ratio, color1,color2);
    }

    public static Drawable producTintColorDrawable(Context context, @DrawableRes int resId, @ColorInt int color) {
        Drawable bmpDrawable = ContextCompat.getDrawable(context, resId);
        Drawable.ConstantState state = bmpDrawable.getConstantState();
        Drawable wrap = DrawableCompat.wrap(state == null ? bmpDrawable : state.newDrawable()).mutate();
        DrawableCompat.setTint(wrap, color);
        return wrap;
    }

}
