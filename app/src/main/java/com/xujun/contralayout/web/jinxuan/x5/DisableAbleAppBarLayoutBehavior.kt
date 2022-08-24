package com.xujun.contralayout.web.jinxuan.x5

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior

class DisableAbleAppBarLayoutBehavior : Behavior {

    private val TAG = "DisableAbleAppBarLayout"
    var isEnabled = true

    var iNestedScroll: INestedScroll? = null

    constructor() : super() {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    init {
        init()
    }


    private fun init() {
        setDragCallback(object : DragCallback() {
            override fun canDrag(p0: AppBarLayout): Boolean {
                return isEnabled
            }

        })
    }


    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return isEnabled && super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        Log.i(
            TAG,
            "onNestedPreScroll before: dy is $dy,consumed[0] is ${consumed[0]},consumed[1] is ${consumed[1]}"
        )
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        Log.i(
            TAG,
            "onNestedPreScroll after: dy is $dy,consumed[0] is ${consumed[0]},consumed[1] is ${consumed[1]}"
        )
        iNestedScroll?.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.i(TAG, "onNestedPreScroll: dy is $dyUnconsumed,dxConsumed is $dxConsumed")
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type
        )
    }

    interface INestedScroll {
        fun onNestedPreScroll(
            coordinatorLayout: CoordinatorLayout,
            child: AppBarLayout,
            target: View,
            dx: Int,
            dy: Int,
            consumed: IntArray,
            type: Int
        )
    }
}