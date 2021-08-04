package com.starstudio.projectdemo.journal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3PreviewImgsBinding;
import com.starstudio.projectdemo.journal.activity.FilterActivity;
import com.starstudio.projectdemo.journal.adapter.PagerPreviewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * created by sgh
 * 2021-8-3
 * “写日记”板块点击已添加的图片进入到预览页面
 */
public class ImagePreviewFragment extends Fragment {

    private Fragment3PreviewImgsBinding binding;
    private PagerPreviewAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getIntent() != null && getActivity().getIntent().getStringExtra("newPath") != null) {
            String newPath = getActivity().getIntent().getStringExtra("newPath");
            int position = getActivity().getIntent().getIntExtra("position", -1);
            adapter.update(position, newPath);
        }

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = Fragment3PreviewImgsBinding.inflate(inflater, container, false);

        String[] data = getArguments().getStringArray("picturePaths");
        adapter = new PagerPreviewAdapter(data);

        configView();
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.image_preview_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.image_preview_delete) {
            int currentItem = binding.pagerPreview.getCurrentItem();
            adapter.remove(currentItem);
        } else if (item.getItemId() == android.R.id.home) {
            Bundle bundle = new Bundle();
            bundle.putStringArray("picturePaths", adapter.getDataArray());
            // 添加点击事件，跳转到ImagePreviewFragment
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            navHost.getNavController().navigate(R.id.action_ImagePreviewFragment_backto_JourAddFragment, bundle);
        }
        return super.onOptionsItemSelected(item);
    }


    private void configView() {
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarFragment.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.pagerPreview.setAdapter(adapter);
        binding.cropBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.filterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                int position = binding.pagerPreview.getCurrentItem();
                intent.putExtra("position", position);
                intent.putExtra("picturePath", adapter.getDataArray()[position]);
                startActivity(intent);
            }
        });
    }


}
