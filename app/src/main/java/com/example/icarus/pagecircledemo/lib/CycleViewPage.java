package com.example.icarus.pagecircledemo.lib;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.icarus.pagecircledemo.R;
import com.example.icarus.pagecircledemo.bean.ADInfo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by icarus9527 on 2017/5/3.
 */
public class CycleViewPage extends Fragment implements ViewPager.OnPageChangeListener{

    private boolean isScrolling = false;
    private boolean isCycle = false;
    private boolean isWheel = false;
    private int time = 5000;//默认轮播时间
    private int currentPosition = 0;//轮播当前位置
    private long releaseTime = 0;//手指松开、页面不滚动时间，防止手指松开后立刻进行切换
    private List<ImageView> imageViews = new ArrayList<ImageView>();
    private ImageView[] indicators;
    private FrameLayout viewPagerFragmentLayout;
    private LinearLayout indicatorLayout;//指示器
    private BaseViewPager viewPager;
    private BaseViewPager parentViewPager;
    private ViewPagerAdapter adapter;
    private CycleViewPagerHandler handler;
    private int WHEEL = 100;//切换
    private int WHEEL_WAIT = 101;//等待
    private ImageCycleViewListener mImageCycleViewListener;
    private List<ADInfo> infos;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.view_cycle_viewpager_content,null);

        viewPager = (BaseViewPager) view.findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) view.findViewById(R.id.layout_viewpager_indicator);
        viewPagerFragmentLayout = (FrameLayout) view.findViewById(R.id.layout_viewager_content);

        handler = new CycleViewPagerHandler(getActivity()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //传递过来的为“切换”同时图片数不为零
                if (msg.what == WHEEL && imageViews.size() != 0){
                    //没有在滚动状态时
                    if (!isScrolling){
                        int max = imageViews.size()+1;
                        int position = (currentPosition + 1) % imageViews.size();
                        viewPager.setCurrentItem(position,true);
                        if (position == max){
                            viewPager.setCurrentItem(1,false);
                        }
                    }

                    releaseTime = System.currentTimeMillis();
                    //删除指定的Runnable对象，使线程对象停止运行
                    handler.removeCallbacks(runnable);
                    //延迟多少毫秒后开始运行
                    handler.postDelayed(runnable, time);
                    return;

                }

                if (msg.what == WHEEL_WAIT && imageViews.size()!=0){
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, time);
                }
            }
        };

        handler.sendEmptyMessage(WHEEL);

        return view;
    }

    //每过一定时间，执行一次判断，判断当前条件是否可以进行切换
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && !getActivity().isFinishing() && isWheel){
                long now = System.currentTimeMillis();
                //检测上一次晃动时间与本次之间是否有触击（手滑动）操作，有的话等待下次轮播
                if (now - releaseTime>time - 500){
                    handler.sendEmptyMessage(WHEEL);
                }else{
                    handler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }
    };

    //是否循环，默认不开启，开启前，请将views的最前面与最后面各加入一个视图，用于循环
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }
    //判断是否处于循环状态
    public boolean isCycle(){
        return isCycle;
    }
    //设置是否轮播，默认不轮播，轮播一定是循环的
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
    }
    //判断是否处于轮播状态
    public boolean isWheel(){
        return isWheel;
    }
    //设置轮播暂停时间，即每过多少秒切换到下一张图片，默认5000ms
    public void setTime(int time) {
        this.time = time;
    }

    //设置指示器居中，默认在右方
    public void setIndicatorCenter() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicatorLayout.setLayoutParams(params);
    }


    //设置数据
    public void setData(List<ImageView> views, List<ADInfo> infos, ImageCycleViewListener mAdCycleViewListener) {
        setData(views, infos, mAdCycleViewListener, 0);
    }

    private void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener mAdCycleViewListener, int showPosition) {
        mImageCycleViewListener = mAdCycleViewListener;
        infos = list;
        this.imageViews.clear();

        //如果没有添加view，fragment就会隐藏，并且不会占用屏幕空间
        if (views.size() == 0){
            viewPagerFragmentLayout.setVisibility(View.GONE);
            return;
        }

        for(ImageView item : views){
            this.imageViews.add(item);
        }

        int ivSize = views.size();

        //设置指示器
        indicators = new ImageView[ivSize];
        if (isCycle){
            indicators = new ImageView[ivSize - 2];
            indicatorLayout.removeAllViews();
        }
        for (int i = 0;i<indicators.length; i++){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_cycle_viewpager_indicator,null);
            indicators[i] = (ImageView) view.findViewById(R.id.image_indicator);
            indicatorLayout.addView(view);
        }

        adapter = new ViewPagerAdapter();

        //默认指向第一项，下方viewpager.setCurrentItem将出发重新计算指示器指向
        setIndicator(0);

        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        if (showPosition<0 || showPosition>= views.size()){
            showPosition = 0;
        }
        if (isCycle){
            showPosition = showPosition + 1;
        }

        viewPager.setCurrentItem(showPosition);
    }

    /**
     * 设置指示器
     *
     * @param selectedPosition
     *            默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i]
                    .setBackgroundResource(R.drawable.icon_point);
        }
        if (indicators.length > selectedPosition)
            indicators[selectedPosition]
                    .setBackgroundResource(R.drawable.icon_point_pre);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int max = imageViews.size()-1;
        currentPosition = position;
        if (isCycle){
            if (position == 0){
                currentPosition = max - 1;

            }else if (position == max){
                currentPosition = 1;
            }
            viewPager.setCurrentItem(currentPosition);
            position = currentPosition-1;
        }

        setIndicator(position);

        Log.i("CycleViewPage-Selected",position+"");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置viewpager是否可以滚动
     *
     * @param enable
     */
    public void setScrollable(boolean enable) {
        viewPager.setScrollable(enable);
    }

    /**
     * 返回当前位置,循环时需要注意返回的position包含之前在views最前方与最后方加入的视图，即当前页面试图在views集合的位置
     *
     * @return
     */
    public int getCurrentPostion() {
        return currentPosition;
    }

    /**
     * 如果当前页面嵌套在另一个viewPager中，为了在进行滚动时阻断父ViewPager滚动，可以 阻止父ViewPager滑动事件
     * 父ViewPager需要实现ParentViewPager中的setScrollable方法
     */
    public void disableParentViewPagerTouchEvent(BaseViewPager parentViewPager) {
        if (parentViewPager != null)
            parentViewPager.setScrollable(false);
    }



    private class ViewPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView v = imageViews.get(position);
            if (mImageCycleViewListener != null){
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageCycleViewListener.onImageClick(infos.get(currentPosition - 1),currentPosition,v);
                    }
                });
            }
            container.addView(v);
            return v;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    public void refreshData() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 释放指示器高度，可能由于之前指示器被限制了高度，此处释放
     */
    public void releaseHeight() {
        getView().getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
        refreshData();
    }

    /**
     * 隐藏CycleViewPager
     */
    public void hide() {
        viewPagerFragmentLayout.setVisibility(View.GONE);
    }

    /**
     * 返回内置的viewpager
     *
     * @return viewPager
     */
    public BaseViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {

        /**
         * 单击图片事件
         */
        public void onImageClick(ADInfo info, int postion, View imageView);
    }
}
