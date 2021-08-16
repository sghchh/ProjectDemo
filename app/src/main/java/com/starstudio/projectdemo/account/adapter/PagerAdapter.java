package com.starstudio.projectdemo.account.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.starstudio.projectdemo.account.fragments.AccoFragment;
import com.starstudio.projectdemo.account.fragments.BudgetFragment;

import org.jetbrains.annotations.NotNull;

/**
 * 为“记账”页面的ViewPager2提供适配器
 */
public class PagerAdapter extends FragmentStateAdapter {

    private final String[] TAGS = {"记账", "预算"};

    public PagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if(position == 0){
            fragment = new AccoFragment();
        }else{
            fragment = new BudgetFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return TAGS.length;
    }
}
