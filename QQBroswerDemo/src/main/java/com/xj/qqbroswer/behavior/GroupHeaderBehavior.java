package com.xj.qqbroswer.behavior;

import android.content.Context;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.xj.behavior.base.ViewOffsetBehavior;
import com.xj.qqbroswer.BuildConfig;

import java.lang.ref.WeakReference;

/**
 * Created by jun xu on 19-3-4
 */
public class GroupHeaderBehavior extends ViewOffsetBehavior {

    private static final String TAG = "GroupHeaderBehavior";

    // about state
    public static final int STATE_OPENED = 0;
    public static final int STATE_CLOSED = 1;
    private int mCurState = STATE_OPENED;

    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;

    private OnPagerStateListener mPagerStateListener;

    private OverScroller mOverScroller;
    private float mLastY;
    private int mOffestY;
    private long mDownTime;
    private boolean mIsUp;
    private int mHeaderOffsetRange;
    private Context mContext;
    private int mTouchSlop;


    public GroupHeaderBehavior() {
        init();
    }

    public GroupHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setPagerStateListener(OnPagerStateListener pagerStateListener) {
        mPagerStateListener = pagerStateListener;
    }

    public void setHeaderOffsetRange(int offsetRange) {
        mHeaderOffsetRange = offsetRange;
    }

    private void init() {
        mOverScroller = new OverScroller(mContext.getApplicationContext());
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<CoordinatorLayout>(parent);
        mChild = new WeakReference<View>(child);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        logD(TAG, "isVertical = " + isVertical);
        return isVertical;
    }

