package com.starstudio.projectdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.starstudio.projectdemo.databinding.ActivityMainBinding;
import com.starstudio.projectdemo.utils.ContextHolder;
import com.starstudio.projectdemo.utils.RequestPermission;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

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
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        permissionRequest.checkPermissions(RequestPermission.CODE_MUST, permissions);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
            for (int res : grantResults)
                if (res != 0) {
                    Toast.makeText(this, "相关权限未授权，无法使用~", Toast.LENGTH_SHORT).show();
                    finish();
                }
    }
}