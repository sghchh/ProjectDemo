package com.starstudio.projectdemo;

import android.app.Application;

import com.starstudio.projectdemo.journal.api.HmsImageService;
import com.starstudio.projectdemo.utils.FileUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 进行文件操作
        FileUtil.init(this);
        HmsImageService.init(this);
    }
}
