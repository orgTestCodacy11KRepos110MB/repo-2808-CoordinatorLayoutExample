package com.xj.qqbroswer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xj.qqbroswer.meituan.MeituanSampleActivity;
import com.xj.qqbroswer.overscroll.OverScroollSample;
import com.xj.qqbroswer.recycler.RecyclerSampleActivity;

public class MainActivity extends AppCompatActivity {

    private Button mBtnBrowser;
    private Button mBtnChat;
    private FloatingActionButton mFabNewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_browser:
                startActivity(new Intent(this, QQBrowserDemoActivity.class));
                break;
            case R.id.btn_chat:
                startActivity(new Intent(this, MeituanSampleActivity.class));
                break;
            case R.id.btn_over_scroll:
                startActivity(new Intent(this, OverScroollSample.class));
                break;

            case R.id.btn_test:
                startActivity(new Intent(this, GroupActivity.class));
                break;


            case R.id.btn_recycler:
                startActivity(new Intent(this, RecyclerSampleActivity.class));
                break;
            default:
                break;
        }
    }

    private void initView() {
        mBtnBrowser = findViewById(R.id.btn_browser);
        mBtnChat = findViewById(R.id.btn_chat);
        mFabNewChat = findViewById(R.id.fab_new_chat);
        mFabNewChat.setBackgroundColor(Color.RED);
        ColorStateList stateList = getResources().getColorStateList(R.color.button_text);
        mFabNewChat.setBackgroundTintList(stateList);
    }
}
