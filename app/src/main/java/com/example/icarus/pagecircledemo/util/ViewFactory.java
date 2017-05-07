package com.example.icarus.pagecircledemo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import com.example.icarus.pagecircledemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by icarus9527 on 2017/5/4.
 */
public class ViewFactory {



    //加载本地资源图片
    public static ImageView getImageView(Context context, int res) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.view_banner,null);
        imageView.setImageResource(res);
        return imageView;
    }
    /**
     * 获取ImageView视图的同时加载显示url
     *
     * @param context
     * @param url
     * @return
     * */
    //通过网络加载图片
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.view_banner,null);
        ImageLoader.getInstance().displayImage(url,imageView);

        return imageView;
    }
}
