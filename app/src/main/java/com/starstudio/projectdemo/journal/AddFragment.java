package com.starstudio.projectdemo.journal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment2AddJourBinding;
import com.starstudio.projectdemo.journal.adapter.AddImgVideoAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-7-31
 * “日记添加”页面
 */
public class AddFragment extends Fragment implements AddImgVideoAdapter.OnItemClickListener{
    Fragment2AddJourBinding binding;
    AddImgVideoAdapter addImgAdapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = Fragment2AddJourBinding.inflate(inflater, container, false);
        configToolbar();

        addImgAdapter = new AddImgVideoAdapter(this::onItemClick);
        binding.recyclerAddImg.setAdapter(addImgAdapter);
        binding.recyclerAddImg.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));

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

    // 用来构建appbar最右侧的按钮菜单
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu2_add_jour, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.send_jour) {
            Log.d("menuitemid", "onOptionsItemSelected: id"+item.getItemId());
            Toast.makeText(getActivity(), "点击了发表按钮", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == android.R.id.home) {
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            navHost.getNavController().navigate(R.id.action_JournalAddFragment_backto_JournalFragment);
        }
        return super.onOptionsItemSelected(item);
    }

    // 该方法用来配置toolbar
    private void configToolbar() {
        binding.toolbarAddJour.titleText.setText(R.string.toolbar_add_jour);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarAddJour.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(View view) {
        if (((AddImgVideoAdapter.ItemType)view.getTag()).equals(AddImgVideoAdapter.ItemType.FIRST)) {
            this.addImgAdapter.append("hello");
        }
    }
}
