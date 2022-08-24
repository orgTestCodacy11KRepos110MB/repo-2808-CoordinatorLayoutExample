package com.xujun.contralayout.web

import android.util.Log
import android.view.*
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import androidx.customview.widget.ViewDragHelper
import android.widget.OverScroller
import androidx.core.view.ViewCompat


open class NestedScrollProxy(val view: View) : NestedScrollingChild2 {
    private var mLastMotionY = 0

    /**
     * 用于跟踪触摸事件速度的辅助类，用于实现
     * fling 和其他类似的手势。
     */
    private var mVelocityTracker: VelocityTracker? = null

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private var mIsBeingDragged = false

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.（多点触控有用）
     */
    private var mActivePointerId = ViewDragHelper.INVALID_POINTER
    private val mTouchSlop: Int
    private val mMinimumVelocity: Int
    private val mMaximumVelocity: Int
    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)
    private val mScroller: OverScroller = OverScroller(view.getContext())

    private var mNestedYOffset = 0
    private var mLastScrollerY = 0
    private var mLastY = 0
    private var moveDistance = 0

    private val scrollingChildHelper: NestedScrollingChildHelper by lazy {
        NestedScrollingChildHelper(view)
    }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }


    fun onTouchEvent(event: MotionEvent?): Boolean {
        initVelocityTrackerIfNotExists()
        var eventAddedToVelocityTracker = false
        val vtEv = MotionEvent.obtain(event) //复制一个event
        val actionMasked = event?.actionMasked //类似getAction
        var result = false
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
            isFlinging = false
        }
        vtEv.offsetLocation(0f, mNestedYOffset.toFloat())
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished.also { mIsBeingDragged = it }) {
                    val parent = getParent()
                    parent.requestDisallowInterceptTouchEvent(true)
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.//如果在fling 的过程中用户触摸屏幕，则停止fling
                 */
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                Log.i(TAG, "onTouchEvent: mLastMotionY is $mLastMotionY")

                // Remember where the motion event started
                mLastMotionY = event.y.toInt()
                mLastY = mLastMotionY
                mActivePointerId = event.getPointerId(0)
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = event.findPointerIndex(mActivePointerId)
                if (activePointerIndex == -1) {
                    return result
                }
                val y = event.getY(activePointerIndex).toInt()
                var deltaY = mLastMotionY - y
                Log.i(TAG, "onTouchEvent: deltaY is $deltaY")
                if (dispatchNestedPreScroll(
                        0, deltaY, mScrollConsumed, mScrollOffset,
                        ViewCompat.TYPE_TOUCH
                    )
                ) {
                    Log.d(TAG, "dispatchNestedPreScroll消费：" + mScrollConsumed[1])
                    deltaY -= mScrollConsumed[1] //纵轴位移- 被父布局消费的滑动距离
                    Log.d(TAG, "dispatchNestedPreScroll未消费：$deltaY")
                    //                    vtev.offsetLocation(0, mScrollOffset[1]);//这句加不加一样
                }
                moveDistance = deltaY
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    val parent = getParent()
                    parent.requestDisallowInterceptTouchEvent(true)
                    mIsBeingDragged = true
                }
                if (mIsBeingDragged) {
                    mLastMotionY = y - mScrollOffset[1] //上一次的坐标
                    var scrolledDeltaY = 0
                    var unconsumedY = deltaY
                    if (Math.abs(deltaY) > 0) {
                        if (deltaY <= 0) {
                            if (canScrollVertically(-1)) { //向顶部滑动
                                if (realScrollY + deltaY < 0) {
                                    scrolledDeltaY = -realScrollY
                                    unconsumedY = realScrollY + deltaY
                                    vtEv.offsetLocation(0f, unconsumedY.toFloat()) //这行不知对不对
                                    mNestedYOffset += unconsumedY
                                } else {
                                    scrolledDeltaY = deltaY
                                    unconsumedY = 0
                                }
                            }
                        } else {
                            if (canScrollVertically(1)) {
                                //todo 这里没有处理底部的事件传递给父布局，本例不需要
                                Log.d(TAG, "canScrollVertically:deltaY:$deltaY")
                                if (deltaY - getTop() > 0) {
                                    scrolledDeltaY = deltaY - getTop()
                                    unconsumedY = getTop()
                                    vtEv.offsetLocation(0f, unconsumedY.toFloat()) //这行不知对不对
                                    mNestedYOffset += unconsumedY
                                } else {
                                    scrolledDeltaY = deltaY
                                    unconsumedY = 0
                                }
                            }
                        }
                    }
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1]
                        vtEv.offsetLocation(0f, mScrollOffset[1].toFloat())
                        mNestedYOffset += mScrollOffset[1]
                    }
                }
                result = if (deltaY == 0 && mIsBeingDragged) {
                    true
                } else {
                    innerTouchEvent(event)
                }
                Log.i(
                    TAG,
                    String.format(
                        "onTouchEvent: deltaY is %s, mIsBeingDragged is %s, result is %s",
                        deltaY,
                        mIsBeingDragged,
                        result
                    )
                )
            }
            MotionEvent.ACTION_UP -> {
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.addMovement(vtEv)
                }
                eventAddedToVelocityTracker = true
                calculate(mActivePointerId, event.y.toInt())
                mLastY = event.y.toInt()
                mActivePointerId = ViewDragHelper.INVALID_POINTER
                endDrag()
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.clear()
                }
