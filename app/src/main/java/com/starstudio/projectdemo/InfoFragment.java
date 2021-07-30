package com.starstudio.projectdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.starstudio.projectdemo.databinding.FragmentInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class InfoFragment extends Fragment {

    FragmentInfoBinding binding;
    private static final int PICTURE = 1;
    private static final int REQUEST_CODE_PERMISSION = 10;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        configToolbar();
        circleImage(null);
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


    // 该方法用来配置toolbar
    private void configToolbar() {
        binding.toolbarInfo.titleText.setText(R.string.toolbar_info);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarInfo.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //该方法用来将用户头像设置为圆形
    private void circleImage(File file){
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.nav_my_unselect)
                .placeholder(R.mipmap.nav_my_unselect)
                .transforms(new CircleCrop());

        Glide.with(getContext())
                .load(R.mipmap.nav_my_unselect)
                .apply(options)
                .into(binding.ivInfo);
    }

    //设置控件的点击事件
    private void setAllOnClick(){
        binding.tvResetPic.setOnClickListener(v->{
            //判断是否获取存储权限权限
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED){
                // 已获得权限
                openPicture();
            }else{
                //未获得权限,进行申请
                InfoFragment.this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }

        });
    }

    // 打开系统图库选择图片
    private void openPicture() {
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, PICTURE);
    }


    //请求权限后的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                if(ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED){
                    openPicture();
                }else{
                    showWaringDialog();
                }
            }
            return;
        }
    }

    //提示
    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this.getContext())
                .setTitle("警告！")
                .setMessage("如不打开读写权限, 则无法设置用户头像")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //startActivityForResult的回调方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //打开系统相册后执行此处
            case PICTURE:
                if(requestCode != RESULT_OK && data != null){
                    Toast.makeText(getActivity(), "相册返回成功", Toast.LENGTH_SHORT).show();
                    Log.e(getClass().getSimpleName(), "相册返回数据 -> " + data);
//                    //图库
//                    String pathResult = null;  // 获取图片路径的方法调用
//                    try {
//                        /**
//                         * 在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
//                         * 在4.4.2之后返回的是:content://com.android.providers.media.documents/document/image:3951或者...
//                         * 总结：uri的组成，eg:content://com.example.project:200/folder/subfolder/etc
//                         * content:--->"scheme"
//                         * com.example.project:200-->"host":"port"--->"authority"[主机地址+端口(省略) =authority]
//                         * folder/subfolder/etc-->"path" 路径部分
//                         * android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,所以要保证无论是哪个系统版本都能正确获取到图片资源的话
//                         * 就需要针对各种情况进行一个处理了
//                         **/
//                        Uri uri = data.getData();
//                        pathResult = getPath(uri);
//                        Log.e("TAG", "图片路径===" + pathResult);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
                break;
        }
    }

}
