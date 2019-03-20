package com.xj.behavior.base;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.OverScroller;

/**
 * Created by jun xu on 19-3-7.
 */
public class HeaderFlingRunnable implements Runnable {

    private final View mChild;
    private final CoordinatorLayout mParent;
    private View mTarget;

    OverScroller mOverScroller;
    OnHeaderFlingListener mOnScrollChangeListener;
    private boolean mIsScroollClose;

    public HeaderFlingRunnable(CoordinatorLayout parent, View child) {
        mParent = parent;
        mChild = child;
        mOverScroller = new OverScroller(mChild.getContext());
    }

    public void setTarget(View target) {
        mTarget = target;
    }

    @Override
    public void run() {
        if (mOverScroller.computeScrollOffset()) {

            mChild.setTranslationY(mOverScroller.getCurrY());
            ViewCompat.postOnAnimation(mChild, this);

        } else {

            mOverScroller.abortAnimation();

            if (mOnScrollChangeListener != null) {
                mOnScrollChangeListener.onFlingFinish();
                if (mIsScroollClose) {
                    mOnScrollChangeListener.onHeaderClose();
                } else {
                    mOnScrollChangeListener.onHeaderOpen();
                }

            }


        }
    }

    public void startFling(int dy, float velocityX, float velocityY) {
        mIsScroollClose = true;
        mOverScroller.abortAnimation();
        mOverScroller.startScroll(0, (int) mChild.getTranslationY(), 0, dy, 100);
        ViewCompat.postOnAnimation(mChild, this);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onFlingStart(mChild, mTarget, velocityX, velocityY);
        }
    }

    public void scrollToClose(int headerOffset) {
        mIsScroollClose = true;
        mOverScroller.abortAnimation();
        mOverScroller.startScroll(0, (int) mChild.getTranslationY(), 0, (int) (headerOffset - mChild.getTranslationY()), 100);
        ViewCompat.postOnAnimation(mChild, this);
    }

    public void scrollToOpen() {
        mIsScroollClose = false;
        float curTranslationY = mChild.getTranslationY();
        mOverScroller.abortAnimation();
        mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY,
                200);
        ViewCompat.postOnAnimation(mChild, this);
    }

    public interface OnHeaderFlingListener {
        void onFlingFinish();

        void onFlingStart(View child, View target, float velocityX, float velocityY);

        void onHeaderClose();

        void onHeaderOpen();
    }

    public void setOnScrollChangeListener(OnHeaderFlingListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }
}
