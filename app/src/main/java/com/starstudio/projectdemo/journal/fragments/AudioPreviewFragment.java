package com.starstudio.projectdemo.journal.fragments;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack;
import com.huawei.hms.audiokit.player.manager.HwAudioConfigManager;
import com.huawei.hms.audiokit.player.manager.HwAudioEffectManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManagerFactory;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerConfig;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerManager;
import com.huawei.hms.audiokit.player.manager.HwAudioQueueManager;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3PreviewAudioBinding;
import com.starstudio.projectdemo.databinding.Fragment3PreviewImgsBinding;
import com.starstudio.projectdemo.journal.activity.JournalEditActivity;
import com.starstudio.projectdemo.journal.adapter.PagerPreviewAdapter;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;
import com.starstudio.projectdemo.utils.OtherUtil;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * “写日记”板块点击已添加的音频进入到预览页面
 */
public class AudioPreviewFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Fragment3PreviewAudioBinding binding;
    private JournalEditActivityData data;

    private HwAudioManager mHwAudioManager;
    private HwAudioPlayerManager mHwAudioPlayerManager;
    private HwAudioConfigManager mHwAudioConfigManager;
    private HwAudioQueueManager mHwAudioQueueManager;
    private HwAudioEffectManager mHwAudioEffectManager;
    List<HwAudioPlayItem> playItemList  = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();

    private HwAudioStatusListener mPlayListener = new HwAudioStatusListener() {

        @Override
        public void onSongChange(HwAudioPlayItem song) {
            // 当前音频变化回调。
        }

        @Override
        public void onQueueChanged(List<HwAudioPlayItem> infos) {
            // 队列变化回调。
        }

        @Override
        public void onBufferProgress(int percent) {
            // 缓冲进度变化回调。
        }

        @Override
        public void onPlayProgress(long currPos, long duration) {
            // 播放进度变化回调。
            updateTime();
        }

        @Override
        public void onPlayCompleted(boolean isStopped) {
            // 播放完成回调。
        }

        @Override
        public void onPlayError(int errorCode, boolean isUserForcePlay) {
            // 播放出错回调。
        }

        @Override
        public void onPlayStateChange(boolean isPlaying, boolean isBuffering) {
            // 播放状态变化回调。
        }
    };


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        data = ((JournalEditActivity)getActivity()).getEditActivityData();
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = Fragment3PreviewAudioBinding.inflate(inflater, container, false);
        configView();
        initEvent();
        createHwAudioManager();
        playLocalList();
        initView();
        return binding.getRoot();
    }


    public void createHwAudioManager() {
        // 创建配置实例，包含各种播放相关的配置。其中context信息传入不可为空。
        HwAudioPlayerConfig hwAudioPlayerConfig = new HwAudioPlayerConfig(getContext());
        // 根据实际情况创建HwAudioManager所需的配置。
        hwAudioPlayerConfig.setDebugMode(true).setDebugPath("").setPlayCacheSize(20);
        // 执行创建管理实例的操作。
        HwAudioManagerFactory.createHwAudioManager(hwAudioPlayerConfig, new HwAudioConfigCallBack() {
            // Audio Kit通过回调返回管理实例。
            @Override
            public void onSuccess(HwAudioManager hwAudioManager) {
                try {
                    Log.i(TAG, "createHwAudioManager onSuccess");
                    mHwAudioManager = hwAudioManager;
                    // 获取播放管理实例。
                    mHwAudioPlayerManager = hwAudioManager.getPlayerManager();
                    // 获取配置管理实例。
                    mHwAudioConfigManager = hwAudioManager.getConfigManager();
                    // 获取队列管理实例。
                    mHwAudioQueueManager = hwAudioManager.getQueueManager();
//                    // 获取音效管理实例。
//                    mHwAudioEffectManager = hwAudioManager.getEffectManager();
                } catch (Exception e) {
                    Log.i(TAG, "player init fail");
                }
            }
            @Override
            public void onError(int errorCode) {
                Log.w(TAG, "init err:" + errorCode);
            }
        });

        addListener(mPlayListener);
    }

    public void addListener(HwAudioStatusListener listener) {
        if (mHwAudioManager != null) {
            try {
                mHwAudioManager.addPlayerStatusListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG, TAG, e);
            }
        }
    }

    public void removeListener(HwAudioStatusListener listener) {
        if (mHwAudioManager != null) {
            try {
                mHwAudioManager.removePlayerStatusListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG, TAG, e);
            }
        }
    }


    public void playLocalList() {
        if (mHwAudioPlayerManager != null) {
            // 播放本地列表歌曲。
            mHwAudioPlayerManager.playList(getLocalPlayItemList(), 0, 0);
        }
    }

    public List<HwAudioPlayItem> getLocalPlayItemList() {
        // 设置音频本地路径。
        String path = data.getAudioPath();
        // 创建音频对象，写入音频信息。
        HwAudioPlayItem item = new HwAudioPlayItem();
        // 设置音频标题。
        item.setAudioTitle("Playing input song");
        // 设置音频ID，作为音频文件的唯一标识，建议通过哈希值获取。
        item.setAudioId(String.valueOf(path.hashCode()));
        // 设置音频是否在线，0表示本地歌曲，1表示在线歌曲。
        item.setOnline(0);
        // 传入音频本地路径。
        item.setFilePath(path);
        playItemList.add(item);
        return playItemList;
    }

    private void initEvent() {
        binding.ivPre.setOnClickListener(this);
        binding.rlPlayPause.setOnClickListener(this);
        binding.ivNext.setOnClickListener(this);

        binding.videoProgess.setOnSeekBarChangeListener(this);
    }

    private void initView(){
        binding.videoStartTime.setText(OtherUtil.formatLongToTime((int) mHwAudioPlayerManager.getOffsetTime()));
        binding.videoEndTime.setText(OtherUtil.formatLongToTime((int) mHwAudioPlayerManager.getDuration()));
        binding.videoProgess.setMax((int) mHwAudioPlayerManager.getDuration());
        binding.videoProgess.setProgress((int) mHwAudioPlayerManager.getOffsetTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_pre:
                backWard();
                break;
            case R.id.iv_next:
                forWard();
                break;
            case R.id.rl_play_pause:
                playControl();
                break;
        }
    }

    /**
     * 更新播放时间
     */
    private void updateTime() {
        binding.videoStartTime.setText(OtherUtil.formatLongToTime((int) mHwAudioPlayerManager.getOffsetTime()));
        binding.videoProgess.setProgress((int) mHwAudioPlayerManager.getOffsetTime());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    public void seekTo(int pos) {
        Log.i(TAG, "setQueuePosition,pos: " + pos);
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "setQueuePosition err");
            return;
        }
        mHwAudioPlayerManager.seekTo(pos);
    }

    /**
     * 设置快进10秒方法
     */
    private void forWard(){
        if(mHwAudioPlayerManager != null){
            int position = (int) mHwAudioPlayerManager.getOffsetTime();
            mHwAudioPlayerManager.seekTo(position + 10000);
        }
    }

    /**
     * 设置快退10秒的方法
     */
    private void backWard(){
        if(mHwAudioPlayerManager != null){
            int position = (int) mHwAudioPlayerManager.getOffsetTime();
            if(position > 10000){
                position-=10000;
            }else{
                position = 0;
            }
            mHwAudioPlayerManager.seekTo(position);
        }
    }


    private void playControl(){
        if(mHwAudioPlayerManager.isPlaying()) {
            pause();
            binding.ivPlay.setImageResource(R.mipmap.btn_playback_pause_normal);
        } else {
            play();
            binding.ivPlay.setImageResource(R.mipmap.btn_playback_play_normal);
        }
    }

    /**
     *  设置音频暂停
     */
    public void pause() {
        Log.i(TAG, "pause");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "pause err");
            return;
        }
        mHwAudioPlayerManager.pause();
    }

    /**
     *  设置音频开始
     */
    public void play() {
        Log.i(TAG, "pause");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "pause err");
            return;
        }
        int position = (int) mHwAudioPlayerManager.getOffsetTime();
        mHwAudioPlayerManager.seekTo(position);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        removeListener(mPlayListener);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.preview_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 点击返回按钮，则返回上一级Fragment
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigateUp();
        } else if (item.getItemId() == R.id.image_preview_delete) {
            // 先删除，然后返回上一级Fragment
            data.setVideoPath(null);
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_jounal_edit);
            navHost.getNavController().navigateUp();
        }
        return super.onOptionsItemSelected(item);
    }


    private void configView() {
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
    }



}
