package com.example.icarus.pagecircledemo.listener;

import android.view.View;

import com.example.icarus.pagecircledemo.bean.ADInfo;

/**
 * Created by icarus9527 on 2017/5/4.
 */
public interface ImageCycleViewListener {
    public void onImageClick(ADInfo info, int position, View imageView);
}
