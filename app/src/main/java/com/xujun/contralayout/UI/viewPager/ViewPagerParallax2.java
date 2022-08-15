package com.xujun.contralayout.UI.viewPager;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.xujun.contralayout.R;
import com.xujun.contralayout.UI.ListFragment;
import com.xujun.contralayout.base.BaseFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerParallax2 extends FragmentActivity {

    ViewPager mViewPager;
    List<Fragment> mFragments;
    Toolbar mToolbar;

    private AppBarLayout mAppBarLayout;
    private View mTitle;
    private static final String TAG = "ViewPagerParallax2";

    String[] mTitles = new String[]{
            "主页", "微博", "相册"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four2);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.mainappbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTitle =  findViewById(R.id.rl_title);
        mTitle.setVisibility(View.GONE);


        setupViewPager();

        AppBarStateChangeListener listener = new AppBarStateChangeListener();
        listener.setOnStateChangedListener(new AppBarStateChangeListener.OnStateChangedListener() {
            @Override
            public void onExpanded() {
                Log.i(TAG, "onExpanded: =");
                mTitle.setVisibility(View.GONE);
            }

            @Override
            public void onCollapsed() {
                Log.i(TAG, "onCollapsed: =");
                mTitle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onInternediateFromExpand() {
                Log.i(TAG, "onInternediateFromExpand: =");
                mTitle.setVisibility(View.GONE);
            }

            @Override
            public void onInternediateFromCollapsed() {
                Log.i(TAG, "onInternediateFromCollapsed: =");
                mTitle.setVisibility(View.GONE);
            }

            @Override
            public void onInternediate() {

            }
        });
        mAppBarLayout.addOnOffsetChangedListener(listener);
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            ListFragment listFragment = ListFragment.newInstance(mTitles[i]);
            mFragments.add(listFragment);
        }
        BaseFragmentAdapter adapter =
                new BaseFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);


        viewPager.setAdapter(adapter);
    }
}
