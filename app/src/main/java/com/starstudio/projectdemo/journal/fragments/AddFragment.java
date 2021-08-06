package com.starstudio.projectdemo.journal.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment2AddJourBinding;
import com.starstudio.projectdemo.journal.GlideEngine;
import com.starstudio.projectdemo.journal.activity.JournalEditActivity;
import com.starstudio.projectdemo.journal.adapter.AddImgVideoAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.utils.DisplayMetricsUtil;
import com.starstudio.projectdemo.utils.RequestPermission;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by sgh
 * 2021-7-31
 * “写日记”页面
 */
public class AddFragment extends Fragment implements AddImgVideoAdapter.OnItemClickListener{
    private Fragment2AddJourBinding binding;
    private AddImgVideoAdapter addImgAdapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = Fragment2AddJourBinding.inflate(inflater, container, false);
        configView();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.addImgAdapter.notifyDataSetChanged();
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
            Toast.makeText(getActivity(), "点击了发表按钮", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // 该方法用来配置页面元素
    private void configView() {

        // 首先配置toolbar
        binding.toolbarAddJour.titleText.setText(R.string.toolbar_add_jour);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarAddJour.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 之后配置RecyclerView
        addImgAdapter = new AddImgVideoAdapter(this::onItemClick);
        if (((JournalEditActivity)getActivity()).picturePaths.size() > 0)
            addImgAdapter.reset(((JournalEditActivity)getActivity()).picturePaths);

        binding.recyclerAddImg.setAdapter(addImgAdapter);
        binding.recyclerAddImg.getLayoutParams().height = (int)(DisplayMetricsUtil.getDisplayWidthPxiels(getActivity()) / 3);
        binding.recyclerAddImg.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.recyclerAddImg.addItemDecoration(new RecyclerGridDivider(5));
    }

    /**
     * 为RecyclerView中的每一个item添加点击事件
     * @param view
     */
    @Override
    public void onItemClick(View view) {
        // 首先检查并申请sd卡权限
        RequestPermission.getInstance().checkPermissions(RequestPermission.CODE_SIMPLE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);

        if (((AddImgVideoAdapter.ItemType)view.getTag()).equals(AddImgVideoAdapter.ItemType.FIRST)) {
            // 选择图片或者拍照
            PictureSelector.create(getActivity())
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .imageSpanCount(4)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            ArrayList<String> data = new ArrayList<>();
                            for (int i = 0; i < result.size(); i ++) {
                                data.add(result.get(i).getRealPath());
                            }
                            ((JournalEditActivity)getActivity()).picturePaths.addAll(data);
                            // 将选择好的图片添加到Adapter中
                            addImgAdapter.append(data);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        } else {
            // 添加点击事件，跳转到ImagePreviewFragment
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigate(R.id.action_JournalAddFragment_to_ImagePreviewFragment);
        }
    }

}