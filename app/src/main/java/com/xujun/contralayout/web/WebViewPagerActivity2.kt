package com.xujun.contralayout.web

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.xujun.contralayout.FragmentTab
import com.xujun.contralayout.R
import com.xujun.contralayout.UI.viewPager.AppBarStateChangeListener
import com.xujun.contralayout.base.BasePagerAdapter
import com.xujun.contralayout.web.jinxuan.x5.AppBarLayoutStateChangeListener
import com.xujun.contralayout.web.jinxuan.x5.DisableAbleAppBarLayoutBehavior

class WebViewPagerActivity2 : AppCompatActivity() {
    var mViewPager: ViewPager? = null

    private val TAG = "WebViewPagerActivity2"

    var isFirst = true
    var x5WebFragment: DashenX5WebFragment? = null
    lateinit var disableAbleAppBarLayoutBehavior: DisableAbleAppBarLayoutBehavior
    lateinit var appBarLayout: AppBarLayout


    private var mTabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5_web2)
        appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
        // 第一步，初始化ViewPager和TabLayout
        mViewPager = findViewById<View>(R.id.viewpager) as ViewPager
        mTabLayout = findViewById<View>(R.id.tabs) as TabLayout
        setupViewPager()
        val layoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior as DisableAbleAppBarLayoutBehavior
        disableAbleAppBarLayoutBehavior = behavior
//        appBarLayout.addOnOffsetChangedListener(object : AppBarLayoutStateChangeListener() {
//            override fun onStateChanged(appBarLayout: AppBarLayout?, state: AppBarStateChangeListener.State) {
//                super.onStateChanged(appBarLayout, state)
//                Log.i(TAG, "onStateChanged: state is $state")
//
//                if (state == AppBarStateChangeListener.State.COLLAPSED && x5WebFragment?.canGoBack() == true) {
//                    behavior.isEnabled = false
//                } else {
//                    behavior.isEnabled = true
//                }
//            }
//        })

        findViewById<View>(R.id.container_back).setOnClickListener {
            x5WebFragment?.checkGoBack()
        }

    }

    private fun setupViewPager() {
        val arrayList = ArrayList<FragmentTab>()
        val fragment = DashenX5WebFragment.newInstance("https://m.ds.163.com/")
        x5WebFragment = fragment
        fragment.iX5WebListener = object : IX5WebListener {
            override fun onClick() {
                TODO("Not yet implemented")
            }

            override fun canGoBack(canGoBack: Boolean) {
                findViewById<View>(R.id.container_back).setViewVisible(canGoBack)
                findViewById<View>(R.id.container_jinxuan).setViewVisible(!canGoBack)
                if (canGoBack) {
                    appBarLayout.setExpanded(false, true)
                    appBarLayout.postDelayed(object :Runnable{
                        override fun run() {
                            disableAbleAppBarLayoutBehavior.isEnabled = false
                        }

                    },200)
                } else {
                    disableAbleAppBarLayoutBehavior.isEnabled = true
                }
            }

        }
        arrayList.add(
            FragmentTab(
                "主页",
                fragment
            )
        )
        arrayList.add(
            FragmentTab(
                "github",
                SimpleWebFragment.newInstance("https://gdutxiaoxu.github.io/")
            )
        )
        // 第二步：为ViewPager设置适配器
        val adapter = BasePagerAdapter(supportFragmentManager, arrayList)
        mViewPager!!.adapter = adapter
        //  第三步：将ViewPager与TableLayout 绑定在一起
        mTabLayout!!.setupWithViewPager(mViewPager)
    }
}

fun View.setViewVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}