    private boolean isClosed(View child) {
        return child.getTranslationY() <= getHeaderOffsetRange();
    }

    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }

    private void changeState(int newState) {
        if (mCurState != newState) {
            mCurState = newState;

            if (mPagerStateListener == null) {
                return;
            }

            if (mCurState == STATE_OPENED) {
                mPagerStateListener.onPagerOpened();
            } else {
                mPagerStateListener.onPagerClosed();
            }
        }

    }

    private boolean canScroll(View child, float pendingDy) {
        int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
        int headerOffsetRange = getHeaderOffsetRange();
        if (pendingTranslationY >= headerOffsetRange && pendingTranslationY <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent
            ev) {

        boolean closed = isClosed();
        logD(TAG, "onInterceptTouchEvent: closed=" + closed);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownTime = SystemClock.currentThreadTimeMillis();
                mLastY = ev.getRawY();
                logD(TAG, "onInterceptTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                mOffestY = (int) (ev.getRawY() - mLastY);
                logD(TAG, "onInterceptTouchEvent: ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                logD(TAG, "onInterceptTouchEvent: ACTION_CANCEL");
                break;

            case MotionEvent.ACTION_UP:
                logD(TAG, "onInterceptTouchEvent: ACTION_UP");
                long l = SystemClock.currentThreadTimeMillis() - mDownTime;
                logD(TAG, "onInterceptTouchEvent: l=" + l);
                if (l < 10) {
                    return false;
                }

        }

        mIsUp = mOffestY < 0;
        logD(TAG, "onInterceptTouchEvent: offestY =" + mOffestY + " mIsUp = " + mIsUp);

        if (ev.getAction() == MotionEvent.ACTION_UP && Math.abs(mOffestY) > mTouchSlop) {
//            handleActionUp(parent, child);
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        //dy>0 scroll up;dy<0,scroll down
        if (dy < 0) {
            return;
        }

        float halfOfDis = dy;
        if (!canScroll(child, halfOfDis)) {
            int headerOffsetRange = getHeaderOffsetRange();

            if (child.getTranslationY() != headerOffsetRange) {
                child.setTranslationY(headerOffsetRange);
                onScrollChange(type, headerOffsetRange);
            }

            logD(TAG, "onNestedPreScroll: dy=" + dy + " child.getTranslationY() = " + child.getTranslationY()
                    + " headerOffsetRange=" + headerOffsetRange);
        } else {
            float translationY = child.getTranslationY();
            float finalTraY = translationY - halfOfDis;
            child.setTranslationY(finalTraY);

            onScrollChange(type, (int) finalTraY);

            //consumed all scroll behavior after we started Nested Scrolling
            consumed[1] = dy;

            logD(TAG, "onNestedPreScroll: translationY=" + translationY + " dy=" + dy + " " +
                    "finalTraY=" + finalTraY);
        }

    }

    private void onScrollChange(int type, int translationY) {
        if (mPagerStateListener != null) {
            logD(TAG, " onScrollChange translationY = " + translationY);
            mPagerStateListener.onScrollChange(mIsUp, translationY, type);
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        logD(TAG, "onNestedScroll: dyConsumed=" + dyConsumed + "  dyUnconsumed= " + dyUnconsumed);

        if (dyUnconsumed > 0) {
            return;
        }

        float translationY = child.getTranslationY() - dyUnconsumed;
        int maxHeadTranslateY = 0;
        if (translationY > maxHeadTranslateY) {
            translationY = maxHeadTranslateY;
        }

        logD(TAG, "onNestedScroll: translationY=" + translationY);

        if (child.getTranslationY() != translationY) {
            onScrollChange(type, (int) translationY);
            child.setTranslationY(translationY);
        }


    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        // consumed the flinging behavior until Closed
        boolean b = !isClosed(child);
        logD(TAG, " onNestedPreFling b = " + b + " mIsUp =" + mIsUp);
        if (mIsUp) {
            return false;
        } else {
            return b;
        }

    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        logD(TAG, "onNestedFling velocityY = " + velocityY);
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
      /*  if (type == ViewCompat.TYPE_TOUCH && Math.abs(mOffestY) > 5 && isClosed(child)) {
            logD(TAG, "onStopNestedScroll handleActionUp type = " + type);
            handleActionUp(coordinatorLayout, child);
        }*/

        logD(TAG, "onStopNestedScroll handleActionUp child.getTranslationY() = " + child.getTranslationY());
        onScrollChange(type, (int) child.getTranslationY());
    }

    // Maximum offset, is a negative number
    private int getHeaderOffsetRange() {
        return (int) mHeaderOffsetRange;
    }

    private void handleActionUp(CoordinatorLayout parent, final View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }

        mFlingRunnable = new FlingRunnable(parent, child);

        //notice  is a negative number
        float targetHeight = getHeaderOffsetRange() / 8.0f;
        float childTranslationY = child.getTranslationY();

        logD(TAG, "handleActionUp: childTranslationY=" + childTranslationY + "targetHeight=" +
                targetHeight + " mCurState= " + mCurState);

        if (mCurState == STATE_OPENED) {

            if (childTranslationY < targetHeight) {
                mFlingRunnable.scrollToClosed(DURATION_SHORT);
            } else {
                mFlingRunnable.scrollToOpen(DURATION_SHORT);
            }

        } else if (mCurState == STATE_CLOSED) {

            float percent = 1 - Math.abs(childTranslationY * 1.0f / getHeaderOffsetRange());
            childTranslationY = getHeaderOffsetRange() - childTranslationY;
            logD(TAG, "handleActionUp: childTranslationY=" + childTranslationY + "percent=" +
                    percent);

            if (percent < 0.15) {
                mFlingRunnable.scrollToClosed(DURATION_SHORT);
            } else {
                mFlingRunnable.scrollToOpen(DURATION_SHORT);
            }

        }


    }

    private void logD(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, s);
        }
    }

    private void onFlingFinished(CoordinatorLayout coordinatorLayout, View layout) {
        changeState(isClosed(layout) ? STATE_CLOSED : STATE_OPENED);
    }

    public void openPager() {
        openPager(DURATION_LONG);
    }

    /**
     * @param duration open animation duration
     */
    public void openPager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (isClosed() && child != null) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToOpen(duration);
        }
    }

    public void closePager() {
        closePager(DURATION_LONG);
    }

    /**
     * @param duration close animation duration
     */
    public void closePager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (!isClosed()) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToClosed(duration);
        }
    }

    private FlingRunnable mFlingRunnable;

    /**
     * For animation , Why not use {@link android.view.ViewPropertyAnimator } to play animation
     * is of the
     * other {@link CoordinatorLayout.Behavior} that depend on this could not receiving the
     * correct result of
     * {@link View#getTranslationY()} after animation finished for whatever reason that i don't know
     */
    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            float dy = getHeaderOffsetRange() - curTranslationY;

            logD(TAG, "scrollToOpen: curTranslationY = " + curTranslationY + "dy = " + dy);

            mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy +
                    0.1f), duration);
            start();
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            logD(TAG, "scrollToOpen: curTranslationY = " + curTranslationY);
            mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY,
                    duration);
            start();
        }

        private void start() {
            if (mOverScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mParent, mLayout);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
                onFlingFinished(mParent, mLayout);
            }
        }

        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    logD(TAG, "run: " + mOverScroller.getCurrY());
                    ViewCompat.setTranslationY(mLayout, mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                    onScrollChange(ViewCompat.TYPE_NON_TOUCH, mOverScroller.getCurrY());
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }

    /**
     * callback for HeaderPager 's state
     */
    public interface OnPagerStateListener {
        /**
         * do callback when pager closed
         */
        void onPagerClosed();

        /**
         * when scrooll, it would call back
         *
         * @param isUp
         * @param dy   child.getTanslationY
         * @param type
         */
        void onScrollChange(boolean isUp, int dy, @ViewCompat.NestedScrollType int type);

        /**
         * do callback when pager opened
         */
        void onPagerOpened();
    }


}
