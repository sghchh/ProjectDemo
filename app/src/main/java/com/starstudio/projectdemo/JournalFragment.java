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

import com.starstudio.projectdemo.databinding.FragmentJournalBinding;
import com.starstudio.projectdemo.databinding.FragmentTodoBinding;

import org.jetbrains.annotations.NotNull;

public class JournalFragment extends Fragment {

    FragmentJournalBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        configToolbar();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 用来构建appbar最右侧的按钮菜单
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    // 该方法用来配置toolbar
    private void configToolbar() {
        binding.toolbarJournal.titleText.setText(R.string.toolbar_journal);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarJournal.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
