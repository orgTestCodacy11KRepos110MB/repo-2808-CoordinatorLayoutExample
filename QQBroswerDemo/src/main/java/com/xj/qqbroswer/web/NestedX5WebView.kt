package com.xj.qqbroswer.web

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import kotlin.jvm.JvmOverloads
import com.tencent.smtt.sdk.WebView


/**
 * 适配 X5 webView
 */
class NestedX5WebView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    WebView(
        context!!, attrs
    ) {

    private val TAG = "NestedWebView"
    val nestedScrollProxy = object : NestedScrollProxy(view) {
        override fun getParent(): ViewParent {
            return view.parent
        }
    }


    init {
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return nestedScrollProxy.onTouchEvent(event!!)
            }

        })

    }

    override fun computeScroll() {
        super.computeScroll()
        Log.i(TAG, "computeScroll: ")
        nestedScrollProxy.computeScroll()
    }

}