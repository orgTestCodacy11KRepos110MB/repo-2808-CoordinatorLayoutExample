package com.xujun.contralayout.web.behavior


interface INestedScroll {
    fun onNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    )
}