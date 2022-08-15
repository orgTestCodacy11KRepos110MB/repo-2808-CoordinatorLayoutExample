package com.xj.qqbroswer.meituan;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xj.behavior.ContentBehavior;
import com.xj.behavior.HeaderBehavior;
import com.xj.behavior.base.HeaderFlingRunnable;
import com.xj.qqbroswer.DemoApplication;
import com.xj.qqbroswer.R;
import com.xj.qqbroswer.adapter.RecyclerViewAdapter;
import com.xj.qqbroswer.utils.StatusBarUtils;
import com.xj.qqbroswer.utils.ViewUtils;

import java.util.ArrayList;

public class MeituanSampleActivity extends AppCompatActivity {

    private static final String TAG = "ChatSampleActivity";

    private FrameLayout mIdUcNewsHeaderPager;
    private RecyclerView mRecyclerView;
    private HeaderBehavior mHeaderBehavior;
    private View mRlTitle;
    private float mTotal;
    private ContentBehavior mContentBehavior;
    private int mStatusbarHeight;
    private ImageView mIv;
    private LinearLayout mBehaviorContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.translucent(this, true);
        setContentView(R.layout.activity_mei_tuan_sample);
        initView();
        mTotal = getResources().getDimension(R.dimen.header_height);
        initData();
        initListener();
    }

    private void initListener() {
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MeituanSampleActivity.this, " 点击头像", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mIdUcNewsHeaderPager = findViewById(R.id.id_uc_news_header_pager);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRlTitle = findViewById(R.id.rl_title);

        mIv = findViewById(R.id.iv);
        mBehaviorContent = findViewById(R.id.behavior_content);
    }

    private void initData() {
        String key = "chat";
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            res.add(key + ":Fragment item :" + i);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(res);
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(recyclerViewAdapter);

        mStatusbarHeight = StatusBarUtils.getStatusbarHeight(this);
        mRlTitle.setBackgroundColor(Color.TRANSPARENT);
        ViewUtils.setViewMargin(mRlTitle, new Rect(0, mStatusbarHeight, 0, 0));

        initBehavior();

    }

    private void initBehavior() {
        mHeaderBehavior = (HeaderBehavior)
                ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager)
                        .getLayoutParams()).getBehavior();
        Resources resources = DemoApplication.getAppContext().getResources();
        mContentBehavior = (ContentBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.behavior_content).getLayoutParams()).getBehavior();
        mHeaderBehavior.setHeaderOffsetRange(-(int) resources.getDimension(R.dimen.header_height));
        mHeaderBehavior.setCouldScroollOpen(true);
        mContentBehavior.setDependsLayoutId(R.id.id_uc_news_header_pager);
        mContentBehavior.setFinalY((int) (resources.getDimension(R.dimen.header_title_height) + mStatusbarHeight));

        mHeaderBehavior.setPagerStateListener(new HeaderBehavior.OnPagerStateListener() {
            @Override
            public void onPagerClosed() {
                Log.i(TAG, "onPagerClosed: onPagerClosed =");
                Toast.makeText(MeituanSampleActivity.this, "close", Toast.LENGTH_SHORT).show();
//                mRlTitle.setBackgroundColor(Color.WHITE);

            }

            @Override
            public void onScrollChange(boolean isUp, int dy, int type) {
                Log.i(TAG, "onScroollChange: isUp=" + isUp + " dy = " + dy + " type = " + type);
                if (!isUp) {
                    mRlTitle.setBackgroundColor(Color.TRANSPARENT);
                }

            }


            @Override
            public void onPagerOpened() {
                Log.i(TAG, "onPagerClosed: onPagerOpened =");
                Toast.makeText(MeituanSampleActivity.this, "open", Toast.LENGTH_LONG).show();
                mRlTitle.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        mHeaderBehavior.setOnHeaderFlingListener(new HeaderFlingRunnable.OnHeaderFlingListener() {
            @Override
            public void onFlingFinish() {

            }

            @Override
            public void onFlingStart(View child, View target, float velocityX, float velocityY) {
                Log.i(TAG, "onFlingStart: velocityY =" + velocityY);
                if (velocityY < 0) {
                    mRecyclerView.smoothScrollBy(0, (int) Math.abs(velocityY), new AccelerateDecelerateInterpolator());
                }

            }

            @Override
            public void onHeaderClose() {

            }

            @Override
            public void onHeaderOpen() {

            }
        });
    }
}
