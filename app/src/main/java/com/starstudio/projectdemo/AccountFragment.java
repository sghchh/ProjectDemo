package com.starstudio.projectdemo;

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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.starstudio.projectdemo.account.fragments.AccoAddFragment;
import com.starstudio.projectdemo.account.fragments.PagerAdapter;
import com.starstudio.projectdemo.databinding.FragmentAccountBinding;

import org.jetbrains.annotations.NotNull;

public class AccountFragment extends Fragment {
    private final String[] TAGS = {"记账", "预算"};
    FragmentAccountBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        configToolbar();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.pager.setAdapter(new PagerAdapter(this));

        // 将ViewPager2与TabLayout绑定起来
        new TabLayoutMediator(binding.tabLayout, binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(TAGS[position]);
            }
        }).attach();
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
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.account_add) {
//            Toast.makeText(getActivity(), "点击了Account页面的add按钮", Toast.LENGTH_SHORT).show();
            DialogFragment dialogFragment = new AccoAddFragment();
            dialogFragment.show(getActivity().getSupportFragmentManager(),"添加");
        }
        Log.d("icon bounds", "icon bounds " + item.getIcon().getBounds().toShortString());
        return super.onOptionsItemSelected(item);
    }

    // 该方法用来配置toolbar
    private void configToolbar() {
        binding.toolbarAccount.titleText.setText(R.string.toolbar_account);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarAccount.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        setHasOptionsMenu(true);
    }
}
