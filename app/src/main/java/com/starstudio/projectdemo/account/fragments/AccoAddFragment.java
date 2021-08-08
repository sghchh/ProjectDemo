package com.starstudio.projectdemo.account.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscription;
import com.starstudio.projectdemo.Custom.EditTextWithText;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.HandlerHelper;
import com.starstudio.projectdemo.utils.OtherUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AccoAddFragment extends DialogFragment implements View.OnClickListener {

    private EditTextWithText etwtKind, etwtMoney;
    private EditText etComment;
    private ImageView ivVoice;
    private MLSpeechRealTimeTranscription mSpeechRecognizer;
    private static final int AUDIO_PERMISSION_CODE = 1;
    // Permission
    private static final String[] ALL_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int WRITE_PERMISSION_CODE = 0;
    public static final int ML_ASR_CAPTURE_CODE = 2;
    public static final String REAL_VOICE_SUCCESS = "success";
    public static final int REAL_VOICE_SUCCESS_CODE = 0;


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
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  dm.widthPixels - OtherUtil.dp2px(this.getContext(),16);
        params.height = OtherUtil.dp2px(this.getContext(),387);
        win.setAttributes(params);
    }

    private void initView(View rootView){
        etwtKind = rootView.findViewById(R.id.et_kind);
        etwtMoney = rootView.findViewById(R.id.et_money);
        etComment = rootView.findViewById(R.id.et_comment);
        ivVoice = rootView.findViewById(R.id.iv_voice_add);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_voice_add:
                startRealTimeVoice();

                Toast.makeText(this.getContext(), "点击了语音按钮", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void startRealTimeVoice() {
        //                requestCameraPermission();
//                speechRecognizer();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }else{
            MLApplication.getInstance().setApiKey("CgB6e3x9c2tIlXQZdvRg9VeCfngxvAwbW5FpKsYs/7eW39cdgYZ90pxu2gM85yEp+f2zCFSTXy4CebF3cdcULMzc");
            // Use Intent for recognition settings.
            Intent intentPlugin = new Intent(this.getContext(), MLAsrCaptureActivity.class)
                    // Set the language that can be recognized to English. If this parameter is not set,
                    // English is recognized by default. Example: "zh-CN": Chinese；"en-US": English；"fr-FR": French；"es-ES": Spanish；"de-DE": German；"it-IT": Italian。
                    .putExtra(MLAsrCaptureConstants.LANGUAGE, "zh-CN")
                    // Set whether to display text on the speech pickup UI. MLAsrCaptureConstants.FEATURE_ALLINONE: no;
                    // MLAsrCaptureConstants.FEATURE_WORDFLUX: yes.
                    .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX);
            // Set the usage scenario to shopping,Currently, only Chinese scenarios are supported.
            // .putExtra(MLAsrConstants.SCENES_KEY, MLAsrConstants.SCENES_SHOPPING);
            // ML_ASR_CAPTURE_CODE: request code between the current activity and speech pickup UI activity.
            // You can use this code to obtain the processing result of the speech pickup UI.
            startActivityForResult(intentPlugin, ML_ASR_CAPTURE_CODE);
        }
    }

    // Check the permissions required by the SDK.
    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ArrayList<String> permissionsList = new ArrayList<>();
            for (String perm : getAllPermission()) {
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this.getContext(), perm)) {
                    permissionsList.add(perm);
                }
            }
            if (!permissionsList.isEmpty()) {
                ActivityCompat.requestPermissions(this.getActivity(), permissionsList.toArray(new String[0]), WRITE_PERMISSION_CODE);
            }
        }
    }

    public static List<String> getAllPermission() {
        return Collections.unmodifiableList(Arrays.asList(ALL_PERMISSION));
    }

    // Permission application callback.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), permissions[0])) {
                    showWaringDialog();
                } else {
//                    Toast.makeText(this.getContext(), "Grant the permission to experience the function.", Toast.LENGTH_SHORT).show();
                    MLApplication.getInstance().setApiKey("CgB6e3x9c2tIlXQZdvRg9VeCfngxvAwbW5FpKsYs/7eW39cdgYZ90pxu2gM85yEp+f2zCFSTXy4CebF3cdcULMzc");
                    // Use Intent for recognition settings.
                    Intent intentPlugin = new Intent(this.getContext(), MLAsrCaptureActivity.class)
                            // Set the language that can be recognized to English. If this parameter is not set,
                            // English is recognized by default. Example: "zh-CN": Chinese；"en-US": English；"fr-FR": French；"es-ES": Spanish；"de-DE": German；"it-IT": Italian。
                            .putExtra(MLAsrCaptureConstants.LANGUAGE, "zh-CN")
                            // Set whether to display text on the speech pickup UI. MLAsrCaptureConstants.FEATURE_ALLINONE: no;
                            // MLAsrCaptureConstants.FEATURE_WORDFLUX: yes.
                            .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX);
                    // Set the usage scenario to shopping,Currently, only Chinese scenarios are supported.
                    // .putExtra(MLAsrConstants.SCENES_KEY, MLAsrConstants.SCENES_SHOPPING);
                    // ML_ASR_CAPTURE_CODE: request code between the current activity and speech pickup UI activity.
                    // You can use this code to obtain the processing result of the speech pickup UI.
                    getParentFragment().startActivityForResult(intentPlugin, ML_ASR_CAPTURE_CODE);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showWaringDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setMessage("Obtaining related permissions: Write permissions failed. Some functions cannot be used properly. You need to manually grant permissions on the settings page.")
                .setPositiveButton("Go Authorization", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Guide the user to the setting page for manual authorization.
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Instruct the user to perform manual authorization. The permission request fails.
                    }
                }).setOnCancelListener(dialogInterface);
        dialog.setCancelable(false);
        dialog.show();
    }

    static DialogInterface.OnCancelListener dialogInterface = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            //Instruct the user to perform manual authorization. The permission request fails.
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == ML_ASR_CAPTURE_CODE) {
            realVoiceForResult(resultCode, data);
        }
    }

    private void realVoiceForResult(int resultCode, @org.jetbrains.annotations.Nullable Intent data) {
        String text = "";
        //实时语音识别返回值
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }

        switch (resultCode) {
            // MLAsrCaptureConstants.ASR_SUCCESS: Recognition is successful.
            case MLAsrCaptureConstants.ASR_SUCCESS:
                // Obtain the text information recognized from speech.
                if (bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                    text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT);
                }
                if (text == null || "".equals(text)) {
                    text = "Result is null.";
                }
                etComment.setText(text);
                // Process the recognized text information.
