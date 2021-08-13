package com.starstudio.projectdemo.account.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.common.util.Objects;
import com.huawei.hms.image.vision.B;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.account.KeyboardStatusDetectorUtil;
import com.starstudio.projectdemo.account.data.BudgetData;
import com.starstudio.projectdemo.utils.OtherUtil;
import com.starstudio.projectdemo.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.view.KeyEvent.KEYCODE_FORWARD_DEL;
import static android.view.KeyEvent.keyCodeFromString;

/**
 *  “记账”页面“预算”板块的适配器
 */
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetHolder> {

    private ArrayList<BudgetData> mBudgetData;
    private KeyboardStatusDetectorUtil mKeyboardStatusDetectorUtil;

    public BudgetAdapter(ArrayList<BudgetData> data,  KeyboardStatusDetectorUtil keyboardStatusDetectorUtil){
        mBudgetData = data;
        mKeyboardStatusDetectorUtil = keyboardStatusDetectorUtil;
    }

    @NonNull
    @NotNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item,parent,false);
        return new BudgetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BudgetAdapter.BudgetHolder holder, int position) {
        holder.loadData(mBudgetData.get(position), holder.itemView, mKeyboardStatusDetectorUtil);
    }

    @Override
    public int getItemCount() {
        return mBudgetData.size();
    }

    static class BudgetHolder extends RecyclerView.ViewHolder{
        private final TextView tvKind,  tvBalance;
        private final EditText etBudget;
        private SharedPreferencesUtils mSharedPreferencesUtils;

        public BudgetHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mSharedPreferencesUtils = SharedPreferencesUtils.getInstance(itemView.getContext());
            tvKind = itemView.findViewById(R.id.tv_kind);
            etBudget = itemView.findViewById(R.id.et_budget);
            tvBalance = itemView.findViewById(R.id.tv_balance);

            etBudget.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            etBudget.addTextChangedListener(new TextWatcher() {
//                boolean deleteLastChar;// 是否需要删除末尾
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (s.toString().contains(".")) {
//                        // 如果点后面有超过三位数值,则删掉最后一位
//                        int length = s.length() - s.toString().lastIndexOf(".");
//                        // 说明后面有三位数值
//                        deleteLastChar = length >= 4;
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (s == null) {
//                        return;
//                    }
//                    if (deleteLastChar) {
//                        // 设置新的截取的字符串
//                        etBudget.setText(s.toString().substring(0, s.toString().length() - 1));
//                        // 光标强制到末尾
//                        etBudget.setSelection(etBudget.getText().length());
//                    }
//                    // 以小数点开头，前面自动加上 "0"
//                    if (s.toString().startsWith(".")) {
//                        etBudget.setText("0" + s);
//                        etBudget.setSelection(etBudget.getText().length());
//                    }
//                    deleteLastChar = false;
//
//
//                }
//            });


        }

        private void setTvBalanceText(String text){
            if(text.charAt(0) == '-'){
                tvBalance.setTextColor(Color.parseColor("#ABFF3E3E"));
            }else{
                tvBalance.setTextColor(Color.parseColor("#FF646A73"));
            }
            tvBalance.setText(text);
        }

        private void loadData(BudgetData data, View itemView, KeyboardStatusDetectorUtil keyboardStatusDetectorUtil){
            tvKind.setText(data.getKind());
            etBudget.setText(data.getBudget());
            setTvBalanceText(data.getBalance());
//            if(data.getBalance().charAt(0) == '-'){
//                tvBalance.setTextColor(Color.parseColor("#ABFF3E3E"));
//            }
//            tvBalance.setText(data.getBalance());


            etBudget.addTextChangedListener(new TextWatcher() {
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
                        etBudget.setText(s.toString().substring(0, s.toString().length() - 1));
                        // 光标强制到末尾
                        etBudget.setSelection(etBudget.getText().length());
                    }
                    // 以小数点开头，前面自动加上 "0"
                    if (s.toString().startsWith(".")) {
                        etBudget.setText("0" + s);
                        etBudget.setSelection(etBudget.getText().length());
                    }
                    deleteLastChar = false;

                    switch (data.getKind()){
                        case "饮食":
                            mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString(), etBudget.getText().toString());
                            break;
                        case "学习进修":
                            mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString(), etBudget.getText().toString());
                            break;
                        case "衣服饰品":
                            mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString(), etBudget.getText().toString());
                            break;
                        case "通讯交通":
                            mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString(), etBudget.getText().toString());
                            break;
                        case "休闲娱乐":
                            mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString(), etBudget.getText().toString());
                            break;
                        case "其他项目":
                            mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString(), etBudget.getText().toString());
                            break;
                    }
                    Log.e(getClass().getSimpleName(), "etBudget中输入的内容为: " + etBudget.getText());
                    if(etBudget.getText().length() == 0){
                        setTvBalanceText(OtherUtil.bigNumberOperation(OtherUtil.bigNumberOperation("-" + data.getBudget(), "0"), tvBalance.getText().toString()));
                    }else if(!etBudget.getText().equals(data.getBudget())){
                        setTvBalanceText(OtherUtil.bigNumberOperation(OtherUtil.bigNumberOperation("-" + data.getBudget(), etBudget.getText().toString()), tvBalance.getText().toString()));
                    }
