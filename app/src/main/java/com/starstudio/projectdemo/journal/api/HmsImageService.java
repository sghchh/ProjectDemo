package com.starstudio.projectdemo.journal.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huawei.hms.image.vision.*;
import com.huawei.hms.image.vision.bean.ImageVisionResult;
import com.starstudio.projectdemo.journal.data.HMSImageServiceJson;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * created by sgh
 * 简单封装HMS中滤镜API
 */
public class HmsImageService {
    private volatile static HmsImageService INSTANCE;
    private final Map<String, Integer> type2Code = new HashMap();
    private final String[] types = new String[]{"黑白", "棕调", "慵懒", "小苍兰",
            "富士", "桃粉", "海盐", "薄荷", "蒹葭", "复古", "棉花糖", "青苔", "日光",
            "雾霾蓝", "向日葵", "硬朗", "古铜黄", "黑白调", "黄绿调", "黄调", "绿调", "青调", "紫调"};

    private final ImageVisionImpl imageVision;

    private HmsImageService(Context context) {
        this.imageVision = ImageVision.getInstance(context);
        this.imageVision.setVisionCallBack(new ImageVision.VisionCallBack() {
            @Override
            public void onSuccess(int i) {
                String authJson = new Gson().toJson(HMSImageServiceJson.AuthJson.getInstance());
                int code = 0;
                try {
                    code = imageVision.init(context, new JSONObject(authJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code != 0)
                    Toast.makeText(context, "滤镜服务初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i) {
                Log.d("hms", "onFailure:hms滤镜服务初始化结果 "+i);
            }
        });
        // 初始化滤镜服务的code与type对应表
        for(int i = 0; i < types.length; i ++)
            type2Code.put(types[i], i + 1);
    }

    public static synchronized void init(Context context) {
        if (INSTANCE == null) {
            synchronized (HmsImageService.class) {
                INSTANCE = new HmsImageService(context);
            }
        }
    }

    public static HmsImageService getInstance() {
        if (INSTANCE == null)
            throw new NullPointerException("请先调用init(Context)方法");
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
    public ImageVisionResult getFilterResult(String filterType, String imgPath) throws JSONException {
        Bitmap initBitmap = BitmapFactory.decodeFile(imgPath);

        HMSImageServiceJson.TaskJson taskJson = new HMSImageServiceJson.TaskJson();
        int typeCode = type2Code.getOrDefault(filterType, 0);
        taskJson.setFilterType(typeCode);
        HMSImageServiceJson.RequestJson requestJson = new HMSImageServiceJson.RequestJson(typeCode + "", taskJson);
        String jsonString = OtherUtil.decodeObject(requestJson);
        Log.e("HmsImageService", "getFilterResult: 请求参数为"+jsonString);
        return imageVision.getColorFilter(new JSONObject(jsonString), initBitmap);
    }
}
