package com.starstudio.projectdemo.account.fragments;

import android.Manifest;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscription;
import com.starstudio.projectdemo.Custom.EditTextWithText;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.account.api.AccoDao;
import com.starstudio.projectdemo.account.api.AccoDatabase;
import com.starstudio.projectdemo.account.data.AccoData;
import com.starstudio.projectdemo.account.data.AccoEntity;
import com.starstudio.projectdemo.account.data.KindData;
import com.starstudio.projectdemo.utils.OtherUtil;
import com.wheelpicker.DataPicker;
import com.wheelpicker.OnCascadeWheelListener;
import com.wheelpicker.OnMultiDataPickListener;
import com.wheelpicker.PickOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class AccoAddFragment extends DialogFragment implements View.OnClickListener {

    private EditTextWithText etwtKind, etwtMoney;
    private EditText etComment;
    private ImageView ivVoice, ivCancle;
    private TextView tvExpense, tvIncome, tvCancel, tvSave, tvTitle;
    private MLSpeechRealTimeTranscription mSpeechRecognizer;
    private AccoDatabase mAccoDatabase;
    private AccoDao mAccoDao;
    private LocalBroadcastManager mLocalBroadcastManager ;
    private List<Integer> mCascadeInitIndex = new ArrayList<Integer>();
    private KindData mKindDatas;
    private String mKind = "", mKindDetail = "", mYear, mMonth, mDay;
    private AccoData.AccoDailyData mAccoDeilyData = null;

    private static final int AUDIO_PERMISSION_CODE = 1;
    // Permission
    private static final String[] ALL_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    private static final int WRITE_PERMISSION_CODE = 0;
    public static final int ML_ASR_CAPTURE_CODE = 2;
    public static final String REAL_VOICE_SUCCESS = "success";
    public static final int REAL_VOICE_SUCCESS_CODE = 0;
    private boolean isExpend = true;
    private boolean  hasData = false;   //判断是点击添加按钮跳转的，还是具体账单跳转的
    private static final int HANDLE_INSERT_FINISH = 3;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case HANDLE_INSERT_FINISH:
//                    Log.e(getClass().getSimpleName(),"接收到了 HANDLE_INSERT_FINISH");
                    Intent intent = new Intent("insert");
                    mLocalBroadcastManager.sendBroadcast(intent);
                    dismiss();
                    break;
            }
            return false;
        }
    });

    public AccoAddFragment(){}

    public AccoAddFragment(AccoData.AccoDailyData data, String year, String month, String day){
        mAccoDeilyData = data;
        mYear = year;
        mMonth = month;
        mDay = day;
        mKind = data.getmKind();
        mKindDetail = data.getmKindDetail();
        hasData = true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_add_acco, container, false);
        initData();
        initView(rootView);
        setAllOnClickListener();
        setDialogCancel();

        if(hasData){
            setInitData(mAccoDeilyData, rootView);
        }


        //注册广播接收器
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext()) ;


        return rootView;
    }

    private void initData() {
        mKindDatas = new KindData(new ArrayList<KindData.KindDetailFirst>(){{
            add(new KindData.KindDetailFirst("饮食", new ArrayList<KindData.KindDetailSecond>(){{
                add(new KindData.KindDetailSecond("食堂三餐"));
                add(new KindData.KindDetailSecond("聚餐"));
                add(new KindData.KindDetailSecond("水果零食"));
                add(new KindData.KindDetailSecond("外卖"));
                add(new KindData.KindDetailSecond("外出吃饭"));
            }}));
            add(new KindData.KindDetailFirst("衣服饰品", new ArrayList<KindData.KindDetailSecond>(){{
                add(new KindData.KindDetailSecond("衣服裤子"));
                add(new KindData.KindDetailSecond("化妆饰品"));
                add(new KindData.KindDetailSecond("鞋帽包包"));
            }}));
            add(new KindData.KindDetailFirst("通讯交通", new ArrayList<KindData.KindDetailSecond>(){{
                add(new KindData.KindDetailSecond("话费"));
                add(new KindData.KindDetailSecond("网费"));
                add(new KindData.KindDetailSecond("邮费"));
                add(new KindData.KindDetailSecond("公共交通"));
                add(new KindData.KindDetailSecond("打车租车"));
                add(new KindData.KindDetailSecond("自行车"));
            }}));
            add(new KindData.KindDetailFirst("学习进修", new ArrayList<KindData.KindDetailSecond>(){{
                add(new KindData.KindDetailSecond("书籍软件"));
                add(new KindData.KindDetailSecond("学杂费"));
                add(new KindData.KindDetailSecond("文印费"));
                add(new KindData.KindDetailSecond("学习工具"));
            }}));
            add(new KindData.KindDetailFirst("休闲娱乐", new ArrayList<KindData.KindDetailSecond>(){{
                add(new KindData.KindDetailSecond("电影"));
                add(new KindData.KindDetailSecond("旅游"));
                add(new KindData.KindDetailSecond("健身"));
                add(new KindData.KindDetailSecond("其他"));
            }}));
            add(new KindData.KindDetailFirst("其他杂项", new ArrayList<KindData.KindDetailSecond>(){{
                add(new KindData.KindDetailSecond("其他"));
            }}));
        }});
        mAccoDatabase = Room.databaseBuilder(this.getContext(), AccoDatabase.class,"acco_database").build();
        mAccoDao = mAccoDatabase.getAccoDao();
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
        tvExpense = rootView.findViewById(R.id.tv_expense);
        tvIncome = rootView.findViewById(R.id.tv_income);
        tvCancel = rootView.findViewById(R.id.tv_cancel);
        tvSave = rootView.findViewById(R.id.tv_save);

        etwtKind.setLeadText("分类选择");
        etwtKind.setLeadTextSize(17);
        etwtKind.setLeadTextColor("#FF646A73");

        etwtMoney.setLeadText("￥");
        etwtMoney.setLeadTextSize(23);
        etwtMoney.setLeadTextColor("#FF646A73");
        etwtMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etwtMoney.addTextChangedListener(new TextWatcher() {
            boolean deleteLastChar;// 是否需要删除末尾

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    // 如果点后面有超过三位数值,则删掉最后一位
                    int length = s.length() - s.toString().lastIndexOf(".");
                    // 说明后面有三位数值
                    deleteLastChar = length >= 4;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    return;
                }
                if (deleteLastChar) {
                    // 设置新的截取的字符串
                    etwtMoney.setText(s.toString().substring(0, s.toString().length() - 1));
                    // 光标强制到末尾
                    etwtMoney.setSelection(etwtMoney.getText().length());
                }
                // 以小数点开头，前面自动加上 "0"
                if (s.toString().startsWith(".")) {
                    etwtMoney.setText("0" + s);
                    etwtMoney.setSelection(etwtMoney.getText().length());
                }
                deleteLastChar = false;
            }
        });
        etwtMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText1 = (EditText) v;
                // 以小数点结尾，去掉小数点
                if (!hasFocus && editText1.getText() != null && editText1.getText().toString().endsWith(".")) {
                    etwtMoney.setText(editText1.getText().subSequence(0, editText1.getText().length() - 1));
                    etwtMoney.setSelection(editText1.getText().length());
                }
            }
        });

        SpannableString ss = new SpannableString("     添加备注");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etComment.setHint(new SpannedString(ss));

    }

    private void setAllOnClickListener(){
        ivVoice.setOnClickListener(this);
        ivCancle.setOnClickListener(this);
        tvExpense.setOnClickListener(this);
        tvIncome.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        etwtKind.setOnClickListener(this);
    }

    private void setDialogCancel(){
        Dialog dialog = getDialog();
        //设置点击半透明区域, 无法关闭FragmentDialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    private void setInitData(AccoData.AccoDailyData data, View rootView){
        tvTitle = rootView.findViewById(R.id.tv_title);

        if(data.getmMoney().charAt(0) == '-'){
            isExpend = true;
            tvExpense.setBackground(getContext().getDrawable(R.drawable.btn_acco_add_press));
            tvIncome.setBackground(getContext().getDrawable(R.drawable.btn_selector_acco_add));
            etwtMoney.setText(data.getmMoney().substring(1));
        }else{
            isExpend = false;
            tvExpense.setBackground(getContext().getDrawable(R.drawable.btn_selector_acco_add));
            tvIncome.setBackground(getContext().getDrawable(R.drawable.btn_acco_add_press));
            etwtMoney.setText(data.getmMoney());
        }
        tvTitle.setText("修改账单");
        etwtKind.setText(data.getmKind() + " > " + data.getmKindDetail());
        etComment.setText(" " + data.getmComment());
        tvCancel.setText("删除");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_voice_add:
                startRealTimeVoice();
//                Toast.makeText(this.getContext(), "点击了语音按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_cancel:
                dismiss();
//                Toast.makeText(this.getContext(), "点击了叉叉按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_expense:
                isExpend = true;
                tvExpense.setBackground(getContext().getDrawable(R.drawable.btn_acco_add_press));
                tvIncome.setBackground(getContext().getDrawable(R.drawable.btn_selector_acco_add));
                break;
            case R.id.tv_income:
                isExpend = false;
                tvExpense.setBackground(getContext().getDrawable(R.drawable.btn_selector_acco_add));
                tvIncome.setBackground(getContext().getDrawable(R.drawable.btn_acco_add_press));
                break;
            case R.id.tv_cancel:
                if(!hasData){
                    dismiss();
                }else{
                    deleteData();
                }
                break;
            case R.id.tv_save:
                if(!hasData){
                    saveData();
                }else{
                    updateData();
                }
                break;
            case R.id.et_kind:
                pickKind();
//                Toast.makeText(getContext(), "进行种类选择", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void pickKind(){
        PickOption option = PickOption.getPickDefaultOptionBuilder(getContext())
                .setLeftTitleColor(0xFF1233DD)
                .setRightTitleColor(0xFF1233DD)
                .setMiddleTitleColor(0xFF333333)
                .setTitleBackground(0XFFDDDDDD)
                .setLeftTitleText("取消")
                .setRightTitleText("确定")
                .setMiddleTitleText("请选择种类")
                .setFlingAnimFactor(0.4f)
                .setVisibleItemCount(7)
                .setItemTextSize(getContext().getResources().getDimensionPixelSize(com.wheelpicker.R.dimen.font_36px))
                .setItemLineColor(0xFF558800)
                .build();

        DataPicker.pickData(getContext(), mCascadeInitIndex, getPickKindData(mCascadeInitIndex), option,
                new OnMultiDataPickListener() {
                    @Override
                    public void onDataPicked(List indexArr, List val, List data) {
                        mKind = val.get(0).toString();
                        mKindDetail = val.get(1).toString();
                        etwtKind.setText(mKind + " > " + mKindDetail);
//                        Log.e(getClass().getSimpleName(), "回调函数: indexArr = " + indexArr.toString());
//                        Log.e(getClass().getSimpleName(), "回调函数: val = " + val.toString());
//                        Log.e(getClass().getSimpleName(), "回调函数: val.get(0) = " + val.get(0));
//                        Log.e(getClass().getSimpleName(), "回调函数: val.get(1) = " + val.get(1));
//                        Log.e(getClass().getSimpleName(), "回调函数: data" + data.toString());
                    }
                }, new OnCascadeWheelListener<List<?>>() {
                    @Override
                    public List<?> onCascade(int wheelIndex, List<Integer> itemIndex) {
                        //级联数据
                            return mKindDatas.kinds.get(itemIndex.get(0)).kindSeconds;
                    }
                });
    }

    private List<List<?>> getPickKindData(List<Integer> indexArr){
        int secondIndex = 0;
        if (indexArr != null && !indexArr.isEmpty()) {
            secondIndex = indexArr.get(0);
        }
        List<List<?>> pickDataKindList = new ArrayList<List<?>>(){{
            add(mKindDatas.kinds);
        }};
        pickDataKindList.add(mKindDatas.kinds.get(secondIndex).kindSeconds);

        return pickDataKindList;
    }

    private void saveData(){
        final String kind = mKind;
        final String kindDetail = mKindDetail;
        final String money = String.valueOf(etwtMoney.getText()).trim();
        final String comment = String.valueOf(etComment.getText()).trim();

        if(kind.equals("")){
            etwtKind.setHint("        请选择种类");
            etwtKind.setHintTextColor(Color.parseColor("#E8FF3E3E"));
            return;
        }else if(money.equals("")){
            etwtMoney.setHint("                  请填写金额");
            etwtMoney.setHintTextColor(Color.parseColor("#E8FF3E3E"));
            return;
        }else if(!OtherUtil.isStringToNum(money)){
            etwtMoney.setText("");
            etwtMoney.setHint("                请填写正确金额");
            etwtMoney.setHintTextColor(Color.parseColor("#E8FF3E3E"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isExpend){
                    mAccoDao.insertAccos(new AccoEntity(System.currentTimeMillis(),
                            OtherUtil.getSystemYear(),OtherUtil.getSystemMonthToNumber(),OtherUtil.getSystemDay(),
                            kind,kindDetail,comment,"-" + money));
                }else{
                    mAccoDao.insertAccos(new AccoEntity(System.currentTimeMillis(),
                            OtherUtil.getSystemYear(),OtherUtil.getSystemMonthToNumber(),OtherUtil.getSystemDay(),
                            kind,kindDetail,comment,money));
                }

                Message msg = new Message();
                msg.what = HANDLE_INSERT_FINISH;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void deleteData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                    mAccoDao.deleteAccos(new AccoEntity(mAccoDeilyData.getPostTime(),
                           "","",
                            "", "", "","",""));

                Message msg = new Message();
                msg.what = HANDLE_INSERT_FINISH;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void updateData(){
        final String kind = mKind;
        final String kindDetail = mKindDetail;
        final String money = String.valueOf(etwtMoney.getText()).trim();
        final String comment = String.valueOf(etComment.getText()).trim();

        if(kind.equals("")){
            etwtKind.setHint("        请选择种类");
            etwtKind.setHintTextColor(Color.parseColor("#E8FF3E3E"));
            return;
        }else if(money.equals("")){
            etwtMoney.setHint("                  请填写金额");
            etwtMoney.setHintTextColor(Color.parseColor("#E8FF3E3E"));
            return;
        }else if(!OtherUtil.isStringToNum(money)){
            etwtMoney.setText("");
            etwtMoney.setHint("                请填写正确金额");
            etwtMoney.setHintTextColor(Color.parseColor("#E8FF3E3E"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isExpend){
                    mAccoDao.updateAccos(new AccoEntity(mAccoDeilyData.getPostTime(),
                            mYear, mMonth,mDay,
                            kind,kindDetail,comment,"-" + money));
                }else{
                    mAccoDao.updateAccos(new AccoEntity(mAccoDeilyData.getPostTime(),
                            mYear, mMonth,mDay,
                            kind,kindDetail,comment,money));
                }

                Message msg = new Message();
                msg.what = HANDLE_INSERT_FINISH;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void startRealTimeVoice() {
        //                requestCameraPermission();
//                speechRecognizer();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
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
                etComment.setText(" " + etComment.getText() + text);
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
