package com.starstudio.projectdemo.journal.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.classification.MLImageClassification;
import com.huawei.hms.mlsdk.classification.MLImageClassificationAnalyzer;
import com.huawei.hms.mlsdk.classification.MLRemoteClassificationAnalyzerSetting;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * created by sgh
 * 2021-8-7
 * 进行图像分类任务
 */
public class HmsClassificationService {
    // 对相册识别分类
    public static final HashMap<String,String> CORE_CLASSIFICATION = new HashMap(){{
        put("Human", "人物");
        put("Plant", "植物");
        put("Wildlife", "动物");
        put("Building", "建筑");
        put("Food", "食物");
        put("Landscape", "风景");
        put("Screenshot", "截图");
        put("Street", "街道");
        put("Selfie", "自拍");
        put("Other", "其他");
    }};
    private static final String TAG = "classification";
    private static MLImageClassificationAnalyzer analyzer;
    public static void init() {
        MLRemoteClassificationAnalyzerSetting setting =
                new MLRemoteClassificationAnalyzerSetting.Factory()
                        .setMinAcceptablePossibility(0.8f)   // 设置置信度阈值
                        .setLargestNumOfReturns(1)    // 设置返回的标签个数
                        .create();
        analyzer = MLAnalyzerFactory.getInstance().getRemoteImageClassificationAnalyzer(setting);
    }

    public static void classify(JournalEditActivityData.PictureWithCategory pic) {
        Bitmap bitmap = BitmapFactory.decodeFile(pic.getPicturePath());
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        Task<List<MLImageClassification>> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<List<MLImageClassification>>() {
            @Override
            public void onSuccess(List<MLImageClassification> classifications) {
                // 识别成功。
                if (classifications.size() == 0)
                    pic.setCategory("其他");
                else {
                    String typeName = classifications.get(0).getName();
                    if (CORE_CLASSIFICATION.containsKey(typeName))
                        pic.setCategory(CORE_CLASSIFICATION.get(typeName));
                    else
                        pic.setCategory("其他");
                }
                Log.e(TAG, "onSuccess: 调用了识别方法，识别成功，分类为"+pic.getCategory());
                if (analyzer != null) {
                    try {
                        analyzer.stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                pic.setCategory("其他");
                try {
                    MLException mlException = (MLException)e;
                    int errorCode = mlException.getErrCode();
                    String errorMessage = mlException.getMessage();
                } catch (Exception error) {
                    // 转换错误处理。
                }
                Log.e(TAG, "onFailure: 调用了识别方法，识别失败，分类为"+pic.getCategory());
                if (analyzer != null) {
                    try {
                        analyzer.stop();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });


    }
}
