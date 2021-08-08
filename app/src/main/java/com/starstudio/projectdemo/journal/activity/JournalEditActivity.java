package com.starstudio.projectdemo.journal.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.starstudio.projectdemo.databinding.Activity2JournalEditBinding;
import com.starstudio.projectdemo.journal.api.HmsImageService;
import com.starstudio.projectdemo.journal.api.HmsWeatherService;

import java.util.ArrayList;

public class JournalEditActivity extends AppCompatActivity {
    Activity2JournalEditBinding binding;
    public ArrayList<String> picturePaths = new ArrayList<>();
    public int currentPostion = -1;
    public String videoPath = null;

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
