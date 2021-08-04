package com.starstudio.projectdemo.journal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.databinding.Activity2FilterBinding;
import com.starstudio.projectdemo.journal.adapter.FilterAdapter;
import com.starstudio.projectdemo.journal.api.HmsImageService;

import java.io.File;

/**
 * creted by sgh
 * 2021-8-4
 * 为图片添加滤镜的activity
 */
public class FilterActivity extends AppCompatActivity {

    private Activity2FilterBinding binding;
    private HmsImageService hmsImageService;
    private String picturePath;
    private int position;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hmsImageService = HmsImageService.getInstance(this);

        picturePath = getIntent().getStringExtra("picturePath");
        position = getIntent().getIntExtra("position", -1);

        binding = Activity2FilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configView();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("picturePath", picturePath);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerFilter.setAdapter(new FilterAdapter(hmsImageService.getTypes()));
        binding.recyclerFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.imageviewFilter.setImageURI(Uri.fromFile(new File(picturePath)));

        binding.saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
