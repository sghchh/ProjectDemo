package com.starstudio.projectdemo.journal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.huawei.hms.image.vision.B;
import com.huawei.hms.videoeditor.sdk.p.H;
import com.starstudio.projectdemo.databinding.Fragment2JourBinding;
import com.starstudio.projectdemo.journal.activity.JournalItemDetailActivity;
import com.starstudio.projectdemo.journal.adapter.JourAdapter;
import com.starstudio.projectdemo.journal.api.JournalDatabase;
import com.starstudio.projectdemo.journal.data.JourData;
import com.starstudio.projectdemo.journal.data.JournalEntity;
import com.starstudio.projectdemo.utils.HandlerHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * created by sgh 2021-7-29
 * “心情日记”页面下的"日记"板块
 */
public class JourFragment extends Fragment implements JourAdapter.OnJourItemClickListener {

    private Fragment2JourBinding binding;
    private JournalDatabase database;
    private JourAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        database = JournalDatabase.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e("TAG", "run: 从数据库中加载到的数据个数为"+ database.journalDAO().loadAllJournal().length);
            }
        }).start();
        binding = Fragment2JourBinding.inflate(inflater, container, false);
        binding.recycler.setAdapter(new JourAdapter(new JournalEntity[]{}));
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onJourItemClick(View view, JournalEntity data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("jourData", data);
        Intent intent = new Intent(getActivity(), JournalItemDetailActivity.class);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }
}
