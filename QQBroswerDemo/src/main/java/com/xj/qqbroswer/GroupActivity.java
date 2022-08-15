package com.xj.qqbroswer;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.xj.qqbroswer.adapter.RecyclerViewAdapter;
import com.xj.qqbroswer.behavior.GroupContentBehavior;
import com.xj.qqbroswer.behavior.GroupHeaderBehavior;
import com.xj.qqbroswer.utils.StatusBarUtils;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {


    private static final String TAG = "GroupActivity";

    private FrameLayout mIdUcNewsHeaderPager;
    private RecyclerView mRecyclerView;
    private GroupHeaderBehavior mHeaderBehavior;
    private View mRlTitle;
    private float mTotal;
    private GroupContentBehavior mContentBehavior;
    private int mStatusbarHeight;
    private ImageView mIv;
    private View mBehaviorContent;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.translucent(this, true);
        setContentView(R.layout.activity_group);
        initView();
        mTotal = getResources().getDimension(R.dimen.header_height);
        initData();
        initListener();
    }

    private void initListener() {
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupActivity.this, " 点击头像", Toast.LENGTH_SHORT).show();
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


        mHeaderBehavior = (GroupHeaderBehavior)
                ((CoordinatorLayout.LayoutParams) findViewById(R.id.id_uc_news_header_pager)
                        .getLayoutParams()).getBehavior();
        Resources resources = DemoApplication.getAppContext().getResources();


        mStatusbarHeight = StatusBarUtils.getStatusbarHeight(this);


        mContentBehavior = (GroupContentBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.behavior_content).getLayoutParams()).getBehavior();
        mHeaderBehavior.setHeaderOffsetRange(
                -(int) resources.getDimension(R.dimen.header_height));
        mContentBehavior.setDependsLayoutId(R.id.id_uc_news_header_pager);
        mContentBehavior.setFinalY((int) (resources.getDimension(R.dimen.header_title_height) + mStatusbarHeight));

        mRlTitle.setBackgroundColor(Color.TRANSPARENT);

        mHeaderBehavior.setPagerStateListener(new GroupHeaderBehavior.OnPagerStateListener() {
            @Override
            public void onPagerClosed() {
                Log.i(TAG, "onPagerClosed: onPagerClosed =");
                Toast.makeText(GroupActivity.this, "close", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GroupActivity.this, "open", Toast.LENGTH_LONG).show();
                mRlTitle.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdapter = new RecyclerViewAdapter(res);
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerViewAdapter.notifyDataSetChanged();
    }
}
