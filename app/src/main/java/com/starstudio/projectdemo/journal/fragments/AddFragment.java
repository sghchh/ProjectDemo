package com.starstudio.projectdemo.journal.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
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
import com.starstudio.projectdemo.journal.api.HmsWeatherService;
import com.starstudio.projectdemo.journal.api.JournalDaoService;
import com.starstudio.projectdemo.journal.api.JournalDatabase;
import com.starstudio.projectdemo.journal.data.JournalEntity;
import com.starstudio.projectdemo.utils.DisplayMetricsUtil;
import com.starstudio.projectdemo.utils.OtherUtil;
import com.starstudio.projectdemo.utils.RequestPermission;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * created by sgh
 * 2021-7-31
 * “写日记”页面
 */
public class AddFragment extends Fragment implements AddImgVideoAdapter.OnItemClickListener{
    private Fragment2AddJourBinding binding;
    private AddImgVideoAdapter addImgAdapter;
    private JournalDaoService daoService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        daoService = JournalDaoService.getInstance();
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
            JournalEntity journalEntity = new JournalEntity();
            journalEntity.setPostTime(System.currentTimeMillis());
            journalEntity.setLocation(HmsWeatherService.getLocation());
            journalEntity.setMonth(OtherUtil.getSystemMonth()+"月"+OtherUtil.getSystemDay()+"日");
            journalEntity.setWeek(OtherUtil.getSystemWeek());
            journalEntity.setContent(binding.contentAdd.getText().toString());
            journalEntity.setPictureArray(addImgAdapter.getData());

            daoService.insert(journalEntity)
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Log.e("rxjava2", "onComplete: 插入数据执行完毕");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Log.e("rxjava2", "onError: 插入数据失败");
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
        addImgAdapter = new AddImgVideoAdapter(((JournalEditActivity)getActivity()).picturePaths,this::onItemClick);

        binding.recyclerAddImg.setAdapter(addImgAdapter);
        binding.recyclerAddImg.getLayoutParams().height = (int)(DisplayMetricsUtil.getDisplayWidthPxiels(getActivity()) / 3);
        binding.recyclerAddImg.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.recyclerAddImg.addItemDecoration(new RecyclerGridDivider(5));

        // 添加点击事件选择视频文件
        binding.imageviewAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((JournalEditActivity)getActivity()).videoPath != null) {
                    Intent intent = new Intent(getActivity(), JournalVideoActivity.class);
                    intent.putExtra("videoPath", ((JournalEditActivity)getActivity()).videoPath);
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
                                Log.d("pictureselector", "onResult: 视频地址为"+result.get(0).getRealPath());
                                String videoPath = result.get(0).getRealPath();
                                ((JournalEditActivity)getActivity()).videoPath = videoPath;
                                binding.imageviewAddVideo.getLayoutParams().width = DisplayMetricsUtil.getDisplayWidthPxiels(getActivity());
                                GlideEngine.createGlideEngine().loadImage(getActivity(), videoPath, binding.imageviewAddVideo);
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
                    .filterMaxFileSize(5000)   // 单张图片上限 5MB
                    .selectionMode(PictureConfig.MULTIPLE)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            ArrayList<String> data = new ArrayList<>();
                            for (int i = 0; i < result.size(); i ++) {
                                data.add(result.get(i).getRealPath());
                            }
                            // 将选择好的图片添加到Adapter中
                            addImgAdapter.append(data);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        } else {
            // 添加点击事件，跳转到ImagePreviewFragment
            ((JournalEditActivity)getActivity()).currentPostion = ((JournalEditActivity)getActivity()).picturePaths.size() - 1 - curPosition;  // 因为显示的顺序是反的，所以要反过来
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigate(R.id.action_JournalAddFragment_to_ImagePreviewFragment);
        }
    }

}
