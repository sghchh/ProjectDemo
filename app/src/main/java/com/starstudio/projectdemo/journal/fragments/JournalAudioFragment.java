package com.starstudio.projectdemo.journal.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManagerFactory;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerConfig;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerManager;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Fragment3JournalDetailAudioBinding;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JournalAudioFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Fragment3JournalDetailAudioBinding binding;

    private HwAudioManager mHwAudioManager;
    private HwAudioPlayerManager mHwAudioPlayerManager;
    private HwAudioStatusListener mPlayListener;
    List<HwAudioPlayItem> playItemList  = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();
    public static final int UPDATE_UI = 1;
    private String mAudioPath;

    private Handler UIhandle = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==UPDATE_UI) {
                if(mHwAudioPlayerManager != null){
                    int position = (int) mHwAudioPlayerManager.getOffsetTime();
                    int totalduration = (int) mHwAudioPlayerManager.getDuration();

                    if(position != -1 && totalduration != -1){
                        binding.videoProgess.setMax(totalduration);
                        binding.videoProgess.setProgress(position);

                        updateTime();
                        if(mHwAudioPlayerManager.isPlaying()) {
                            binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_pause_normal);
                        } else {
                            binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_play_normal);
                        }
                    }
                }

                UIhandle.sendEmptyMessageDelayed(UPDATE_UI, 500);
            }
        }
    };

    public JournalAudioFragment(){}


    public JournalAudioFragment(String audioPath){
        this.mAudioPath = audioPath;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = Fragment3JournalDetailAudioBinding.inflate(inflater, container, false);
        initEvent();
        createHwAudioManager();
        playLocalList();
//        initView();
        UIhandle.sendEmptyMessage(UPDATE_UI);
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
                    Log.e(TAG, "createHwAudioManager onSuccess");
                    mHwAudioManager = hwAudioManager;
                    // 获取播放管理实例。
                    mHwAudioPlayerManager = hwAudioManager.getPlayerManager();

                } catch (Exception e) {
                    Log.e(TAG, "player init fail");
                }
            }
            @Override
            public void onError(int errorCode) {
                Log.w(TAG, "init err:" + errorCode);
            }
        });

        mPlayListener = new HwAudioStatusListener() {


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
//            updateTime();
                if(currPos == duration && binding != null){
                    // 播放完成回调。
                    updateTime();
                    binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_play_normal);
                    pause();
                }
            }

            @Override
            public void onPlayCompleted(boolean isStopped) {
                if(isStopped){
//                    UIhandle.sendEmptyMessage(UPDATE_UI);
                    // 播放完成回调。
                    if(binding != null && mHwAudioPlayerManager != null){
                        binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_play_normal);
                        seekTo((int) mHwAudioPlayerManager.getDuration());
                        updateTime();
                        pause();
                    }
                }

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
            mHwAudioPlayerManager.pause();
        }
    }

    public List<HwAudioPlayItem> getLocalPlayItemList() {
        // 设置音频本地路径。
        String path = mAudioPath;
        Log.e(getClass().getSimpleName(), "音频本地路径: " + path);
        // 创建音频对象，写入音频信息。
        HwAudioPlayItem item = new HwAudioPlayItem();
        // 设置音频标题。
        item.setAudioTitle("");
        // 设置音频ID，作为音频文件的唯一标识，建议通过哈希值获取。
        item.setAudioId(String.valueOf(path.hashCode()));
        // 设置音频是否在线，0表示本地歌曲，1表示在线歌曲。
        item.setOnline(0);
        // 传入音频本地路径。
        item.setFilePath(path);
        playItemList.add(item);

        Log.e(getClass().getSimpleName(), "获取当前时间:" + (int) mHwAudioPlayerManager.getOffsetTime());
        Log.e(getClass().getSimpleName(), "获取总时间:" + (int) mHwAudioPlayerManager.getDuration());

        return playItemList;
    }

    private void initEvent() {
        binding.ivPreDetail.setOnClickListener(this);
        binding.rlPlayPauseDetail.setOnClickListener(this);
        binding.ivNextDetail.setOnClickListener(this);

        binding.videoProgess.setOnSeekBarChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_pre_detail:
                backWard();
                break;
            case R.id.iv_next_detail:
                forWard();
                break;
            case R.id.rl_play_pause_detail:
                Log.e(getClass().getSimpleName(), "获取当前时间:" + (int) mHwAudioPlayerManager.getOffsetTime());
                Log.e(getClass().getSimpleName(), "获取总时间:" + (int) mHwAudioPlayerManager.getDuration());
                Log.e(getClass().getSimpleName(), "执行了点击事件");
                playControl();
                break;
        }
    }

    /**
     * 更新播放时间
     */
    private void updateTime() {
//        if(!mIsInit){
//                initView();
//                mIsInit = true;
//                Log.e(getClass().getSimpleName(), "设置了bar");
//        }
        // 播放进度变化回调。
        if(mHwAudioPlayerManager != null){
            if(mHwAudioPlayerManager.getOffsetTime() != -1 && mHwAudioPlayerManager.getDuration() != -1){
                binding.audioStartTimeDetail.setText(OtherUtil.formatLongToTime((int) mHwAudioPlayerManager.getOffsetTime()));
                binding.audioEndTimeDetail.setText(OtherUtil.formatLongToTime((int) mHwAudioPlayerManager.getDuration()));
//            binding.videoProgess.setProgress((int) (mHwAudioPlayerManager.getOffsetTime() * 1000 / mHwAudioPlayerManager.getDuration()));
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(mHwAudioPlayerManager != null && fromUser){
            seekTo(progress);
        }
//        seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int process = seekBar.getProgress();
        seekTo(process);
    }

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
            if(mHwAudioPlayerManager.getDuration() != -1 && mHwAudioPlayerManager.getDuration() - 10000 <= position){
                mHwAudioPlayerManager.seekTo((int) mHwAudioPlayerManager.getDuration());
                updateTime();
                binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_play_normal);
                mHwAudioPlayerManager.pause();
            }else{
                mHwAudioPlayerManager.seekTo(position + 10000);
//                updateTime();
            }
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
            updateTime();
        }
    }


    private void playControl(){
        if(mHwAudioPlayerManager.isPlaying()) {
            pause();
            binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_play_normal);
        } else {
            play();
            binding.ivPlayDetail.setImageResource(R.mipmap.btn_playback_pause_normal);
        }
    }

    /**
     *  设置音频暂停
     */
    public void pause() {
        Log.e(TAG, "pause");
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
        Log.e(TAG, "play");
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "pause err");
            return;
        }

        int position = (int) mHwAudioPlayerManager.getOffsetTime();
        mHwAudioPlayerManager.playList(getLocalPlayItemList(),0,position);
        Log.e(getClass().getSimpleName(), "获取当前时间:" + (int) mHwAudioPlayerManager.getOffsetTime());
        Log.e(getClass().getSimpleName(), "获取总时间:" + (int) mHwAudioPlayerManager.getDuration());
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        removeListener(mPlayListener);
        if(UIhandle != null){
            UIhandle.removeCallbacksAndMessages(null);
        }
        mPlayListener = null;
        mHwAudioPlayerManager.stop();
        mHwAudioManager = null;
        mHwAudioPlayerManager = null;
        binding = null;

    }

}
