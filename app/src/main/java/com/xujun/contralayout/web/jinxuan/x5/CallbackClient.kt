package com.xujun.contralayout.web

import com.tencent.smtt.sdk.WebViewCallbackClient
import android.view.MotionEvent
import com.xujun.contralayout.web.CallbackClient
import android.annotation.TargetApi
import android.os.Build
import android.util.Log
import android.view.View
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension
import com.tencent.smtt.sdk.WebView

class CallbackClient(var webView: WebView) : WebViewCallbackClient {
    override fun invalidate() {}
    override fun onTouchEvent(event: MotionEvent, view: View): Boolean {
        Log.d(TAG, "ycycyc onTouchEvent")
        Log.i(TAG, "onTouchEvent: view is $view")
        return webView.super_onTouchEvent(event)
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    override fun overScrollBy(
        deltaX: Int, deltaY: Int, scrollX: Int,
        scrollY: Int, scrollRangeX: Int, scrollRangeY: Int,
        maxOverScrollX: Int, maxOverScrollY: Int,
        isTouchEvent: Boolean, view: View
    ): Boolean {
        Log.e("0705", "overScrollBy")
        return webView.super_overScrollBy(
            deltaX, deltaY, scrollX, scrollY,
            scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
            isTouchEvent
        )
    }

    override fun computeScroll(view: View) {
        webView.super_computeScroll()
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onOverScrolled(
        scrollX: Int, scrollY: Int, clampedX: Boolean,
        clampedY: Boolean, view: View
    ) {
        webView.super_onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int, view: View) {
        webView.super_onScrollChanged(l, t, oldl, oldt)
    }

    override fun dispatchTouchEvent(ev: MotionEvent, view: View): Boolean {
        return webView.super_dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent, view: View): Boolean {
        Log.i(TAG, "onInterceptTouchEvent: view is $view")
        return webView.super_onInterceptTouchEvent(ev)
    }

    companion object {
        private const val TAG = "CallbackClient"
    }
}

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