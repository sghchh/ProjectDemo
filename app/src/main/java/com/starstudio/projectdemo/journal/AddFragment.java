package com.starstudio.projectdemo.journal;

import android.Manifest;
import android.app.Activity;
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
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment2AddJourBinding;
import com.starstudio.projectdemo.journal.adapter.AddImgVideoAdapter;
import com.starstudio.projectdemo.utils.RequestPermission;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-7-31
 * “写日记”页面
 */
public class AddFragment extends Fragment implements AddImgVideoAdapter.OnItemClickListener{
    private Fragment2AddJourBinding binding;
    private AddImgVideoAdapter addImgAdapter;
    private PopupWindow pop;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = Fragment2AddJourBinding.inflate(inflater, container, false);
        configView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            navHost.getNavController().navigate(R.id.action_JournalAddFragment_backto_JournalFragment);
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
        binding.recyclerAddImg.setAdapter(addImgAdapter);
        binding.recyclerAddImg.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
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
            showPop();
        }
    }

    /**
     * 点击“添加”按钮时底部弹窗
     */
    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.windows_bottom_pop, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        Activity activity = getActivity();
        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(activity)
                                .openGallery(PictureMimeType.ofImage())
                                //.maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(activity)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }
}
