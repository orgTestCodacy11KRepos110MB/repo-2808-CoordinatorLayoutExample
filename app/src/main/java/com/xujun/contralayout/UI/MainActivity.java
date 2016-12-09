package com.xujun.contralayout.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xujun.contralayout.R;
import com.xujun.contralayout.UI.FloatingActiobButtton.FloatingActionButtonActivity;
import com.xujun.contralayout.UI.FloatingActiobButtton.HorizontalSample;
import com.xujun.contralayout.UI.zhihu.ZhiHuActivity;
import com.xujun.contralayout.UI.zhihu.ZhiHuHomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recycler_snap:
                jump(ToolBarSampleSnar.class);
                break;

            case R.id.btn_toolBar:
                jump(ToolBarSample.class);
                break;

            case R.id.btn_viewPager:
                jump(ViewPagerSample.class);
                break;

            case R.id.btn_parallax:
                jump(ViewPagerParallax.class);
                break;

            case R.id.btn_parallax_snap:
                jump(ViewPagerParallaxSnap.class);
                break;

            case R.id.btn_floatingAction:
                jump(FloatingActionButtonActivity.class);
                break;

            case R.id.btn_floatingAction_horizontal:
                jump(HorizontalSample.class);
                break;

            case R.id.btn_zhihu:
                jump(ZhiHuActivity.class);
                break;

            case R.id.btn_zhihu_home:
                jump(ZhiHuHomeActivity.class);
                break;

            case R.id.btn_jianshu:
                jump(JianShuActivity.class);
                break;

            default:
                break;
        }
    }

    public void jump(Class<? extends Activity> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}
