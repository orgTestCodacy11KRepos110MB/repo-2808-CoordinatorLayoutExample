package com.xujun.contralayout.UI.viewPager;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.xujun.contralayout.R;
import com.xujun.contralayout.UI.ListFragment;
import com.xujun.contralayout.base.BaseFragmentAdapter;
import com.xujun.contralayout.base.WriteLogUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerNew extends AppCompatActivity {

    ViewPager mViewPager;
    List<Fragment> mFragments;
    AppBarLayout mAppBarLayout;
    View mView;

    private static final String TAG = "ViewPagerNew";

    String[] mTitles = new String[]{
            "主页", "微博", "相册"
    };

    private int mHeight;
    View ll_content;
    private int mHeightContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_new);
        mView = findViewById(R.id.view);
        ll_content = findViewById(R.id.ll_content);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = findViewById(R.id.headview).getHeight();
                mHeightContent = ll_content.getHeight() - findViewById(R.id.tabs).getHeight();

                WriteLogUtil.i(" mHeight=" + mHeight);
                WriteLogUtil.i(" mHeightContent=" + mHeightContent);
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int abs = Math.abs(verticalOffset);
            }
        });


        setupViewPager();
    }


    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

       /* TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tab2);
        tabLayout2.setupWithViewPager(viewPager);*/
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
