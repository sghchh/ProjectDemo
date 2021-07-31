package com.starstudio.projectdemo.journal.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.starstudio.projectdemo.journal.AlbumFragment;
import com.starstudio.projectdemo.journal.JourFragment;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh 2021-7-29
 * 为"心情日记"页面的ViewPager2提供适配器
 */
public class PagerAdapter extends FragmentStateAdapter {

    private final String[] TAGS = {"日记", "相册"};

    public PagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new JourFragment();
            return fragment;
        }

        fragment = new AlbumFragment();
        return fragment;
    }

    @Override
    public int getItemCount() {
        return TAGS.length;
    }
}
