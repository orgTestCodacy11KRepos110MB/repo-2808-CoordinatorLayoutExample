package com.xujun.contralayout.web

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout.ScrollingViewBehavior

class MyScrollBehavior : ScrollingViewBehavior {
    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    private val TAG = "MyScrollBehavior"

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        Log.i(
            TAG,
            "onNestedPreScroll: dy is $dy,consumed[0] is ${consumed[0]},consumed[1] is ${consumed[1]}"
        )
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
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
        Log.i(TAG, "onNestedScroll: dyConsumed is $dyConsumed,dyConsumed is $dyConsumed")
    }


}