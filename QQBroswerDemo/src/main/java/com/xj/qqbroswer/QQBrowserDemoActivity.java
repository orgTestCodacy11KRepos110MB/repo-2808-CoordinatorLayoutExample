package com.xj.qqbroswer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xj.behavior.ContentBehavior;
import com.xj.behavior.HeaderBehavior;
import com.xj.qqbroswer.adapter.TestFragmentAdapter;
import com.xj.qqbroswer.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 *
 * @author xujun
 * @time 19-3-10
 */
public class QQBrowserDemoActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "MainActivity";
    private ViewPager mNewsPager;
    private TabLayout mTableLayout;
    private List<TestFragment> mFragments;

    private HeaderBehavior mHeaderBehavior;
    private ContentBehavior mContentBehavior;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, QQBrowserDemoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.translucent(this, false);
        setContentView(R.layout.activity_qq_browser_demo);
        initData();
        initListener();
    }

    private void initListener() {

    }

    protected void initData() {
        initBehavior();
        mNewsPager = (ViewPager) findViewById(R.id.id_uc_news_content);
        mTableLayout = (TabLayout) findViewById(R.id.id_uc_news_tab);
        mFragments = new ArrayList<TestFragment>();
        for (int i = 0; i < 4; i++) {
            mFragments.add(TestFragment.newInstance(String.valueOf(i), false));
            mTableLayout.addTab(mTableLayout.newTab().setText("Tab" + i));
        }
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setOnTabSelectedListener(this);
        mNewsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTableLayout));
        mNewsPager.setAdapter(new TestFragmentAdapter(mFragments, getSupportFragmentManager()));
        findViewById(R.id.news_tv_header_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QQBrowserDemoActivity.this, "点击我了", Toast.LENGTH_SHORT).show();
            }
        });
//        setViewPagerScrollEnable(mNewsPager,false);
    }

    private void initBehavior() {
        Resources resources = DemoApplication.getAppContext().getResources();
        mHeaderBehavior = (HeaderBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager).getLayoutParams()).getBehavior();
        mHeaderBehavior.setPagerStateListener(new HeaderBehavior.OnPagerStateListener() {
            @Override
            public void onPagerClosed() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onPagerClosed: ");
                }
                Snackbar.make(mNewsPager, "pager closed", Snackbar.LENGTH_SHORT).show();
                setFragmentRefreshEnabled(true);
                setViewPagerScrollEnable(mNewsPager, true);
            }

            @Override
            public void onScrollChange(boolean isUp, int dy, int type) {

            }

            @Override
            public void onPagerOpened() {
                Snackbar.make(mNewsPager, "pager opened", Snackbar.LENGTH_SHORT).show();
                setFragmentRefreshEnabled(false);
            }
        });
        // 设置为 header height 的相反数
        mHeaderBehavior.setHeaderOffsetRange(-resources.getDimensionPixelOffset(R.dimen.header_height));
        // 设置 header close 的时候是否能够通过滑动打开
        mHeaderBehavior.setCouldScroollOpen(false);

        mContentBehavior = (ContentBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.behavior_content).getLayoutParams()).getBehavior();
        // 设置依赖于哪一个 id，这里要设置为 Header layout id
        mContentBehavior.setDependsLayoutId(R.id.id_uc_news_header_pager);
        // 设置 content 部分最终停留的位置
        mContentBehavior.setFinalY(resources.getDimensionPixelOffset(R.dimen.header_title_height));
    }

    private void openMyGitHub() {
        Uri uri = Uri.parse("https://github.com/gdutxiaoxu");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mNewsPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        if (mHeaderBehavior != null && mHeaderBehavior.isClosed()) {
            mHeaderBehavior.openPager();
        } else {
            super.onBackPressed();
        }
    }

    public void setViewPagerScrollEnable(ViewPager viewPager, boolean enable) {
        if (false == (viewPager instanceof FixedViewPager)) {
            return;
        }
        FixedViewPager fixViewPager = (FixedViewPager) viewPager;
        if (enable) {
            fixViewPager.setScrollable(true);
        } else {
            fixViewPager.setScrollable(false);
        }
    }

    private void setFragmentRefreshEnabled(boolean enabled) {
        for (Fragment fragment : mFragments) {
            ((TestFragment) fragment).setRefreshEnable(enabled);
        }
    }

    public void onButtonClick(View view) {
    }
}
