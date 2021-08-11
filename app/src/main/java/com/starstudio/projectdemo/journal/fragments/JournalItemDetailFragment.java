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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3JournalItemDetailBinding;
import com.starstudio.projectdemo.journal.GlideEngine;
import com.starstudio.projectdemo.journal.activity.JournalItemDetailActivity;
import com.starstudio.projectdemo.journal.activity.JournalVideoActivity;
import com.starstudio.projectdemo.journal.adapter.JourDetailImgAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.journal.api.JournalDaoService;
import com.starstudio.projectdemo.journal.data.JournalEntity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * created by sgh
 * 2021-8-5
 * 日记详情展示界面
 */
public class JournalItemDetailFragment extends Fragment implements JourDetailImgAdapter.OnDetailItemClickListener {
    private Fragment3JournalItemDetailBinding binding;
    private JournalEntity data;
    private JournalDaoService daoService;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        daoService = JournalDaoService.getInstance();

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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.preview_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().finish();
        else if (item.getItemId() == R.id.image_preview_delete) {
            daoService.delte(data)
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Toast.makeText(getContext(), "删除数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    private void configView() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        data = ((JournalItemDetailActivity)getActivity()).data;
        binding.date.setText(data.getMonth());
        binding.location.setText(data.getLocation());
        binding.week.setText(data.getWeek());

        // 展示文本内容
        if (data.getContent() != null) {
            binding.content.setVisibility(View.VISIBLE);
            binding.content.setText(data.getContent());
        }

        // 展示图片内容
        if (data.getPictureArray().size() != 0) {
            binding.imgRecycler.setVisibility(View.VISIBLE);
            int columns = Math.min(data.getPictureArray().size(), 3);
            JourDetailImgAdapter adapter = new JourDetailImgAdapter(data.getPictureArray());
            adapter.setItemClickListener(this::onItemClick);
            binding.imgRecycler.setAdapter(adapter);
            binding.imgRecycler.setLayoutManager(new GridLayoutManager(getActivity(), columns, RecyclerView.VERTICAL, false));
            binding.imgRecycler.addItemDecoration(new RecyclerGridDivider(10));
            return;
        }

        // 展示视频预览
        if (data.getVideo() != null) {
            binding.journalDetailVideoRoot.setVisibility(View.VISIBLE);
            File pic = new File(data.getVideo());
            GlideEngine.createGlideEngine().loadImage(getActivity(), data.getVideo(), binding.journalDetailVideoPre);
            //Glide.with(binding.journalDetailVideoPre.getContext()).load(pic).centerInside().load(binding.journalDetailVideoPre);
            binding.journalDetailVideoPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), JournalVideoActivity.class);
                    intent.putExtra("videoPath", data.getVideo());
                    startActivity(intent);
                }
            });
        }

        //展示音频
        if (data.getAudio() != null) {

        }

    }

    @Override
    public void onItemClick(View v, String path) {
        NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_item_detail);
        Bundle bundle = new Bundle();
        bundle.putString("picture", path);
        navHost.getNavController().navigate(R.id.action_DetailFragment_to_ImageShowFragment, bundle);
    }
}
