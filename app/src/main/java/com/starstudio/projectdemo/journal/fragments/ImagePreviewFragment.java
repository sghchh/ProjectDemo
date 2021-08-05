package com.starstudio.projectdemo.journal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.huawei.hms.image.vision.B;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3PreviewImgsBinding;
import com.starstudio.projectdemo.journal.activity.JournalEditActivity;
import com.starstudio.projectdemo.journal.adapter.PagerPreviewAdapter;

import org.jetbrains.annotations.NotNull;


/**
 * created by sgh
 * 2021-8-3
 * “写日记”板块点击已添加的图片进入到预览页面
 */
public class ImagePreviewFragment extends Fragment {

    private Fragment3PreviewImgsBinding binding;
    private PagerPreviewAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = Fragment3PreviewImgsBinding.inflate(inflater, container, false);
        adapter = new PagerPreviewAdapter(((JournalEditActivity)getActivity()).picturePaths);
        configView();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adapter.notifyDataSetChanged();
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
            adapter.remove(binding.pagerPreview.getCurrentItem());
        } else if (item.getItemId() == android.R.id.home) {
            // 添加点击事件，跳转到ImagePreviewFragment
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigateUp();
        }
        return super.onOptionsItemSelected(item);
    }


    private void configView() {
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        binding.pagerPreview.setAdapter(adapter);
        binding.cropTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.filterTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = binding.pagerPreview.getCurrentItem();
                ((JournalEditActivity)getActivity()).currentPostion = position;
                NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
                navHost.getNavController().navigate(R.id.action_ImagePreviewFragment_to_FilterFragment);
            }
        });
    }


}
