
package com.xj.qqbroswer.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xj.qqbroswer.BuildConfig;
import com.xj.qqbroswer.DemoApplication;
import com.xj.qqbroswer.R;

import java.lang.ref.WeakReference;

/**
 * 主要是通过 transY 来控制位置的
 */
public class SearchBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "SearchBehavior";

    private WeakReference<View> dependentView;
    private long mLastTime;
    private boolean first = true;

    public SearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child,
                                   View dependency) {
        if (dependency != null && dependency.getId() == R.id.id_uc_news_header_pager) {
            dependentView = new WeakReference<>(dependency);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        float dependencyTranslationY = dependency.getTranslationY();
        Log.i(TAG, "onDependentViewChanged: dependencyTranslationY=" + dependencyTranslationY);
        int translationY = (int) (getSearchViewMarginTop() + dependencyTranslationY);
        Log.i(TAG, "onDependentViewChanged: translationY=" + translationY);
        child.setTranslationY(translationY >= 0 ? translationY : 0);

        return false;
    }

    private View getDependentView() {
        return dependentView.get();
    }

    private float getSearchViewMarginTop() {
        return 200;
    }


    private int getMaxTranslationY() {
        return (-(int) (DemoApplication.getAppContext().getResources()
                .getDimension(R.dimen.search_margin_top) + 0.5f));
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        int headerOffsetRange = getHeaderOffsetRange();
        int titleOffsetRange = getSearchOffest();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "offsetChildAsNeeded:" + dependency.getTranslationY());
        }
        if (dependency.getTranslationY() == headerOffsetRange) {
            child.setTranslationY(titleOffsetRange);
        } else if (dependency.getTranslationY() == 0) {
            child.setTranslationY(0);
        } else {
            child.setTranslationY((int) (dependency.getTranslationY() / (headerOffsetRange * 1.0f) * titleOffsetRange));
        }

    }

    private int getHeaderOffsetRange() {
        return DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.header_pager_offset);
    }

    private int getSearchOffest() {
        return -DemoApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.search_margin_top);
    }

}
