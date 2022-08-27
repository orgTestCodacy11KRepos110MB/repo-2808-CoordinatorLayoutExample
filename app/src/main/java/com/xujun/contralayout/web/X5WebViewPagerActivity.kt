package com.xujun.contralayout.web

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import android.os.Bundle
import android.view.View
import com.xujun.contralayout.web.behavior.HeaderNestedScroll
import com.xujun.contralayout.FragmentTab
import com.xujun.contralayout.R
import com.xujun.contralayout.base.BasePagerAdapter
import java.util.ArrayList

class X5WebViewPagerActivity : AppCompatActivity() {
    lateinit var mViewPager: ViewPager

    private val TAG = "WebViewPagerActivity"

    private lateinit var mTabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5_web)
        // 第一步，初始化ViewPager和TabLayout
        mViewPager = findViewById<View>(R.id.viewpager) as ViewPager
        mTabLayout = findViewById<View>(R.id.tabs) as TabLayout
        setupViewPager()
    }

    private fun setupViewPager() {
        val arrayList = ArrayList<FragmentTab>()
        val fragment =
            NestedX5WebFragment.newInstance("https://juejin.cn/user/2207475076966584/posts")
        fragment.iNestedScroll = HeaderNestedScroll(mTabLayout, mViewPager)
        arrayList.add(
            FragmentTab(
                "主页",
                fragment
            )
        )
        arrayList.add(
            FragmentTab(
                "github",
                NestedX5WebFragment.newInstance("https://gdutxiaoxu.github.io/")
            )
        )
        // 第二步：为ViewPager设置适配器
        val adapter = BasePagerAdapter(supportFragmentManager, arrayList)
        mViewPager!!.adapter = adapter
        //  第三步：将ViewPager与TableLayout 绑定在一起
        mTabLayout!!.setupWithViewPager(mViewPager)

    }

}