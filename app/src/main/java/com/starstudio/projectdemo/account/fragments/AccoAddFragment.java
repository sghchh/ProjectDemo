package com.starstudio.projectdemo.account.fragments;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscription;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConfig;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConstants;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionListener;
import com.starstudio.projectdemo.Custom.EditTextWithText;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.OtherUtil;

public class AccoAddFragment extends DialogFragment implements View.OnClickListener {

    private EditTextWithText etwtKind, etwtMoney;
    private EditText etComment;
    private ImageView ivVoice, ivCancle;
    private MLSpeechRealTimeTranscription mSpeechRecognizer;
    private static final int AUDIO_PERMISSION_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_add_acco, container, false);
        initView(rootView);
        setAllOnClickListener();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        setSize();

    }

    //设置宽和高
    private void setSize(){
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );

        WindowManager.LayoutParams params = win.getAttributes();
        // 使用ViewGroup.LayoutParams，以便 Dialog 宽度控制
        params.width =  dm.widthPixels - OtherUtil.dp2px(this.getContext(),16);
        params.height = OtherUtil.dp2px(this.getContext(),387);
        win.setAttributes(params);
    }

    private void initView(View rootView){
        etwtKind = rootView.findViewById(R.id.et_kind);
        etwtMoney = rootView.findViewById(R.id.et_money);
        etComment = rootView.findViewById(R.id.et_comment);
        ivVoice = rootView.findViewById(R.id.iv_voice_add);
        ivCancle = rootView.findViewById(R.id.iv_cancel);

        etwtKind.setLeadText("分类选择");
        etwtKind.setLeadTextSize(17);
        etwtKind.setLeadTextColor("#FF646A73");

        etwtMoney.setLeadText("￥");
        etwtMoney.setLeadTextSize(23);
        etwtMoney.setLeadTextColor("#FF646A73");

        SpannableString ss = new SpannableString("     添加备注");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etComment.setHint(new SpannedString(ss));

    }

    private void setAllOnClickListener(){
        ivVoice.setOnClickListener(this);
        ivCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_voice_add:
                requestCameraPermission();
                speechRecognizer();
                Toast.makeText(this.getContext(), "点击了语音按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_cancel:

                break;
        }
    }

    private void speechRecognizer(){
        MLSpeechRealTimeTranscriptionConfig config = new MLSpeechRealTimeTranscriptionConfig.Factory()
                // 设置语言，目前支持中文、英语、法语转写。
                .setLanguage(MLSpeechRealTimeTranscriptionConstants.LAN_ZH_CN)
                // 设置标点。
                .enablePunctuation(true)
                // 设置句子的偏移。
                .enableSentenceTimeOffset(true)
                // 设置词的偏移。
                .enableWordTimeOffset(true)
                // 设置使用场景，MLSpeechRealTimeTranscriptionConstants.SCENES_SHOPPING：表示购物，仅支持中文，该场景对华为商品名识别进行了优化。
                .setScenes(MLSpeechRealTimeTranscriptionConstants.SCENES_SHOPPING)
                .create();
        mSpeechRecognizer = MLSpeechRealTimeTranscription.getInstance();

        //绑定语音识别器
        mSpeechRecognizer.setRealTimeTranscriptionListener(new SpeechRecognitionListener());

        //启动语音识别
        mSpeechRecognizer.startRecognizing(config);


    }

    // 回调实现MLSpeechRealTimeTranscriptionListener接口，实现接口中的方法。
    protected class SpeechRecognitionListener implements MLSpeechRealTimeTranscriptionListener {
        @Override
        public void onStartListening() {
            // 录音器开始接收声音。
        }

        @Override
        public void onStartingOfSpeech() {
            // 用户开始讲话，即语音识别器检测到用户开始讲话。
        }

        @Override
        public void onVoiceDataReceived(byte[] data, float energy, Bundle bundle) {
            // 返回给用户原始的PCM音频流和音频能量，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        }

        @Override
        public void onRecognizingResults(Bundle partialResults) {
            // 从MLSpeechRealTimeTranscription接收到持续语音识别的文本。
            Log.e(getClass().getSimpleName(),"语音识别内容: " + partialResults.getString("data"));
        }

        @Override
        public void onError(int error, String errorMessage) {
            // 识别发生错误后调用该接口。
            // 从MLSpeechRealTimeTranscription接收到持续语音识别的文本。
            Log.e(getClass().getSimpleName(),"语音识别发生错误: " + errorMessage);
        }

        @Override
        public void onState(int state,Bundle params) {
            // 通知应用状态发生改变。
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.RECORD_AUDIO)) {
            ActivityCompat.requestPermissions(this.getActivity(), permissions, AccoAddFragment.AUDIO_PERMISSION_CODE);
            return;
        }
    }

    // Permission application callback.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != AccoAddFragment.AUDIO_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer!= null) {
            mSpeechRecognizer.destroy();
        }
    }
}
