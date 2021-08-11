package com.starstudio.projectdemo.journal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PagerPreviewAdapter extends PagerAdapter {
    private ArrayList<JournalEditActivityData.PictureWithCategory> pcitures = new ArrayList<>();

    public PagerPreviewAdapter(ArrayList<JournalEditActivityData.PictureWithCategory> data) {
        this.pcitures = data;
    }

    public void remove(int position) {
        this.pcitures.remove(position);
        notifyDataSetChanged();
    }

    @SuppressLint("CheckResult")
    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_preview_item, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.pager_preview_img);
        Glide.with(img).load(pcitures.get(position).getPicturePath()).into(img);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return pcitures.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }
}
