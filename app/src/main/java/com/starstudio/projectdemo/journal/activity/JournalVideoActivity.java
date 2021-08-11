package com.starstudio.projectdemo.journal.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.Activity3JournalVideoBinding;
import com.starstudio.projectdemo.utils.OtherUtil;

/**
 * created by sgh
 * 2021-8-11
 * 由“帖子详情”跳转过来观看video的界面
 */
public class JournalVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener, View.OnClickListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener,
        SeekBar.OnSeekBarChangeListener{
    private Activity3JournalVideoBinding binding;
    private String videoPath;

    private MediaPlayer mPlayer;
    private boolean isShow = false;

    public static final int UPDATE_TIME = 0x0001;
    public static final int HIDE_CONTROL = 0x0002;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIME:
                    updateTime();
                    mHandler.sendEmptyMessageDelayed(UPDATE_TIME, 500);
                    break;
                case HIDE_CONTROL:
                    hideControl();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoPath = getIntent().getStringExtra("videoPath");
        configView();
        initSurfaceView();
        initPlayer();
        initEvent();
    }

    private void initEvent() {
        binding.playOrPauseVideo.setOnClickListener(this);
        binding.rlVideo.setOnClickListener(this);
        //binding.rlVideo.setOnTouchListener(this);
        binding.videoForward.setOnClickListener(this);
        binding.videoBackward.setOnClickListener(this);
        binding.videoProgess.setOnSeekBarChangeListener(this);
    }
    private void initSurfaceView() {
        binding.surfaceView.setZOrderOnTop(false);
        binding.surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        binding.surfaceView.getHolder().addCallback(this);
    }

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);
        try {
            //使用手机本地视频
            mPlayer.setDataSource(videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer.setDisplay(holder);
        mPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        binding.videoStartTime.setText(OtherUtil.formatLongToTime(mp.getCurrentPosition()));
        binding.videoEndTime.setText(OtherUtil.formatLongToTime(mp.getDuration()));
        binding.videoProgess.setMax(mp.getDuration());
        binding.videoProgess.setProgress(mp.getCurrentPosition());
    }
    @Override
    public void onCompletion(MediaPlayer mp) {

    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }
    private void play() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mHandler.removeMessages(UPDATE_TIME);
            mHandler.removeMessages(HIDE_CONTROL);
            binding.playOrPauseVideo.setVisibility(View.VISIBLE);
            binding.playOrPauseVideo.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mPlayer.start();
            mHandler.sendEmptyMessageDelayed(UPDATE_TIME, 500);
            mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, 5000);
            binding.playOrPauseVideo.setVisibility(View.INVISIBLE);
            binding.playOrPauseVideo.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //TODO
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        changeVideoSize();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_backward:
                backWard();
                break;
            case R.id.video_forward:
                forWard();
                break;
            case R.id.playOrPause_video:
                play();
                break;
            case R.id.rl_video:
                showControl();
                break;
        }
    }
    /**
     * 更新播放时间
     */
    private void updateTime() {

        binding.videoStartTime.setText(OtherUtil.formatLongToTime(mPlayer.getCurrentPosition()));
        binding.videoProgess.setProgress(mPlayer.getCurrentPosition());
    }

    /**
     * 隐藏进度条
     */
    private void hideControl() {
        isShow = false;
        mHandler.removeMessages(UPDATE_TIME);
        binding.controlLl.animate().setDuration(300).translationY(binding.controlLl.getHeight());
    }
    /**
     * 显示进度条
     */
    private void showControl() {
        if (isShow) {
            play();
        }
        isShow = true;
        mHandler.removeMessages(HIDE_CONTROL);
        mHandler.sendEmptyMessage(UPDATE_TIME);
        mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, 5000);
        binding.controlLl.animate().setDuration(300).translationY(0);
    }
    /**
     * 设置快进10秒方法
     */
    private void forWard(){
        if(mPlayer != null){
            int position = mPlayer.getCurrentPosition();
            mPlayer.seekTo(position + 10000);
        }
    }

    /**
     * 设置快退10秒的方法
     */
    public void backWard(){
        if(mPlayer != null){
            int position = mPlayer.getCurrentPosition();
            if(position > 10000){
                position-=10000;
            }else{
                position = 0;
            }
            mPlayer.seekTo(position);
        }
    }

    //OnSeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if(mPlayer != null && b){
            mPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }



    private void configView() {
        binding = Activity3JournalVideoBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }

    //改变视频的尺寸自适应。
    public void changeVideoSize() {
        int videoWidth = mPlayer.getVideoWidth();
        int videoHeight = mPlayer.getVideoHeight();

        int surfaceWidth = binding.surfaceView.getWidth();
        int surfaceHeight = binding.surfaceView.getHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
        params.addRule(RelativeLayout.CENTER_VERTICAL, binding.rlVideo.getId());
        binding.surfaceView.setLayoutParams(params);
    }

}
