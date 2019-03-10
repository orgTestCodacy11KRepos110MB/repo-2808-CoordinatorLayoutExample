package com.xj.qqbroswer.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jun xu on 19-3-7.
 */
public class ViewUtils {

    public static void setViewMargin(View v, Rect rect) {
        if (v == null || rect == null) {
            return;
        }

        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
            layoutParams.leftMargin = rect.left;
            layoutParams.rightMargin = rect.right;
            layoutParams.bottomMargin = rect.bottom;
            layoutParams.topMargin = rect.top;
            v.setLayoutParams(layoutParams);
        }
    }

}
