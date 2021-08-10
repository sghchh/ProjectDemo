package com.starstudio.projectdemo.journal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment2AlbumBinding;
import com.starstudio.projectdemo.journal.adapter.AlbumAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.journal.data.AlbumData;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh 2021-7-29
 * 为“心情日记”页面的“相册”板块
 */
public class AlbumFragment extends Fragment implements AlbumAdapter.AlbumItemClickListener {

    private Fragment2AlbumBinding binding;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = Fragment2AlbumBinding.inflate(inflater, container, false);
        AlbumAdapter adapter = new AlbumAdapter();
        adapter.setListener(this::OnAlbumItemClick);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        binding.recycler.addItemDecoration(new RecyclerGridDivider(10));
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

    @Override
    public void OnAlbumItemClick(View view, AlbumData data) {
        Bundle bundle = new Bundle();
        bundle.putString("albumName", data.getName());
        NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        navHost.getNavController().navigate(R.id.action_JournalFragment_to_AlbumPictureFragment, bundle);
    }
}
