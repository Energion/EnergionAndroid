package com.github.energion.energionandroid.utils;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;

/**
 * Created by Anatolii on 26.11.2016.
 */

public class CustomViewPager extends android.support.v4.view.ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v instanceof BarChart){
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

}