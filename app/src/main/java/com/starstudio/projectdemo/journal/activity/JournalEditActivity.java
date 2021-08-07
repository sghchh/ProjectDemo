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
import com.starstudio.projectdemo.journal.api.HmsWeatherService;

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
        HmsWeatherService.init(this);
        
        binding = Activity2JournalEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
