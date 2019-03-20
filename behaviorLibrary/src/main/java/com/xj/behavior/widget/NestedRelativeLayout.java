package com.xj.behavior.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;


/**
 * Created by jun xu on 19-3-4
 */
public class NestedRelativeLayout extends RelativeLayout implements NestedScrollingChild {

    private static final String TAG = "NestedLinearLayout";

    private final int[] offset = new int[2];
    private final int[] consumed = new int[2];

    private NestedScrollingChildHelper mScrollingChildHelper;
    private int lastY;
    private int mScaledTouchSlop;

    public NestedRelativeLayout(Context context) {
        this(context, null);
    }

    public NestedRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
            mScrollingChildHelper.setNestedScrollingEnabled(true);
        }

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mScaledTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        logD("onTouchEvent: =");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                logD("onTouchEvent: ACTION_DOWN=");
                lastY = (int) event.getRawY();
                // 当开始滑动的时候，告诉父view
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL
                        | ViewCompat.SCROLL_AXIS_VERTICAL);
                return true;

            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "onTouchEvent: ACTION_MOVE=");
                int y = (int) (event.getRawY());
                int dy = y - lastY;
                lastY = y;
                logD("onTouchEvent: lastY=" + lastY);
                logD("onTouchEvent: dy=" + dy);
                if (startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL) // 如果找到了支持嵌套滚动的父类
                        && dispatchNestedPreScroll(0, Math.abs(dy), consumed, offset)) {// 父类进行了一部分滚动
                }

        }
        return super.onTouchEvent(event);
    }

    private void logD(String s) {
        Log.d(TAG, s);
    }


    //    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getRawY();
                // 当开始滑动的时候，告诉父view
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL
                        | ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getRawY() - lastY) > mScaledTouchSlop) {
                    logD("onInterceptTouchEvent: ACTION_MOVE  mScaledTouchSlop =" + mScaledTouchSlop);
                    return true;
                }

        }
        return super.onInterceptTouchEvent(ev);
    }


    private NestedScrollingChildHelper getScrollingChildHelper() {
        return mScrollingChildHelper;
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed,
                                           int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy,
                consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY,
                                       boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX,
                velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX,
                velocityY);
    }
}
