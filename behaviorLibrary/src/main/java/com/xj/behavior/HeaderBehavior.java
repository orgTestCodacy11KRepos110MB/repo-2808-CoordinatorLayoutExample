package com.xj.behavior;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.xj.behavior.base.HeaderFlingRunnable;
import com.xj.behavior.base.ViewOffsetBehavior;
import com.xj.behavior.widget.NestedLinearLayout;

import java.lang.ref.WeakReference;

/**
 * Created by jun xu on 19-3-4
 */
public class HeaderBehavior extends ViewOffsetBehavior {

    private static final String TAG = "GroupHeaderBehavior";

    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;

    private OnPagerStateListener mPagerStateListener;
    HeaderFlingRunnable.OnHeaderFlingListener mOnHeaderFlingListener;

    private float mLastY;
    private int mOffestY;
    private long mDownTime;
    private boolean mIsUp;
    private int mHeaderOffsetRange;
    private Context mContext;
    private int mTouchSlop;
    private HeaderFlingRunnable mFlingRunnableHeader;

    private boolean mIsFling;
    private int mMinimumFlingVelocity;
    private boolean mCouldScroollOpen = true;
    private int mLastTranlationY;
    private boolean mLastStopIsClose;


    public HeaderBehavior() {
        init();
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setCouldScroollOpen(boolean couldScroollOpen) {
        mCouldScroollOpen = couldScroollOpen;
    }

    public void setOnHeaderFlingListener(HeaderFlingRunnable.OnHeaderFlingListener onHeaderFlingListener) {
        mOnHeaderFlingListener = onHeaderFlingListener;
    }

    public void setPagerStateListener(OnPagerStateListener pagerStateListener) {
        mPagerStateListener = pagerStateListener;
    }

    public void setHeaderOffsetRange(int offsetRange) {
        mHeaderOffsetRange = offsetRange;
    }

    private void init() {
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
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
        return isVertical && !mIsFling;
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


        logD(TAG, " onNestedPreScroll child = " + child + " target =" + target);
        //dy>0 scroll up;dy<0,scroll down
        if (dy < 0) {
            return;
        }

        logD(TAG, "onNestedPreScroll: dy=" + dy + " child.getTranslationY() = " + child.getTranslationY());

        float halfOfDis = dy;
        if (!canScroll(child, halfOfDis)) {
            int headerOffsetRange = getHeaderOffsetRange();

            if (child.getTranslationY() != headerOffsetRange) {
                logD(TAG, "onNestedPreScroll: dy=" + dy + " child.getTranslationY() = " + child.getTranslationY()
                        + " headerOffsetRange=" + headerOffsetRange);
                child.setTranslationY(headerOffsetRange);
                onScrollChange(type, headerOffsetRange);
            }


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

            if (mLastTranlationY == translationY) {
                return;
            }

            logD(TAG, " onScrollChange translationY = " + translationY + " mLastTranlationY = " + mLastTranlationY);

            if (translationY == 0) {
                mPagerStateListener.onPagerOpened();
            }

            mPagerStateListener.onScrollChange(mIsUp, translationY, type);
            mLastTranlationY = translationY;


            if (translationY <= mHeaderOffsetRange) {
                mPagerStateListener.onPagerClosed();
                mLastStopIsClose = true;
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {


        if (!mCouldScroollOpen) {
            if (isClosed(child)) {
                return;
            }
        }

        if (mIsFling || dyUnconsumed > 0) {
            return;
        }

        logD(TAG, "onNestedScroll: dyConsumed=" + dyConsumed + "  dyUnconsumed= " + dyUnconsumed + "mIsFling " + mIsFling);

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
    public boolean onNestedPreFling(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        // consumed the flinging behavior until Closed
        boolean b = !isClosed(child);
        logD(TAG, " onNestedPreFling b = " + b + " mIsUp =" + mIsUp + "velocityY =" + velocityY + " mMinimumFlingVelocity =" + mMinimumFlingVelocity);

        if (mIsUp && Math.abs(velocityY) > mMinimumFlingVelocity && isHeaderFling(target)) {
//            handleActionUp(coordinatorLayout, child);
            tryToInitFlingRunnable(coordinatorLayout, child);

            mFlingRunnableHeader.setTarget(target);

            mFlingRunnableHeader.startFling((int) (getHeaderOffsetRange() - child.getTranslationY()), velocityX, velocityY);
            return true;
        }

        if (mIsUp) {
            return false;
        } else {
            return false;
        }

    }

    private void tryToInitFlingRunnable(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull View child) {
        if (mFlingRunnableHeader == null) {
            mFlingRunnableHeader = new HeaderFlingRunnable(coordinatorLayout, child);
            mFlingRunnableHeader.setOnScrollChangeListener(new HeaderFlingRunnable.OnHeaderFlingListener() {

                @Override
                public void onFlingFinish() {
                    mIsFling = false;
                    if (mOnHeaderFlingListener != null) {
                        mOnHeaderFlingListener.onFlingFinish();
                    }


                }

                @Override
                public void onFlingStart(View child, View target, float velocityX, float velocityY) {
                    mIsFling = true;
                    if (mOnHeaderFlingListener != null) {
                        mOnHeaderFlingListener.onFlingStart(child, target, velocityX, velocityY);
                    }
                }

                @Override
                public void onHeaderClose() {

                    if (mOnHeaderFlingListener != null) {
                        mOnHeaderFlingListener.onHeaderClose();
                    }

                    if (mPagerStateListener != null) {
                        mPagerStateListener.onPagerClosed();
                    }
                }

                @Override
                public void onHeaderOpen() {

                    if (mOnHeaderFlingListener != null) {
                        mOnHeaderFlingListener.onHeaderOpen();
                    }

                    if (mPagerStateListener != null) {
                        mPagerStateListener.onPagerOpened();
                    }
                }
            });
        }
    }

    private boolean isHeaderFling(@NonNull View target) {
        return target instanceof NestedLinearLayout;
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

        boolean closed = isClosed();
        if (!mLastStopIsClose && closed) {
            if (mPagerStateListener != null) {
                mPagerStateListener.onPagerClosed();
            }
        }
        mLastStopIsClose = closed;
    }

    // Maximum offset, is a negative number
    private int getHeaderOffsetRange() {
        return (int) mHeaderOffsetRange;
    }


    private void logD(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, s);
        }
    }

    public void openPager() {
        tryToInitFlingRunnable(mParent.get(), mChild.get());
        mFlingRunnableHeader.setTarget(null);
        mFlingRunnableHeader.scrollToOpen();
    }

    public void closePager() {
        tryToInitFlingRunnable(mParent.get(), mChild.get());
        mFlingRunnableHeader.setTarget(null);
        mFlingRunnableHeader.scrollToClose(mHeaderOffsetRange);
    }


    private boolean isClosed(View child) {
        return child.getTranslationY() <= getHeaderOffsetRange();
    }

    public boolean isClosed() {
        return isClosed(mChild.get());
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
         * @param isUp isScroollUp
         * @param dy   child.getTanslationY
         * @param type touch or not touch, TYPE_TOUCH, TYPE_NON_TOUCH
         */
        void onScrollChange(boolean isUp, int dy, @ViewCompat.NestedScrollType int type);

        /**
         * do callback when pager opened
         */
        void onPagerOpened();
    }


}
