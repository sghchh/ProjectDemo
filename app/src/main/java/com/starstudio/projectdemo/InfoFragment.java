package com.starstudio.projectdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.starstudio.projectdemo.databinding.FragmentInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class InfoFragment extends Fragment {

    FragmentInfoBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        configToolbar();
        circleImage(null);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // 该方法用来配置toolbar
    private void configToolbar() {
        binding.toolbarInfo.titleText.setText(R.string.toolbar_info);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarInfo.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //该方法用来将用户头像设置为圆形
    private void circleImage(File file){
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.nav_my_unselect)
                .placeholder(R.mipmap.nav_my_unselect)
                .transforms(new CircleCrop());

        Glide.with(getContext())
                .load(R.mipmap.nav_my_unselect)
                .apply(options)
                .into(binding.ivInfo);
    }
}
