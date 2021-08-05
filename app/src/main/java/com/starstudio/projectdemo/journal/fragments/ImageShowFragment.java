package com.starstudio.projectdemo.journal.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3JournalDetailImgShowBinding;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-8-5
 * 用来展示picture的页面
 */
public class ImageShowFragment extends Fragment {
    Fragment3JournalDetailImgShowBinding binding;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = Fragment3JournalDetailImgShowBinding.inflate(inflater, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        String picture = getArguments().getString("picture");
        Bitmap bitmap = BitmapFactory.decodeFile(picture);
        binding.imageviewShow.setImageBitmap(bitmap);
        binding.imageviewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
                navHost.getNavController().navigateUp();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
