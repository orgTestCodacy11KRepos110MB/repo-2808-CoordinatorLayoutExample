package com.xujun.contralayout.UI.zhihu;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xujun.contralayout.R;
import com.xujun.contralayout.UI.ItemFragement;
import com.xujun.contralayout.adapter.ZhiHuAdapter;

import java.util.ArrayList;
import java.util.List;

public class ZhiHuHomeActivity extends AppCompatActivity {

    FrameLayout mFl;
    RadioGroup mRg;

    private AppBarLayout mAppBarLayout;

    public static final String TAG = "xujun";

    private int currentTab = 0;

    public static final String[] mTiltles = new String[]{
            "home", "course", "direct", "me"
    };
    private List<Fragment> mFragments;
    private Fragment mCurFragment;
    private ZhiHuAdapter mZhiHuAdapter;

    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_home);
        initView();
        initEvent();
        initHeaderAndFooter();
    }

    private void initHeaderAndFooter() {
        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                mHeight =   mAppBarLayout.getHeight();
            }
        });
    }


    private void initEvent() {
        ((RadioButton) mRg.getChildAt(currentTab)).setChecked(true);
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTiltles.length; i++) {
            if(i==0){
                HomeFragment homeFragment = new HomeFragment();
                mFragments.add(homeFragment);
            }else{
                ItemFragement itemFragement = ItemFragement.newInstance(mTiltles[i]);
                mFragments.add(itemFragement);
            }

        }
        mCurFragment = mFragments.get(currentTab);


        mZhiHuAdapter = new ZhiHuAdapter(this, mFragments, R.id.fl);
        mRg.setOnCheckedChangeListener(mZhiHuAdapter);
        mZhiHuAdapter.setFragmentToogleListener(new ZhiHuAdapter.FragmentToogleListener() {
            @Override
            public void onToogleChange(Fragment fragment, int currentTab) {
                if (currentTab == 0) {
                    ViewGroup.LayoutParams layoutParams = mAppBarLayout.getLayoutParams();
                    layoutParams.height=mHeight;
                    mAppBarLayout.setLayoutParams(layoutParams);
                    mAppBarLayout.setVisibility(View.VISIBLE);

                } else {
                    ViewGroup.LayoutParams layoutParams = mAppBarLayout.getLayoutParams();
                    layoutParams.height=0;
                    mAppBarLayout.setLayoutParams(layoutParams);
                   mAppBarLayout.setVisibility(View.GONE);
                }
                Log.i(TAG, "onToogleChange: " + currentTab);
            }
        });
        replace(mCurFragment);




    }

    private void initView() {
        mFl = (FrameLayout) findViewById(R.id.fl);
        mRg = (RadioGroup) findViewById(R.id.rg);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
    }

    public void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (isBottomHide()) {
            int top = mAppBarLayout.getTop();
            int bottom = mAppBarLayout.getBottom();
            Log.i(TAG, "onBackPressed: bottom=" + bottom);
            Log.i(TAG, "onBackPressed: bottom=" + bottom);
            //            AnimatorUtil.show(mAppBarLayout,top,0);
            super.onBackPressed();

        } else {
            super.onBackPressed();
        }


    }

    public boolean isBottomHide() {
        //        这里mRg的TranslationY之所以会改变，是因为我们改变了他的值
        float translationY = mRg.getTranslationY();

        return translationY > 0;

    }
}
