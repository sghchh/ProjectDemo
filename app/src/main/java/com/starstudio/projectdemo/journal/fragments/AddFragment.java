package com.starstudio.projectdemo.journal.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import com.starstudio.projectdemo.journal.activity.JournalVideoActivity;
import com.starstudio.projectdemo.journal.adapter.AddImgVideoAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.journal.api.HmsClassificationService;
import com.starstudio.projectdemo.journal.api.HmsWeatherService;
import com.starstudio.projectdemo.journal.api.JournalDaoService;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;
import com.starstudio.projectdemo.journal.data.JournalEntity;
import com.starstudio.projectdemo.utils.DisplayMetricsUtil;
import com.starstudio.projectdemo.utils.FileUtil;
import com.starstudio.projectdemo.utils.OtherUtil;
import com.starstudio.projectdemo.utils.RequestPermission;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * created by sgh
 * 2021-7-31
 * “写日记”页面
 */
public class AddFragment extends Fragment implements AddImgVideoAdapter.OnItemClickListener{
    private Fragment2AddJourBinding binding;
    private AddImgVideoAdapter addImgAdapter;
    private JournalDaoService daoService;
    private JournalEditActivityData editActivityData;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // 获取位于JournalEditActivity中的共享数据
        editActivityData = ((JournalEditActivity)getActivity()).getEditActivityData();

        daoService = JournalDaoService.getInstance();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = Fragment2AddJourBinding.inflate(inflater, container, false);
        configView();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.addImgAdapter.notifyDataSetChanged();
        checkAddMediaVisibility();
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

            JournalEntity journalEntity = new JournalEntity();
            journalEntity.setPostTime(System.currentTimeMillis());
            journalEntity.setLocation(HmsWeatherService.getLocation());
            journalEntity.setWeather(HmsWeatherService.getWeather());
            journalEntity.setMonth(OtherUtil.getSystemMonth()+"月"+OtherUtil.getSystemDay()+"日");
            journalEntity.setWeek(OtherUtil.getSystemWeek());
            journalEntity.setContent(binding.contentAdd.getText().toString());

            List<JournalEditActivityData.PictureWithCategory> _data = addImgAdapter.getData();
            List<String> destArray = new ArrayList<>();
            try {
                for (int i = 0; i < _data.size(); i ++)
                    // 将图片数据拷贝到com.starstudio.projectdemo对应的目录下
                    destArray.add(FileUtil.copyFromPath(_data.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "IO错误，添加失败！", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            journalEntity.setPictureArray(destArray);

            daoService.insert(journalEntity)
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Toast.makeText(getContext(), "插入数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });

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
        addImgAdapter = new AddImgVideoAdapter(editActivityData.getPictures(),this::onItemClick);

        binding.recyclerAddImg.setAdapter(addImgAdapter);
        binding.recyclerAddImg.getLayoutParams().height = (int)(DisplayMetricsUtil.getDisplayWidthPxiels(getActivity()) / 3);
        binding.recyclerAddImg.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.recyclerAddImg.addItemDecoration(new RecyclerGridDivider(5));

        // 添加点击事件选择视频文件
        binding.imageviewAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editActivityData.getVideoPath() != null) {
                    Intent intent = new Intent(getActivity(), JournalVideoActivity.class);
                    intent.putExtra("videoPath", editActivityData.getVideoPath());
                    startActivity(intent);
                    return;
                }
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofVideo())
                        .selectionMode(PictureConfig.SINGLE)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .maxVideoSelectNum(1)
                        .filterMaxFileSize(100000)  // 视频上限100MB
                        .imageSpanCount(3)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                String videoPath = result.get(0).getRealPath();
                                editActivityData.setVideoPath(videoPath);
                                binding.imageviewAddVideo.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                                binding.imageviewAddVideo.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                GlideEngine.createGlideEngine().loadImage(getActivity(), videoPath, binding.imageviewAddVideo);

                                // 添加了视频，所以需要关闭一些页面元素
                                cloasAddMedia();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
    }

    /**
     * 为RecyclerView中的每一个item添加点击事件
     * @param view
     */
    @Override
    public void onItemClick(View view, int curPosition) {
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
                    .maxSelectNum(Integer.MAX_VALUE)
                    .filterMaxFileSize(5000)   // 单张图片上限 5MB
                    .selectionMode(PictureConfig.MULTIPLE)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            ArrayList<JournalEditActivityData.PictureWithCategory> data = new ArrayList<>();
                            for (int i = 0; i < result.size(); i ++) {
                                String path = result.get(i).getRealPath();
                                data.add(new JournalEditActivityData.PictureWithCategory());
                                data.get(i).setPicturePath(path);

                                HmsClassificationService.classify(data.get(i));

                            }
                            // 将选择好的图片添加到Adapter中
                            addImgAdapter.append(data);

                            // 如果添加了图片，那么不可再“添加视频”与“添加音频”
                            cloasAddMedia();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        } else {
            // 添加点击事件，跳转到ImagePreviewFragment
            editActivityData.setCurrentPostion(editActivityData.getPictures().size() - 1 - curPosition);  // 因为显示的顺序是反的，所以要反过来
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigate(R.id.action_JournalAddFragment_to_ImagePreviewFragment);
        }
    }

    // 根据JournalEditActivity中数据的状态来确定关闭一些view
    private void cloasAddMedia() {
        if (editActivityData.getPictures().size() > 0) { // 说明添加了图片，则禁止再添加视频与音频
            binding.addJourAddVideoTxt.setVisibility(View.GONE);
            binding.addJourAddAudioTxt.setVisibility(View.GONE);
            binding.imageviewAddVideo.setVisibility(View.GONE);
        } else if (editActivityData.getVideoPath() != null) {   // 说明添加了视频，则禁止再添加图片与音频
            binding.addJourAddPicTxt.setVisibility(View.GONE);
            binding.recyclerAddImg.setVisibility(View.GONE);
            binding.addJourAddAudioTxt.setVisibility(View.GONE);
        } else if (editActivityData.getAudioPath() != null){                               // 说明添加了音频，则禁止再添加视频与图片
            binding.addJourAddVideoTxt.setVisibility(View.GONE);
            binding.imageviewAddVideo.setVisibility(View.GONE);
            binding.addJourAddPicTxt.setVisibility(View.GONE);
            binding.recyclerAddImg.setVisibility(View.GONE);
        }
    }

    /**
     * 当从各个媒体内容编辑页面返回来时，可能存在删除添加媒体内容的操作
     * 该方法检查是否需要重新show一些View元素
     * 在onStart中被调用
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkAddMediaVisibility() {
        JournalEditActivity activity = (JournalEditActivity) getActivity();
        if (editActivityData.getPictures().size() == 0 && editActivityData.getVideoPath() == null && editActivityData.getAudioPath() == null) {
            binding.addJourAddVideoTxt.setVisibility(View.VISIBLE);
            binding.addJourAddAudioTxt.setVisibility(View.VISIBLE);
            binding.imageviewAddVideo.setVisibility(View.VISIBLE);
            binding.addJourAddPicTxt.setVisibility(View.VISIBLE);
            binding.recyclerAddImg.setVisibility(View.VISIBLE);

            binding.imageviewAddVideo.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            binding.imageviewAddVideo.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            binding.imageviewAddVideo.setImageDrawable(activity.getDrawable(R.drawable.add_big));
        }
    }
}
