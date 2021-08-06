package com.starstudio.projectdemo.account.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.fragment.app.DialogFragment;

import com.starstudio.projectdemo.Custom.EditTextWithText;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.OtherUtil;

public class AccoAddFragment extends DialogFragment {

    private EditTextWithText etwtKind, etwtMoney;
    private EditText etComment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_add_acco, container, false);
        etwtKind = rootView.findViewById(R.id.et_kind);
        etwtMoney = rootView.findViewById(R.id.et_money);
        etComment = rootView.findViewById(R.id.et_comment);

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

}
