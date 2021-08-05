package com.starstudio.projectdemo.journal.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.starstudio.projectdemo.databinding.Activity2JournalEditBinding;
import com.starstudio.projectdemo.journal.data.JourData;


public class JournalItemDetailActivity extends AppCompatActivity {
    private Activity2JournalEditBinding binding;
    public JourData data;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = Activity2JournalEditBinding.inflate(getLayoutInflater());
        configView();
        setContentView(binding.getRoot());
    }

    private void configView() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("详情");
        data = (JourData) getIntent().getBundleExtra("data").getSerializable("jourData");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
