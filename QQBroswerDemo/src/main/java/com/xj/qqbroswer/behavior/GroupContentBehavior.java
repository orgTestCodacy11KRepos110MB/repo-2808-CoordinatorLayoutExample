package com.xj.qqbroswer.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xj.behavior.base.HeaderScrollingViewBehavior;

import java.util.List;

/**
 * Created by jun xu on 19-3-5
 */
public class GroupContentBehavior extends HeaderScrollingViewBehavior {

    private static final String TAG = "GroupContentBehavior";
    private int mDependsLayoutId;
    private int mFinalY;
    private int mHeaderOffsetRange;

    public void setDependsLayoutId(int dependsLayoutId) {
        mDependsLayoutId = dependsLayoutId;
    }

    public GroupContentBehavior() {
    }

    public GroupContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean dependOn = isDependOn(dependency);
        Log.i(TAG, "layoutDependsOn: dependOn =" + dependOn);
        return dependOn;
    }

    public void setFinalY(int finalY) {
        mFinalY = finalY;
    }

    public void setHeaderOffsetRange(int headerOffsetRange) {
        mHeaderOffsetRange = headerOffsetRange;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        /* int translationY = (int) (denpendencyTranslationY / (getHeaderOffsetRange() * 1.0f)
         * getScrollRange(dependency));*/
        float denpendencyTranslationY = dependency.getTranslationY();
        Log.d(TAG, "offsetChildAsNeeded: denpendencyTranslationY=" + denpendencyTranslationY
                + " denpendencyTranslationY=" + denpendencyTranslationY);
        //        child.setTranslationY(translationY);

        //  is a negative number
        int maxTranslationY = -(dependency.getHeight() - getFinalY());
        if (denpendencyTranslationY < maxTranslationY) {
            denpendencyTranslationY = maxTranslationY;
        }

        child.setTranslationY((int) (denpendencyTranslationY));
    }

    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            int finalY = getFinalY();
            Log.i(TAG, "getScrollRange: finalY =" + finalY);
            return Math.max(0, v.getMeasuredHeight() - finalY);
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getFinalY() {
        return mFinalY;
    }

    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == mDependsLayoutId;
    }
}
