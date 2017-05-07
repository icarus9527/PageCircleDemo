package com.example.icarus.pagecircledemo.lib;

import android.content.Context;
import android.os.Handler;


import java.util.logging.LogRecord;

/**
 * Created by icarus9527 on 2017/5/4.
 */
public class CycleViewPagerHandler extends Handler {

    Context context;

    public CycleViewPagerHandler(Context context) {
        this.context = context;
    }
}
