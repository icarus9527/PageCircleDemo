package com.example.icarus.pagecircledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icarus.pagecircledemo.bean.ADInfo;
import com.example.icarus.pagecircledemo.lib.CycleViewPage;
import com.example.icarus.pagecircledemo.util.ViewFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPage cycleViewPage;

    private int[] imageRes = {R.drawable.ad01,R.drawable.ad02,R.drawable.ad03,R.drawable.ad04};

    private CycleViewPage.ImageCycleViewListener mAdCycleViewListener = new CycleViewPage.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPage.isCycle()) {
                position = position - 1;
                Toast.makeText(MainActivity.this,
                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        configImageLoader();
        initialize();
    }

    private void initialize(){
        cycleViewPage = (CycleViewPage) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

        for(int i = 0;i<imageRes.length; i++){
            ADInfo info = new ADInfo();
            info.setRes(imageRes[i]);
            info.setContent("this is the NO."+i+" picture");
            infos.add(info);
        }

        //将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this,infos.get(infos.size() - 1).getRes()));
        for(int i = 0; i<infos.size(); i++){
            views.add(ViewFactory.getImageView(this, infos.get(i).getRes()));
        }

        //将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getRes()));


        cycleViewPage.setCycle(true);

        cycleViewPage.setWheel(true);

        cycleViewPage.setTime(2000);

        cycleViewPage.setData(views, infos, mAdCycleViewListener);

        cycleViewPage.setIndicatorCenter();
    }



    private void configImageLoader(){
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


}
