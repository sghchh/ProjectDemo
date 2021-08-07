package com.starstudio.projectdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.CaptureClient;
import com.huawei.hms.kit.awareness.capture.LocationResponse;
import com.starstudio.projectdemo.Custom.HideInputActivity;
import com.starstudio.projectdemo.databinding.ActivityMainBinding;
import com.starstudio.projectdemo.utils.ContextHolder;
import com.starstudio.projectdemo.utils.FileUtil;
import com.starstudio.projectdemo.utils.RequestPermission;
import com.starstudio.projectdemo.utils.SharedPreferencesUtils;

import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends HideInputActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RequestPermission permissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        ContextHolder.init(this);

        // 进行权限申请的操作
        RequestPermission.init(this);
        permissionRequest = RequestPermission.getInstance();
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        // 本次申请的权限是必要的，即没用通过则会导致APP无法使用
        permissionRequest.checkPermissions(RequestPermission.CODE_MUST, permissions);

        // 进行文件操作
        FileUtil.init(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(this);

        configView();

    }

    /*
        在该方法中对各种View添加点击事件、相互关联等配置
     */
    private void configView() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // 将BottomNavigationView与NavController绑定
        NavigationUI.setupWithNavController(binding.bottomNavMain, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermission.CODE_MUST)
            for (int res : grantResults) {
                if (res != 0) {
                    Toast.makeText(this, "相关权限未通过,无法使用~", Toast.LENGTH_SHORT).show();
                }
            }
    }

}