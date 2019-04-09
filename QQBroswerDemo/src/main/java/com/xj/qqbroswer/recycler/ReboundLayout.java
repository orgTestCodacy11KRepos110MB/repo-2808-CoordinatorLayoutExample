package com.xj.qqbroswer.recycler;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by jun xu on 19-4-9.
 */
public class ReboundLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "ReboundLayout";

    private View mHeaderView;
    private View mFooterView;
    private static final int MAX_WIDTH = 200;
    private View mChildView;
    // 解决多点触控问题
    private boolean isRunAnim;
    private int mDrag = 5;//除数越大可以滑动的距离越短

    public ReboundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderView = new View(context);
        mHeaderView.setBackgroundColor(0xfff);
        mFooterView = new View(context);
        mFooterView.setBackgroundColor(0xfff);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChildView = getChildAt(0);

        // 左移
        if (getOrientation() == LinearLayout.HORIZONTAL) {
            LayoutParams layoutParams = new LayoutParams(MAX_WIDTH, LayoutParams.MATCH_PARENT);
            addView(mHeaderView, 0, layoutParams);
            addView(mFooterView, getChildCount(), layoutParams);
            scrollBy(MAX_WIDTH, 0);
        } else {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, MAX_WIDTH);
            addView(mHeaderView, 0, layoutParams);
            scrollBy(0, MAX_WIDTH);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = mChildView.getLayoutParams();
        if (getOrientation() == HORIZONTAL) {
            params.width = getMeasuredWidth();
        } else {
            params.height = getMeasuredHeight() + MAX_WIDTH;
        }


    }

    /**
     * 必须要复写 onStartNestedScroll后调用
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {

    }

    /**
     * 返回true代表处理本次事件
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (target instanceof RecyclerView && !isRunAnim) {
            return true;
        }
        return false;
    }

    /**
     * 复位初始位置
     */
    @Override
    public void onStopNestedScroll(View target) {
        startAnimation(new ProgressAnimation(getOrientation()));
    }

    /**
     * 回弹动画
     */
    private class ProgressAnimation extends Animation {
        private final int mOrientation;
        // 预留
        private float startProgress = 0;
        private float endProgress = 1;

        public ProgressAnimation(int orientation) {
            isRunAnim = true;
            mOrientation = orientation;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float progress = ((endProgress - startProgress) * interpolatedTime) + startProgress;
            Log.i(TAG, "applyTransformation: interpolatedTime =" + interpolatedTime);
            if (mOrientation == LinearLayout.HORIZONTAL) {
                scrollBy((int) ((MAX_WIDTH - getScrollX()) * progress), 0);
            } else {

                float v = (MAX_WIDTH - getScrollY()) * progress;
                Log.i(TAG, "applyTransformation: getScrollY() =" + getScrollY() + " progress = " + progress + " v =" + v);
                scrollBy(0, (int) v);
            }

            if (progress >= 1) {
                isRunAnim = false;
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(500);
            setInterpolator(new BounceInterpolator());
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
            dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // 如果在自定义ViewGroup之上还有父View交给我来处理
        getParent().requestDisallowInterceptTouchEvent(true);
        int orientation = getOrientation();
        if (orientation == LinearLayout.HORIZONTAL) {
            handleHorizontal(target, dx, consumed);
        } else {
            handleVertical(target, dy, consumed);
        }

    }

    private void handleVertical(View target, int dy, int[] consumed) {
        // dy>0 往左滑动 dy<0往右滑动
        boolean hiddenTop = dy > 0 && getScrollY() < MAX_WIDTH && !ViewCompat
                .canScrollVertically(target, -1);
        boolean showTop = dy < 0 && !ViewCompat.canScrollVertically(target, -1);
        boolean hiddenBottom = dy < 0 && getScrollY() > MAX_WIDTH && !ViewCompat
                .canScrollVertically(target, 1);
        boolean showBottom = dy > 0 && !ViewCompat.canScrollVertically(target, 1);
        if (hiddenTop || showTop || hiddenBottom || showBottom) {
            scrollBy(0, dy / mDrag);
            consumed[1] = dy;
        }

        // 限制错位问题
        if (dy > 0 && getScrollY() > MAX_WIDTH && !ViewCompat.canScrollVertically(target, -1)) {
            scrollTo(0, MAX_WIDTH);
        }
        if (dy < 0 && getScrollY() < MAX_WIDTH && !ViewCompat.canScrollVertically(target, 1)) {
            scrollTo(0, MAX_WIDTH);
        }
    }

    private void handleHorizontal(View target, int dx, int[] consumed) {
        // dx>0 往左滑动 dx<0往右滑动
        boolean hiddenLeft = dx > 0 && getScrollX() < MAX_WIDTH && !ViewCompat
                .canScrollHorizontally(target, -1);
        boolean showLeft = dx < 0 && !ViewCompat.canScrollHorizontally(target, -1);
        boolean hiddenRight = dx < 0 && getScrollX() > MAX_WIDTH && !ViewCompat
                .canScrollHorizontally(target, 1);
        boolean showRight = dx > 0 && !ViewCompat.canScrollHorizontally(target, 1);
        if (hiddenLeft || showLeft || hiddenRight || showRight) {
            scrollBy(dx / mDrag, 0);
            consumed[0] = dx;
        }

        // 限制错位问题
        if (dx > 0 && getScrollX() > MAX_WIDTH && !ViewCompat.canScrollHorizontally(target, -1)) {
            scrollTo(MAX_WIDTH, 0);
        }
        if (dx < 0 && getScrollX() < MAX_WIDTH && !ViewCompat.canScrollHorizontally(target, 1)) {
            scrollTo(MAX_WIDTH, 0);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        // 当RecyclerView在界面之内交给它自己惯性滑动
        if (getOrientation() == HORIZONTAL && getScrollX() == MAX_WIDTH) {
            return false;
        }

        if (getOrientation() == VERTICAL && getScrollY() == MAX_WIDTH) {
            return false;
        }

        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    /**
     * 限制滑动 移动x轴不能超出最大范围
     */
    @Override
    public void scrollTo(int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x > MAX_WIDTH * 2) {
            x = MAX_WIDTH * 2;
        }
        super.scrollTo(x, y);
    }
}
