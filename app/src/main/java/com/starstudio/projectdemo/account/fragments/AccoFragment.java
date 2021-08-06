package com.starstudio.projectdemo.account.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.starstudio.projectdemo.account.adapter.AccoAdapter;
import com.starstudio.projectdemo.databinding.FragmentAccoBinding;
import com.starstudio.projectdemo.databinding.FragmentAccountBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 为“记账”页面的“账单”
 */
public class AccoFragment extends Fragment {
    private FragmentAccoBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentAccoBinding.inflate(inflater,container,false);
        binding.recyclerAcco.setAdapter(new AccoAdapter(null));
        binding.recyclerAcco.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
