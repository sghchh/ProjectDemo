package com.starstudio.projectdemo.journal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.starstudio.projectdemo.databinding.Activity2JournalEditBinding;
import com.starstudio.projectdemo.journal.api.HmsClassificationService;
import com.starstudio.projectdemo.journal.api.HmsImageService;
import com.starstudio.projectdemo.journal.api.HmsWeatherService;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            if(data.getDataString() != null && !data.getDataString().equals(""))
            editActivityData.setAudioPath(data.getDataString());
            Log.e(getClass().getSimpleName(), "获得的音频地址: " + data.getDataString());
        }
    }

}
