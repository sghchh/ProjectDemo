package com.starstudio.projectdemo.todo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment2TodoAnalyseBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * created by sgh
 * 2021-8-12
 * ”完成度分析“页面
 */
public class AnalyseFragment extends Fragment {
    private Fragment2TodoAnalyseBinding binding;
    private int allNum;    // 总计个数
    private int doneNum;   // 已完成个数

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        allNum = getArguments().getInt("allNum");
        doneNum = getArguments().getInt("doneNum");
        setHasOptionsMenu(true);
        binding = Fragment2TodoAnalyseBinding.inflate(inflater, container, false);
        configView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void configView() {
        binding.todoAnalyseToolbar.titleText.setText("完成度分析");
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.todoAnalyseToolbar.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        int[] colors = new int[]{Color.rgb(72, 208, 77), Color.rgb(203, 201, 205)};
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(allNum - doneNum, "未完成"));
        pieEntries.add(new PieEntry(doneNum, "已完成"));
        PieDataSet iPieDataSet = new PieDataSet(pieEntries, "pie label");
        iPieDataSet.setColors(colors);
        List<Integer> textColors = new ArrayList<>();
        textColors.add(Color.WHITE);
        textColors.add(Color.WHITE);
        iPieDataSet.setValueTextColors(textColors);
        iPieDataSet.setSliceSpace(3);   // 每块之间的距离
        PieData pieData = new PieData(iPieDataSet);
        binding.todoAnalysePie.setData(pieData);
        /*mPieChart.setDrawSliceText(true);*/   // : 将X值绘制到饼状图环切片内,否则不显示。默认true,已弃用，用下面setDrawEntryLabels()
        binding.todoAnalysePie.setDrawEntryLabels(true);   // 同上,默认true，记住颜色和环不要一样，否则会显示不出来
        binding.todoAnalysePie.setUsePercentValues(true);    // 表内数据用百分比替代，而不是原先的值。并且ValueFormatter中提供的值也是该百分比的。默认false
        binding.todoAnalysePie.setHoleRadius(0);  // 设置中心圆半径占整个饼形图圆半径（图表半径）的百分比。默认50%
        binding.todoAnalysePie.setMaxAngle(360);    // 设置整个饼形图的角度，默认是360°即一个整圆，也可以设置为弧，这样现实的值也会重新计算
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            navHost.getNavController().navigateUp();
        }
        return super.onOptionsItemSelected(item);
    }
}
