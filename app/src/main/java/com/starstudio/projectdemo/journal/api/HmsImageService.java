package com.starstudio.projectdemo.journal.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.huawei.hms.image.vision.*;
import com.huawei.hms.image.vision.bean.ImageVisionResult;
import com.starstudio.projectdemo.journal.data.HMSImageServiceJson;

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
                int code = imageVision.init(context, HMSImageServiceJson.AuthJson.getInstance());
                if (code != 0)
                    Toast.makeText(context, "滤镜服务初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i) {

            }
        };

        // 初始化滤镜服务的code与type对应表
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

    public String[] getTypes() {
        return types;
    }

    /**
     * HMS滤镜服务使用
     * @param filterType 滤镜服务的类别，可选方案参考types数组
     * @param imgPath 应用滤镜服务的图像的路径
     * @return HMS接口的ImageVisionResult
     */
    public ImageVisionResult getFilterResult(String filterType, String imgPath) {
        Bitmap initBitmap = BitmapFactory.decodeFile(imgPath);

        HMSImageServiceJson.TaskJson taskJson = new HMSImageServiceJson.TaskJson();
        int typeCode = (int)type2Code.get(filterType);
        HMSImageServiceJson.RequestJson requestJson = new HMSImageServiceJson.RequestJson(typeCode + "", taskJson);
        return imageVision.getColorFilter(requestJson, initBitmap);
    }
}