//                displayResult(text);
//                break;
//            // MLAsrCaptureConstants.ASR_FAILURE: Recognition fails.
//            case MLAsrCaptureConstants.ASR_FAILURE:
//                // Check whether a result code is contained.
//                if (bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
//                    text = text + bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE);
//                    // Perform troubleshooting based on the result code.
//                }
//                // Check whether error information is contained.
//                if (bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)) {
//                    String errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE);
//                    // Perform troubleshooting based on the error information.
//                    if (errorMsg != null && !"".equals(errorMsg)) {
//                        text = "[" + text + "]" + errorMsg;
//                    }
//                }
//                // Check whether a sub-result code is contained.
//                if (bundle.containsKey(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE)) {
//                    int subErrorCode = bundle.getInt(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE);
//                    // Process the sub-result code.
//                    text = "[" + text + "]" + subErrorCode;
//                }
//                displayResult(text);
//                break;
//            default:
//                displayResult("Failure.");
//                break;
        }
    }

//    private void speechRecognizer(){
//        MLSpeechRealTimeTranscriptionConfig config = new MLSpeechRealTimeTranscriptionConfig.Factory()
//                // 设置语言，目前支持中文、英语、法语转写。
//                .setLanguage(MLSpeechRealTimeTranscriptionConstants.LAN_ZH_CN)
//                // 设置标点。
//                .enablePunctuation(true)
//                // 设置句子的偏移。
//                .enableSentenceTimeOffset(true)
//                // 设置词的偏移。
//                .enableWordTimeOffset(true)
//                // 设置使用场景，MLSpeechRealTimeTranscriptionConstants.SCENES_SHOPPING：表示购物，仅支持中文，该场景对华为商品名识别进行了优化。
//                .setScenes(MLSpeechRealTimeTranscriptionConstants.SCENES_SHOPPING)
//                .create();
//        mSpeechRecognizer = MLSpeechRealTimeTranscription.getInstance();
//
//        //绑定语音识别器
//        mSpeechRecognizer.setRealTimeTranscriptionListener(new SpeechRecognitionListener());
//
//        //启动语音识别
//        mSpeechRecognizer.startRecognizing(config);
//
//
//    }
//
//    // 回调实现MLSpeechRealTimeTranscriptionListener接口，实现接口中的方法。
//    protected class SpeechRecognitionListener implements MLSpeechRealTimeTranscriptionListener {
//        @Override
//        public void onStartListening() {
//            // 录音器开始接收声音。
//        }
//
//        @Override
//        public void onStartingOfSpeech() {
//            // 用户开始讲话，即语音识别器检测到用户开始讲话。
//        }
//
//        @Override
//        public void onVoiceDataReceived(byte[] data, float energy, Bundle bundle) {
//            // 返回给用户原始的PCM音频流和音频能量，该接口并非运行在主线程中，返回结果需要在子线程中处理。
//        }
//
//        @Override
//        public void onRecognizingResults(Bundle partialResults) {
//            // 从MLSpeechRealTimeTranscription接收到持续语音识别的文本。
//            Log.e(getClass().getSimpleName(),"语音识别内容: " + partialResults.getString("data"));
//        }
//
//        @Override
//        public void onError(int error, String errorMessage) {
//            // 识别发生错误后调用该接口。
//            // 从MLSpeechRealTimeTranscription接收到持续语音识别的文本。
//            Log.e(getClass().getSimpleName(),"语音识别发生错误: " + errorMessage);
//        }
//
//        @Override
//        public void onState(int state,Bundle params) {
//            // 通知应用状态发生改变。
//        }
//    }
//
//    private void requestCameraPermission() {
//        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.RECORD_AUDIO)) {
//            ActivityCompat.requestPermissions(this.getActivity(), permissions, AccoAddFragment.AUDIO_PERMISSION_CODE);
//            return;
//        }
//    }

//    // Permission application callback.
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode != AccoAddFragment.AUDIO_PERMISSION_CODE) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            return;
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer!= null) {
            mSpeechRecognizer.destroy();
        }
    }
}
