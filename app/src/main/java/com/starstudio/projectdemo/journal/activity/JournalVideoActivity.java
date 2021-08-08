package com.starstudio.projectdemo.journal.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.image.vision.C;
import com.starstudio.projectdemo.databinding.Activity2JournalVideoBinding;
import com.starstudio.projectdemo.journal.api.HmsVideoService;

public class JournalVideoActivity extends AppCompatActivity {
    private Activity2JournalVideoBinding binding;
    private String videoPath;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        videoPath = getIntent().getStringExtra("videoPath");
        HmsVideoService.init();
        binding = Activity2JournalVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void configView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HmsVideoService.start(this, videoPath);
    }
}
