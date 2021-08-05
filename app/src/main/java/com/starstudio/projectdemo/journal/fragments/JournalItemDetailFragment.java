package com.starstudio.projectdemo.journal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.image.vision.B;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3JournalItemDetailBinding;
import com.starstudio.projectdemo.journal.activity.JournalItemDetailActivity;
import com.starstudio.projectdemo.journal.adapter.JourDetailImgAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.journal.data.JourData;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-8-5
 * 日记详情展示界面
 */
public class JournalItemDetailFragment extends Fragment implements JourDetailImgAdapter.OnDetailItemClickListener {
    private Fragment3JournalItemDetailBinding binding;
    private JourData data;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        binding = Fragment3JournalItemDetailBinding.inflate(inflater, container, false);
        configView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().finish();
        return super.onOptionsItemSelected(item);
    }

    private void configView() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = ((JournalItemDetailActivity)getActivity()).data;
        binding.date.setText(data.getDate());
        binding.location.setText(data.getLoaction());
        binding.week.setText(data.getWeek());
        binding.content.setText(data.getContent());
        JourDetailImgAdapter adapter = new JourDetailImgAdapter(data.getImgs());
        adapter.setItemClickListener(this::onItemClick);
        binding.imgRecycler.setAdapter(adapter);
        binding.imgRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        binding.imgRecycler.addItemDecoration(new RecyclerGridDivider(10));
    }

    @Override
    public void onItemClick(View v, String path) {
        NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_detail);
        Bundle bundle = new Bundle();
        bundle.putString("picture", path);
        navHost.getNavController().navigate(R.id.action_DetailFragment_to_ImageShowFragment, bundle);
    }
}
