package com.starstudio.projectdemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.starstudio.projectdemo.databinding.FragmentInfoBinding;
import com.starstudio.projectdemo.journal.GlideEngine;
import com.starstudio.projectdemo.journal.api.HmsClassificationService;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;
import com.starstudio.projectdemo.utils.FileUtil;
import com.starstudio.projectdemo.utils.RequestPermission;
import com.starstudio.projectdemo.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class InfoFragment extends Fragment {

    FragmentInfoBinding binding;
    private static final int PICTURE = 1;
    private static final int REQUEST_CODE_PERMISSION = 10;
    private SharedPreferencesUtils mSharedPreferencesUtils;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        mSharedPreferencesUtils = SharedPreferencesUtils.getInstance(getContext());
        configToolbar();
        initView();
        setAllOnClick();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // ?????????????????????toolbar
    private void configToolbar() {
        binding.toolbarInfo.titleText.setText(R.string.toolbar_info);
        // ???Fragment???toolbar?????????
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarInfo.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //???????????????????????????
    private void initView(){
        RequestOptions options = new RequestOptions()
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo);
        Glide.with(getContext())
                .load(mSharedPreferencesUtils.readString("avatar"))
                .apply(options)
                .skipMemoryCache(true)
                .into(binding.avatar);

        if(mSharedPreferencesUtils.readString(binding.etInfoName.getId() + "") != ""){
            binding.etInfoName.setText(mSharedPreferencesUtils.readString(binding.etInfoName.getId() + ""));
        }
    }


    //???????????????????????????
    private void setAllOnClick(){
        binding.tvResetPic.setOnClickListener(v->{
            //????????????????????????????????????
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED){
                // ???????????????
                selectPicture();
            }else{
                //???????????????,????????????
                InfoFragment.this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }

        });

        binding.etInfoName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.e(getClass().getSimpleName(), "???????????????Keycode???: " + keyCode);

                //?????????????????????????????????????????????????????????????????????????????????
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    binding.etInfoName.clearFocus();
                    InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(binding.etInfoName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mSharedPreferencesUtils.putString(binding.etInfoName.getId() + "",binding.etInfoName.getText().toString());
                }
                return false;
            }
        });
    }

    private void selectPicture(){
        // ????????????????????????
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(4)
                .maxSelectNum(1)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        String picPath = result.get(0).getRealPath();
                        mSharedPreferencesUtils.putString("avatar", FileUtil.saveAvatar(picPath));
                        RequestOptions options = new RequestOptions()
                                .error(R.drawable.logo)
                                .placeholder(R.drawable.logo);
                        Glide.with(getContext())
                                .load(mSharedPreferencesUtils.readString("avatar"))
                                .apply(options)
                                .skipMemoryCache(true)
                                .into(binding.avatar);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    //??????
    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this.getContext())
                .setTitle("?????????")
                .setMessage("????????????????????????, ???????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
