package com.xj.behavior;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xj.behavior.base.HeaderScrollingViewBehavior;

import java.util.List;


public class ContentBehavior extends HeaderScrollingViewBehavior {

    private static final String TAG = "GroupContentBehavior";
    private int mDependsLayoutId;
    private int mFinalY;
    private int mHeaderOffsetRange;


    public ContentBehavior() {
    }

    public ContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }


    public void setDependsLayoutId(int dependsLayoutId) {
        mDependsLayoutId = dependsLayoutId;
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
        float denpendencyTranslationY = dependency.getTranslationY();

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
            return Math.max(0, v.getMeasuredHeight() - getFinalY());
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

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
