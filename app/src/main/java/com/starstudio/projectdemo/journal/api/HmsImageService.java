package com.starstudio.projectdemo.journal.api;

import android.app.Activity;
import android.content.Context;

import com.huawei.hms.image.vision.*;
import com.huawei.hms.image.vision.bean.ImageVisionResult;

import java.util.HashMap;

public class HmsImageService {
    private volatile static HmsImageService INSTANCE;
    private HashMap type2Code = new HashMap();
    private String[] types = new String[]{"黑白", "棕调", "慵懒", "小苍兰",
            "富士", "桃粉", "海盐", "薄荷", "蒹葭", "复古", "棉花糖", "青苔", "日光",
            "雾霾蓝", "向日葵", "硬朗", "古铜黄", "黑白调", "黄绿调", "黄调", "绿调", "青调", "紫调"};

    private ImageVisionImpl imageVision;
    private ImageVision.VisionCallBack callBack;

    private HmsImageService(Context context) {
        this.imageVision = ImageVision.getInstance(context);
        this.callBack = new ImageVision.VisionCallBack() {
            @Override
            public void onSuccess(int i) {
                imageVision.init(context, JsonObjects.AuthJson.getInstance());
            }

            @Override
            public void onFailure(int i) {

            }
        };

        for(int i = 0; i < types.length; i ++)
            type2Code.put(types[i], i + 1);
    }

    public static synchronized HmsImageService getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HmsImageService.class) {
                INSTANCE = new HmsImageService(context);
            }
        }
        return INSTANCE;
    }

    public int type2Code(String type) {
        return (int)type2Code.get(type);
    }
}
