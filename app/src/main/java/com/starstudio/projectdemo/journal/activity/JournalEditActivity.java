package com.starstudio.projectdemo.journal.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.image.vision.A;
import com.starstudio.projectdemo.databinding.Activity2JournalEditBinding;
import com.starstudio.projectdemo.journal.api.HmsClassificationService;
import com.starstudio.projectdemo.journal.api.HmsImageService;
import com.starstudio.projectdemo.journal.api.HmsWeatherService;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;

import java.util.ArrayList;

public class JournalEditActivity extends AppCompatActivity {
    Activity2JournalEditBinding binding;
    // 共享数据
    private final JournalEditActivityData editActivityData = new JournalEditActivityData();

    public JournalEditActivityData getEditActivityData() {
        return editActivityData;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HmsImageService.init(this);
        HmsWeatherService.init(this);
        HmsClassificationService.init();

        binding = Activity2JournalEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
