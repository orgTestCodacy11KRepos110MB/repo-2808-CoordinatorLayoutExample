package com.xujun.contralayout.UI.toolBar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.xujun.contralayout.R;
import com.xujun.contralayout.adapter.ItemAdapter;
import com.xujun.contralayout.recyclerView.divider.DividerItemDecoration;
import com.xujun.contralayout.utils.WriteLogUtil;

import java.util.ArrayList;
import java.util.List;

public class ToolBarSampleSnar extends AppCompatActivity {

    public static final String TAG = "xujun";

    private RecyclerView mRecyclerView;
    private RelativeLayout mRlBottomSheet;
    private Toolbar mToolbar;

    private BottomSheetBehavior<RelativeLayout> mFrom;
    private List<String> mDatas;
    private ItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRlBottomSheet = (RelativeLayout) findViewById(R.id.rl_bottom_sheet);
        mFrom = BottomSheetBehavior.from(mRlBottomSheet);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // 该属性必须在setSupportActionBar之前 调用
        mToolbar.setTitle("ToolBarSample");
        setSupportActionBar(mToolbar);

        initListener();
        initData();
    }

    private void initData() {
        WriteLogUtil.init(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String s = String.format("我是第%d个item", i);
            mDatas.add(s);
        }
        mAdapter = new ItemAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {

        mFrom.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i(TAG, "onStateChanged: newState=" + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i(TAG, "onStateChanged: slideOffset=" + slideOffset);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: dy=" + dy);
            }
        });
    }


}
