package com.xj.qqbroswer.recycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xj.qqbroswer.R;
import com.xj.qqbroswer.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class RecyclerSampleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sample);
        mRecyclerView = findViewById(R.id.recyclerView);
        initData();
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
    }
}
