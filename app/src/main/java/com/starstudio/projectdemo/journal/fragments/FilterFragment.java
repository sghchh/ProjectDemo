package com.starstudio.projectdemo.journal.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.image.vision.bean.ImageVisionResult;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3FilterBinding;
import com.starstudio.projectdemo.journal.activity.JournalEditActivity;
import com.starstudio.projectdemo.journal.adapter.FilterAdapter;
import com.starstudio.projectdemo.journal.api.HmsImageService;
import com.starstudio.projectdemo.utils.FileUtil;
import com.starstudio.projectdemo.utils.HandlerHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

/**
 * creted by sgh
 * 2021-8-4
 * 为图片添加滤镜的activity
 */
public class FilterFragment extends Fragment implements FilterAdapter.OnFilterTypeClickListener {

    private Fragment3FilterBinding binding;
    private HmsImageService hmsImageService;
    private ArrayList<String> picturePaths;
    private int position;
    private String originPath;
    private ImageVisionResult filterRes = null;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        hmsImageService = HmsImageService.getInstance();

        picturePaths = ((JournalEditActivity)getActivity()).picturePaths;
        position = ((JournalEditActivity)getActivity()).currentPostion;
        originPath = picturePaths.get(position);

        binding = Fragment3FilterBinding.inflate(inflater, container, false);
        configView();
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 添加点击事件，跳转到ImagePreviewFragment
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigateUp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configView() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        FilterAdapter adapter = new FilterAdapter(hmsImageService.getTypes());
        adapter.setListener(this::onFilterTypeClick);
        binding.recyclerFilter.setAdapter(adapter);
        binding.recyclerFilter.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.imageviewFilter.setImageURI(Uri.fromFile(new File(picturePaths.get(position))));

        binding.saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterRes != null) {
                    String newPath = FileUtil.saveBitmap(filterRes.getImage());
                    if (newPath.endsWith(".jpg"))
                        picturePaths.set(position, newPath);
                    Toast.makeText(getActivity(), "已保存修改", Toast.LENGTH_SHORT).show();
                    filterRes = null;
                }
            }
        });

        HandlerHelper.register(FilterFragment.class, new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                filterRes = (ImageVisionResult) message.obj;
                binding.imageviewFilter.setImageBitmap(filterRes.getImage());
                return true;
            }
        });
    }

    @Override
    public void onFilterTypeClick(View v, String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    message.obj = hmsImageService.getFilterResult(type, originPath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HandlerHelper.send(FilterFragment.class, message);;
            }
        }).start();
    }
}
