package com.starstudio.projectdemo.journal.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.starstudio.projectdemo.journal.adapter.AddPictureAdapter;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.journal.api.HmsClassificationService;
import com.starstudio.projectdemo.journal.api.HmsWeatherService;
import com.starstudio.projectdemo.journal.api.JournalDaoService;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;
import com.starstudio.projectdemo.journal.data.JournalEntity;
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
 * ?????????????????????
 */
public class AddFragment extends Fragment implements AddPictureAdapter.OnItemClickListener{
    private Fragment2AddJourBinding binding;
    private AddPictureAdapter addImgAdapter;
    private JournalDaoService daoService;
    private JournalEditActivityData editActivityData;
    private PopupWindow pop;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // ????????????JournalEditActivity??????????????????
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

    // ????????????appbar????????????????????????
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
            journalEntity.setMonth(OtherUtil.getSystemMonth()+"???"+OtherUtil.getSystemDay()+"???");
            journalEntity.setWeek(OtherUtil.getSystemWeek());
            journalEntity.setContent(binding.contentAdd.getText().toString());

            List<JournalEditActivityData.PictureWithCategory> _data = addImgAdapter.getData();
            List<String> destArray = new ArrayList<>();
            try {
                for (int i = 0; i < _data.size(); i ++)
                    // ????????????????????????com.starstudio.projectdemo??????????????????
                    destArray.add(FileUtil.copyFromPath(_data.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "IO????????????????????????", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            journalEntity.setPictureArray(destArray);
            journalEntity.setVideo(editActivityData.getVideoPath());
            journalEntity.setAudio(editActivityData.getAudioPath());

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
                            Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // ?????????????????????????????????
    private void configView() {

        // ????????????toolbar
        binding.toolbarAddJour.titleText.setText(R.string.toolbar_add_jour);
        // ???Fragment???toolbar?????????
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarAddJour.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ????????????RecyclerView
        addImgAdapter = new AddPictureAdapter(editActivityData.getPictures(),this::onItemClick);

        binding.recyclerAddImg.setAdapter(addImgAdapter);
        binding.recyclerAddImg.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.recyclerAddImg.addItemDecoration(new RecyclerGridDivider(5));

        binding.imageviewAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

        binding.addJournalVideoPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
                navHost.getNavController().navigate(R.id.action_JournalAddFragment_to_VideoPreviewFragment);
            }
        });

        binding.addJournalAudioIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
                navHost.getNavController().navigate(R.id.action_JournalAddFragment_to_AudioPreviewFragment);
            }
        });
    }

    /**
     * ??????PictureSelector????????????
     */
    private void selectPicture(){
        // ?????????????????????sd?????????
        RequestPermission.getInstance().checkPermissions(RequestPermission.CODE_SIMPLE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);

        // ????????????????????????
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(4)
                .maxSelectNum(Integer.MAX_VALUE)
                .filterMaxFileSize(5000)   // ?????????????????? 5MB
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
                        // ??????????????????????????????Adapter???
                        addImgAdapter.append(data);

                        // ??????????????????????????????????????????????????????????????????????????????
                        checkAddMediaVisibility();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * ??????PictureSelector??????????????????
     */
    private void selectVideo() {
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofVideo())
                .selectionMode(PictureConfig.SINGLE)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxVideoSelectNum(1)
                .filterMaxFileSize(100000)  // ????????????100MB
                .imageSpanCount(3)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        String videoPath = result.get(0).getRealPath();
                        editActivityData.setVideoPath(videoPath);
                        GlideEngine.createGlideEngine().loadImage(getActivity(), videoPath, binding.addJournalVideoPre);

                        // ??????????????????????????????????????????????????????
                        checkAddMediaVisibility();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * ??????PictureSelector??????????????????
     */
    private void selectAudio() {
//        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
//        intent1.setType("audio/*"); //????????????
//        getActivity().startActivityForResult(intent1, 300);

        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofAudio())
                .selectionMode(PictureConfig.SINGLE)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxVideoSelectNum(1)
                .filterMaxFileSize(100000)  // ????????????100MB
                .imageSpanCount(3)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        String audioPath = result.get(0).getRealPath();
                        editActivityData.setAudioPath(audioPath);

                        // ??????????????????????????????????????????????????????
                        checkAddMediaVisibility();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
//        checkAddMediaVisibility();
    }

    /**
     * ??????????????????RecyclerView???????????????item??????????????????
     * @param view
     */
    @Override
    public void onItemClick(View view, int curPosition) {
        if (((AddPictureAdapter.ItemType)view.getTag()).equals(AddPictureAdapter.ItemType.FIRST)) {
            // ????????????????????????
            selectPicture();
        } else {
            // ??????????????????????????????ImagePreviewFragment
            editActivityData.setCurrentPostion(editActivityData.getPictures().size() - 1 - curPosition);  // ???????????????????????????????????????????????????
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigate(R.id.action_JournalAddFragment_to_ImagePreviewFragment);
        }
    }


    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mVideo = bottomView.findViewById(R.id.tv_video);
        TextView mAudio = bottomView.findViewById(R.id.tv_audio);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:   // ??????
                        selectPicture();
                        break;
                    case R.id.tv_video:    //??????
                        selectVideo();
                        break;
                    case R.id.tv_audio:    // ??????
                        selectAudio();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mVideo.setOnClickListener(clickListener);
        mAudio.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    // ????????????
    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    /**
     * 1. ????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????show??????View??????
     * ???onStart????????????
     * 2. ????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkAddMediaVisibility() {
        if(editActivityData.getPictures().size() > 0) {  // ?????????????????????????????????????????????GONE
            binding.addJourPicTxt.setVisibility(View.VISIBLE);
            binding.recyclerAddImg.setVisibility(View.VISIBLE);

            binding.imageviewAddMedia.setVisibility(View.GONE);
            binding.addJourMediaTxt.setVisibility(View.GONE);

            binding.addJournalVideoRoot.setVisibility(View.GONE);
            binding.addJourVideoTxt.setVisibility(View.GONE);

            binding.addJourAudioTxt.setVisibility(View.GONE);
            binding.addJournalAudioRoot.setVisibility(View.GONE);
        } else if (editActivityData.getVideoPath() != null) {
            binding.addJournalVideoRoot.setVisibility(View.VISIBLE);
            binding.addJourVideoTxt.setVisibility(View.VISIBLE);

            binding.imageviewAddMedia.setVisibility(View.GONE);
            binding.addJourMediaTxt.setVisibility(View.GONE);

            binding.addJourPicTxt.setVisibility(View.GONE);
            binding.recyclerAddImg.setVisibility(View.GONE);

            binding.addJourAudioTxt.setVisibility(View.GONE);
            binding.addJournalAudioRoot.setVisibility(View.GONE);
        } else if (editActivityData.getAudioPath() != null) {
            binding.addJourAudioTxt.setVisibility(View.VISIBLE);
            binding.addJournalAudioRoot.setVisibility(View.VISIBLE);

            binding.imageviewAddMedia.setVisibility(View.GONE);
            binding.addJourMediaTxt.setVisibility(View.GONE);

            binding.addJourPicTxt.setVisibility(View.GONE);
            binding.recyclerAddImg.setVisibility(View.GONE);

            binding.addJournalVideoRoot.setVisibility(View.GONE);
            binding.addJourVideoTxt.setVisibility(View.GONE);
        } else {
            binding.imageviewAddMedia.setVisibility(View.VISIBLE);
            binding.addJourMediaTxt.setVisibility(View.VISIBLE);

            binding.addJourPicTxt.setVisibility(View.GONE);
            binding.recyclerAddImg.setVisibility(View.GONE);

            binding.addJournalVideoRoot.setVisibility(View.GONE);
            binding.addJourVideoTxt.setVisibility(View.GONE);

            binding.addJourAudioTxt.setVisibility(View.GONE);
            binding.addJournalAudioRoot.setVisibility(View.GONE);
        }
    }
}
