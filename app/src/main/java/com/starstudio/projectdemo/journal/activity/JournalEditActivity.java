package com.starstudio.projectdemo.journal.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.CaptureClient;
import com.huawei.hms.kit.awareness.capture.CapabilityResponse;
import com.huawei.hms.kit.awareness.capture.LocationResponse;
import com.huawei.hms.kit.awareness.capture.WeatherStatusResponse;
import com.huawei.hms.kit.awareness.status.WeatherStatus;
import com.huawei.hms.kit.awareness.status.weather.Situation;
import com.huawei.hms.kit.awareness.status.weather.WeatherSituation;
import com.starstudio.projectdemo.databinding.Activity2JournalEditBinding;
import com.starstudio.projectdemo.journal.api.HmsImageService;

import java.util.ArrayList;

public class JournalEditActivity extends AppCompatActivity {
    Activity2JournalEditBinding binding;
    public ArrayList<String> picturePaths = new ArrayList<>();
    public int currentPostion = -1;
    CaptureClient client;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HmsImageService.init(this);
        client = Awareness.getCaptureClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Awareness.getCaptureClient(this).getWeatherByDevice()
                    // 执行成功的回调监听
                    .addOnSuccessListener(new OnSuccessListener<WeatherStatusResponse>() {
                        @Override
                        public void onSuccess(WeatherStatusResponse weatherStatusResponse) {
                            WeatherStatus weatherStatus = weatherStatusResponse.getWeatherStatus();
                            WeatherSituation weatherSituation = weatherStatus.getWeatherSituation();
                            Situation situation = weatherSituation.getSituation();
                            // 更多返回的天气数据信息可参见华为开发者文档
                            String weatherInfoStr = "City:" + weatherSituation.getCity().getName() + "\n" +
                                    "Weather id is " + situation.getWeatherId() + "\n" +
                                    "CN Weather id is " + situation.getCnWeatherId() + "\n" +
                                    "Temperature is " + situation.getTemperatureC() + "℃" +
                                    "," + situation.getTemperatureF() + "℉" + "\n" +
                                    "Wind speed is " + situation.getWindSpeed() + "km/h" + "\n" +
                                    "Wind direction is " + situation.getWindDir() + "\n" +
                                    "Humidity is " + situation.getHumidity() + "%";
                            Log.i("获取天气的结果", weatherInfoStr);
                        }
                    })
                    // 执行失败的回调监听
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e("获取天气的结果", "get weather failed",e);
                        }
                    });
        }



        binding = Activity2JournalEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
