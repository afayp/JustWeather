package com.pfh.app.justweather;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baoyz.swipemenulistview.SwipeMenuListView;

public class CustomSwipeMenuListView extends SwipeMenuListView {
    public CustomSwipeMenuListView(Context context) {
        super(context);
    }

    public CustomSwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomSwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
