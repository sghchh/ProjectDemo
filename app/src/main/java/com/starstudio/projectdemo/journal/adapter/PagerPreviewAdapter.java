package com.starstudio.projectdemo.journal.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.huawei.hms.image.vision.A;
import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class PagerPreviewAdapter extends PagerAdapter {
    private ArrayList<String> pciturePaths = new ArrayList<>();

    public PagerPreviewAdapter(ArrayList<String> data) {
        this.pciturePaths = data;
    }

    public void remove(int position) {
        this.pciturePaths.remove(position);
        notifyDataSetChanged();
    }

    public void reset(ArrayList<String> data) {
        this.pciturePaths = data;
        notifyDataSetChanged();
    }

    public String[] getDataArray() {
        if (pciturePaths.isEmpty())
            return null;
        String[] res = new String[pciturePaths.size()];
        pciturePaths.toArray(res);
        return res;
    }

    public void update(int positon, String newPath) {
        pciturePaths.set(positon, newPath);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_preview_item, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.pager_preview_img);
        img.setImageURI(Uri.fromFile(new File(pciturePaths.get(position))));
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
        return pciturePaths.size();
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
