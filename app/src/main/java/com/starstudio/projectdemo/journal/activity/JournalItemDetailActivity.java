package com.starstudio.projectdemo.journal.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.starstudio.projectdemo.databinding.Activity2JournalItemDetailBinding;
import com.starstudio.projectdemo.journal.data.JournalEntity;


public class JournalItemDetailActivity extends AppCompatActivity {
    private Activity2JournalItemDetailBinding binding;
    public JournalEntity data;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (JournalEntity) getIntent().getBundleExtra("data").getSerializable("jourData");
        binding = Activity2JournalItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
