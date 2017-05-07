package com.example.icarus.pagecircledemo.lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by icarus9527 on 2017/5/4.
 */

public class BaseViewPager extends ViewPager {

    private boolean scrollable = true;

    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    //设置ViewPager是否可以滚动
    public void setScrollable(boolean enable){
        scrollable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollable){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }

    }
}