//                        tvBalance.setText();
                    Log.e(getClass().getSimpleName(), "tvBalance中修改后的内容为: " + tvBalance.getText().toString());
                    data.setBudget(etBudget.getText().toString());

                }
            });


//            new KeyboardStatusDetectorUtil()
//                    .registerView(itemView);



//            keyboardStatusDetectorUtil
//                    .registerView(itemView)
//                    .setmVisibilityListener(new KeyboardStatusDetectorUtil.KeyboardVisibilityListener() {
//                @Override
//                public void onVisibilityChanged(boolean keyboardVisible) {
////                    Toast.makeText(itemView.getContext(), "键盘收起了", Toast.LENGTH_SHORT).show();
//                    switch (data.getKind()){
//                            case "饮食":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString(), etBudget.getText().toString());
//                                break;
//                            case "学习进修":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString(), etBudget.getText().toString());
//                                break;
//                            case "衣服饰品":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString(), etBudget.getText().toString());
//                                break;
//                            case "通讯交通":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString(), etBudget.getText().toString());
//                                break;
//                            case "休闲娱乐":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString(), etBudget.getText().toString());
//                                break;
//                            case "其他项目":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString(), etBudget.getText().toString());
//                                break;
//                        }
//                        Log.e(getClass().getSimpleName(), "etBudget中输入的内容为: " + etBudget.getText());
//                        if(etBudget.getText().equals("")){
//                            setTvBalanceText(OtherUtil.bigNumberOperation(OtherUtil.bigNumberOperation("-" + data.getBudget(), "0"), tvBalance.getText().toString()));
//                        }else if(!etBudget.getText().equals(data.getBudget())){
//                            setTvBalanceText(OtherUtil.bigNumberOperation(OtherUtil.bigNumberOperation("-" + data.getBudget(), etBudget.getText().toString()), tvBalance.getText().toString()));
//                        }
////                        tvBalance.setText();
//                        Log.e(getClass().getSimpleName(), "tvBalance中修改后的内容为: " + tvBalance.getText());
//                        data.setBudget(etBudget.getText().toString());
//                }
//            });


//            etBudget.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                    Log.e(getClass().getSimpleName(), "收起键盘的Keycode为: " + keyCode);
//
////                    keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_RIGHT
////                            || keyCode ==  KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT
//
//                    //当点击键盘回车时，隐藏光标和键盘，同时保存修改后的内容
//                    if(keyCode < KeyEvent.KEYCODE_0 && keyCode > KeyEvent.KEYCODE_9 &&
//                            keyCode != KeyEvent.KEYCODE_FORWARD_DEL && keyCode != KeyEvent.KEYCODE_PERIOD || keyCode == EditorInfo.IME_ACTION_NEXT){
//                        Log.e(getClass().getSimpleName(), "判断成功, 进去方法体中");
//                        etBudget.clearFocus();
//                        InputMethodManager manager = (InputMethodManager)itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        manager.hideSoftInputFromWindow(etBudget.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        switch (data.getKind()){
//                            case "饮食":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString(), etBudget.getText().toString());
//                                break;
//                            case "学习进修":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString(), etBudget.getText().toString());
//                                break;
//                            case "衣服饰品":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString(), etBudget.getText().toString());
//                                break;
//                            case "通讯交通":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString(), etBudget.getText().toString());
//                                break;
//                            case "休闲娱乐":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString(), etBudget.getText().toString());
//                                break;
//                            case "其他项目":
//                                mSharedPreferencesUtils.putString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString(), etBudget.getText().toString());
//                                break;
//                        }
//                        Log.e(getClass().getSimpleName(), "etBudget中输入的内容为: " + etBudget.getText());
//                        setTvBalanceText(OtherUtil.bigNumberOperation(OtherUtil.bigNumberOperation("-" + data.getBudget(), etBudget.getText().toString()), tvBalance.getText().toString()));
////                        tvBalance.setText();
//                        Log.e(getClass().getSimpleName(), "tvBalance中修改后的内容为: " + tvBalance.getText());
//                        data.setBudget(etBudget.getText().toString());
//                    }
//                    return false;
//                }
//            });

        }
    }

}
