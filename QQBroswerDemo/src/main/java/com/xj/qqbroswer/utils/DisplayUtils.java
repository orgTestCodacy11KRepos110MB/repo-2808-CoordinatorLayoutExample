package com.xj.qqbroswer.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;

public class DisplayUtils {
    // ====================== Setting ===========================
    private static final String XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar";

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static int dp2px(Context context, int dp) {
        return (int) (getDensity(context) * dp + 0.5);
    }

    public static int sp2px(Context context, int sp) {
        return (int) (getFontDensity(context) * sp + 0.5);
    }

    public static int px2dp(Context context, int px) {
        return (int) (px / getDensity(context) + 0.5);
    }

    public static int px2sp(Context context, int px) {
        return (int) (px / getFontDensity(context) + 0.5);
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        int screenHeight = getDisplayMetrics(context).heightPixels;
        if (DeviceUtils.isXiaomi() && xiaomiNavigationGestureEnabled(context)) {
            screenHeight += getResourceNavHeight(context);
        }
        return screenHeight;
    }

    private static int getResourceNavHeight(Context context) {
        // 小米4没有nav bar, 而 navigation_bar_height 有值
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    public static boolean xiaomiNavigationGestureEnabled(Context context) {
        int val = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val = Settings.Global.getInt(context.getContentResolver(), XIAOMI_FULLSCREEN_GESTURE, 0);
        }
        return val != 0;
    }
}
