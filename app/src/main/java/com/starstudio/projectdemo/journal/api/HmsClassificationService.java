package com.starstudio.projectdemo.journal.api;

import android.graphics.Bitmap;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.classification.MLImageClassification;
import com.huawei.hms.mlsdk.classification.MLImageClassificationAnalyzer;
import com.huawei.hms.mlsdk.classification.MLLocalClassificationAnalyzerSetting;
import com.huawei.hms.mlsdk.classification.MLRemoteClassificationAnalyzerSetting;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.common.MLFrame;

import java.io.IOException;
import java.util.List;

public class HmsClassificationService {
    private static final String TAG = "classification";
    private static MLImageClassificationAnalyzer analyzer;
    public static void init() {
        MLRemoteClassificationAnalyzerSetting setting =
                new MLRemoteClassificationAnalyzerSetting.Factory()
                        .setMinAcceptablePossibility(0.8f)
                        .setLargestNumOfReturns(1)
                        .create();
        analyzer = MLAnalyzerFactory.getInstance().getRemoteImageClassificationAnalyzer(setting);
        //analyzer = MLAnalyzerFactory.getInstance().getLocalImageClassificationAnalyzer();
    }

    public static void classify(Bitmap bitmap) {
        Log.e(TAG, "classify: 调用了识别方法+++++++++++++++++++++++");
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        Task<List<MLImageClassification>> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<List<MLImageClassification>>() {
            @Override
            public void onSuccess(List<MLImageClassification> classifications) {
                // 识别成功。
                Log.e(TAG, "onSuccess: 识别成功----------------");
                for (int i = 0; i < classifications.size(); i ++) {
                    Log.e(TAG, "----------识别的结果是"+classifications.get(i).getClassificationIdentity());
                    Log.e(TAG, "----------识别的结果是"+classifications.get(i).getName());
                }
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
                Log.e(TAG, "识别错误=============================");
                // 识别失败。
                // Recognition failure.
                try {
                    MLException mlException = (MLException)e;
                    // 获取错误码，开发者可以对错误码进行处理，根据错误码进行差异化的页面提示。
                    int errorCode = mlException.getErrCode();
                    // 获取报错信息，开发者可以结合错误码，快速定位问题。
                    String errorMessage = mlException.getMessage();
                } catch (Exception error) {
                    // 转换错误处理。
                }
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
