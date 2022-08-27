package com.netease.cc.main.fragment.jinxuan.x5

import android.view.MotionEvent
import android.view.View
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension
import com.tencent.smtt.sdk.WebViewCallbackClient

class X5ProxyWebViewClientExtension(var mCallbackClient: WebViewCallbackClient) : ProxyWebViewClientExtension() {


    override fun invalidate() {}
    override fun onReceivedViewSource(data: String) {}
    override fun onTouchEvent(event: MotionEvent, view: View): Boolean {
        return mCallbackClient.onTouchEvent(event, view)
    }

    // 1
    override fun onInterceptTouchEvent(ev: MotionEvent, view: View): Boolean {
        return mCallbackClient.onInterceptTouchEvent(ev, view)
    }

    // 3
    override fun dispatchTouchEvent(ev: MotionEvent, view: View): Boolean {
        return mCallbackClient.dispatchTouchEvent(ev, view)
    }

    // 4
    override fun overScrollBy(
        deltaX: Int, deltaY: Int, scrollX: Int, scrollY: Int,
        scrollRangeX: Int, scrollRangeY: Int,
        maxOverScrollX: Int, maxOverScrollY: Int,
        isTouchEvent: Boolean, view: View
    ): Boolean {
        return mCallbackClient.overScrollBy(
            deltaX, deltaY, scrollX, scrollY,
            scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, view
        )
    }

    // 5
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int, view: View) {
        mCallbackClient.onScrollChanged(l, t, oldl, oldt, view)
    }

    // 6
    override fun onOverScrolled(
        scrollX: Int, scrollY: Int, clampedX: Boolean,
        clampedY: Boolean, view: View
    ) {

    }

    // 7
    override fun computeScroll(view: View) {
        mCallbackClient.computeScroll(view)
    }
}