//                stopNestedScroll(ViewCompat.TYPE_TOUCH)
                result = innerTouchEvent(vtEv)
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = ViewDragHelper.INVALID_POINTER
                endDrag()
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.clear()
                }
                stopNestedScroll(ViewCompat.TYPE_TOUCH)
                result = innerTouchEvent(event)
            }
        }
        if (!eventAddedToVelocityTracker) {
            if (mVelocityTracker != null) {
                mVelocityTracker!!.addMovement(vtEv)
            }
        }
        vtEv.recycle()
        return result
    }

    private fun innerTouchEvent(event: MotionEvent): Boolean {
        return view.onTouchEvent(event)
    }

    private fun getTop(): Int {
        return view.top
    }

    private fun canScrollVertically(i: Int): Boolean {
        return view.canScrollVertically(i)
    }

    open fun getParent(): ViewParent {
        return view.parent
    }

    private var isFlinging = false

    /**
     * 处理fling 速度问题
     *
     * @param mActivePointerId
     * @param curY
     */
    private fun calculate(mActivePointerId: Int, curY: Int) {
        mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
        val initialVelocity = mVelocityTracker!!.getYVelocity(mActivePointerId).toInt()
        Log.d(TAG, " caculateV curY:$curY mLastY:$mLastY initialVelocity:$initialVelocity")
        if (Math.abs(initialVelocity) > mMinimumVelocity) {
            mLastScrollerY = realScrollY
            isFlinging = true
        }
    }

    private val realScrollY: Int
        private get() = view.scrollY

    fun computeScroll() {
        view.computeScroll()
        if (isFlinging) {
            val dy = realScrollY - mLastScrollerY
            if (realScrollY == 0) {
                val velocityY = 1000
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
                if (moveDistance < 0) {
                    dispatchNestedScroll(
                        0, dy, 0, -velocityY, null,
                        ViewCompat.TYPE_NON_TOUCH
                    )
                }
                isFlinging = false
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            }
        }
        Log.d(TAG, "computeScroll webView:getScrollY: $realScrollY, isFlinging is $isFlinging")
    }

    private fun endDrag() {
        mIsBeingDragged = false
        recycleVelocityTracker()
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }


    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return scrollingChildHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        val stackTraceString = Log.getStackTraceString(IllegalStateException("stop"))
        Log.e(TAG, "stopNestedScroll: stackTraceString is \n${stackTraceString}")

        scrollingChildHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return scrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return scrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return scrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        scrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return scrollingChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return scrollingChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        scrollingChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return scrollingChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return scrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return scrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.i(TAG, "dispatchNestedFling: velocityY is $velocityY")
        return scrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        Log.i(TAG, "dispatchNestedPreFling: velocityY is $velocityY")
        return scrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    companion object {
        private const val TAG = "NestedWebView2"
    }

    init {
        val configuration = ViewConfiguration.get(view.getContext())
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
        isNestedScrollingEnabled = true //设置支持嵌套滑动
    }
}