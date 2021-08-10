package com.starstudio.projectdemo.journal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3AlbumPictureBinding;
import com.starstudio.projectdemo.journal.adapter.AlbumPictureAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.utils.FileUtil;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-8-10
 * 展示每一个“相册”图片内容的Fragment
 */
public class AlbumPictureFragment extends Fragment {
    private Fragment3AlbumPictureBinding binding;
    private String albumName;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        assert getArguments() != null;
        albumName = getArguments().getString("albumName");

        binding = Fragment3AlbumPictureBinding.inflate(inflater, container, false);
        binding.albumToolbarTitleTxt.setText(albumName);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        binding.albumPicRecycler.setAdapter(new AlbumPictureAdapter(FileUtil.loadAlbumPictures(albumName)));
        binding.albumPicRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        binding.albumPicRecycler.addItemDecoration(new RecyclerGridDivider(10));
        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            navHost.getNavController().navigateUp();
        }
        return super.onOptionsItemSelected(item);
    }
}
