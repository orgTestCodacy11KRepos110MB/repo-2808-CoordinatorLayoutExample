package com.xujun.contralayout.web.jinxuan.x5

import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class HeaderNestedScroll(private val headContainer: View, private val contentContainer: View) :
    DisableAbleAppBarLayoutBehavior.INestedScroll {
    private val TAG = "HeaderNestedScroll"

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        Log.i(TAG, "onNestedPreScroll: dy is $dy")
        if (dy > 0) {
            val measuredHeight = headContainer.measuredHeight.toFloat()
            val translationY = headContainer.translationY
            val finalY = Math.max(translationY - dy, -measuredHeight)
            Log.i(
                TAG,
                "onNestedPreScroll: translationY is  $translationY, dy is $dy, translationY is $translationY"
            )
            headContainer.translationY = finalY
            contentContainer.translationY = finalY
        } else {
            if (headContainer.translationY != 0f) {
                val finalY = Math.min(headContainer.translationY - dy, 0f)
                headContainer.translationY = finalY
                contentContainer.translationY = finalY
            }
        }
    }
}