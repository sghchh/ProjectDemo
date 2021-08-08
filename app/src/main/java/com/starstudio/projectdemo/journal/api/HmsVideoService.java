package com.starstudio.projectdemo.journal.api;

import android.content.Context;
import android.view.View;

import com.huawei.hms.videoeditor.ui.api.MediaApplication;
import com.huawei.hms.videoeditor.ui.api.MediaExportCallBack;
import com.huawei.hms.videoeditor.ui.api.MediaInfo;
import com.huawei.hms.videoeditor.ui.api.VideoEditorLaunchOption;

import static com.huawei.hms.videoeditor.ui.api.MediaApplication.START_MODE_IMPORT_FROM_MEDIA;

/**
 * created by sgh
 * 2021-8-8
 * 简单封装Hms的视频服务
 */
public class HmsVideoService {
    public static void init() {
        MediaApplication.getInstance().setApiKey("CgB6e3x9c2tIlXQZdvRg9VeCfngxvAwbW5FpKsYs/7eW39cdgYZ90pxu2gM85yEp+f2zCFSTXy4CebF3cdcULMzc");
        MediaApplication.getInstance().setLicenseId("License ID");


        MediaExportCallBack callBack = new MediaExportCallBack() {
                @Override
                public void onMediaExportSuccess(MediaInfo mediaInfo) {
                    // 导出成功
                    String mediaPath = mediaInfo.getMediaPath();
                }
                @Override
                public void onMediaExportFailed(int errorCode) {
                    // 导出失败
                }
        };
        MediaApplication.getInstance().setOnMediaExportCallBack(callBack);

    }
    public static void start(Context context, String video) {
        VideoEditorLaunchOption option = new VideoEditorLaunchOption.Builder().setStartMode(START_MODE_IMPORT_FROM_MEDIA).build();
        MediaApplication.getInstance().launchEditorActivity(context,option);
    }

}
