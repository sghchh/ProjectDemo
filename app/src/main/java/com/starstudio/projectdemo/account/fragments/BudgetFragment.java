package com.starstudio.projectdemo.account.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.starstudio.projectdemo.account.adapter.BudgetAdapter;
import com.starstudio.projectdemo.account.api.AccoDao;
import com.starstudio.projectdemo.account.api.AccoDatabase;
import com.starstudio.projectdemo.account.data.AccoEntity;
import com.starstudio.projectdemo.account.data.BudgetData;
import com.starstudio.projectdemo.databinding.FragmentBudgetBinding;
import com.starstudio.projectdemo.utils.OtherUtil;
import com.starstudio.projectdemo.utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 为“记账”页面的“预算”
 */
public class BudgetFragment extends Fragment {

    private FragmentBudgetBinding binding;
    private AccoDatabase mAccoDatabase;
    private AccoDao mAccoDao;
    private ArrayList<BudgetData>  mBudgetData;
    private SharedPreferencesUtils mSharedPreferencesUtils;
    private String mBudgetCount;
    private static final HashMap<String,Integer> kindToNum = new HashMap(){{
        put("饮食",0);
        put("学习进修",1);
        put("衣服饰品",2);
        put("通讯交通",3);
        put("休闲娱乐",4);
        put("其他杂项",5);
    }};
    private static final int HADNLE_INIT = 1;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case HADNLE_INIT:
                    initView();
                    break;
            }
            return false;
        }
    });

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mSharedPreferencesUtils = SharedPreferencesUtils.getInstance(getContext());
        binding = FragmentBudgetBinding.inflate(inflater,container,false);
        refreshData();
        return binding.getRoot();
    }

    private void refreshData(){
        mAccoDatabase = Room.databaseBuilder(this.getContext(),AccoDatabase.class,"acco_database").build();
        mAccoDao = mAccoDatabase.getAccoDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mBudgetData != null){
                    mBudgetData.clear();
                }
                Log.e(getClass().getSimpleName(), "测试SP存储: " + (mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString()).equals("") ? "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString())));
                mBudgetData = new ArrayList<BudgetData>(){{
                    add(new BudgetData("饮食", mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString()),
                            mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_EAT.toString())));
                    add(new BudgetData("学习进修", mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString()),
                            mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_LEARN.toString())));
                    add(new BudgetData("衣服饰品", mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString()),
                            mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_CLOTHES.toString())));
                    add(new BudgetData("通讯交通", mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString()),
                            mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_COMMUN_TRANS.toString())));
                    add(new BudgetData("休闲娱乐", mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString()),
                            mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_RELAX.toString())));
                    add(new BudgetData("其他杂项", mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString()),
                            mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString()).equals("") ?
                            "0" : mSharedPreferencesUtils.readString(SharedPreferencesUtils.Key.KEY_BUDGET_OTHER.toString())));
                }};
                mBudgetCount = "0";
                for(int i = 0; i < mBudgetData.size(); i++){
                    Log.e(getClass().getSimpleName(), "mBudgetCount的值: " + mBudgetCount);
                    Log.e(getClass().getSimpleName(), "每项的预算: " + mBudgetData.get(i).getBudget());
                    mBudgetCount = OtherUtil.bigNumberOperation(mBudgetCount, mBudgetData.get(i).getBudget());
                }
                classifyData((ArrayList<AccoEntity>) mAccoDao.getPreAccos());
                Log.e(getClass().getSimpleName(), "执行了refreshData");
                Message msg = new Message();
                msg.what = HADNLE_INIT;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void classifyData(ArrayList<AccoEntity> data){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getMonth().equals(OtherUtil.getSystemMonthToNumber()) && data.get(i).getYear().equals(OtherUtil.getSystemYear())){
                Log.e(getClass().getSimpleName(), "计算余额为中第一个数: " + mBudgetData.get(kindToNum.get(data.get(i).getKind())).getBalance());
                Log.e(getClass().getSimpleName(), "计算余额为中第二个数: " + data.get(i).getMoney());
                Log.e(getClass().getSimpleName(), "计算余额为: " + OtherUtil.bigNumberOperation(mBudgetData.get(kindToNum.get(data.get(i).getKind())).getBalance(), data.get(i).getMoney()));
                mBudgetData.get(kindToNum.get(data.get(i).getKind())).
                        setBalance(OtherUtil.bigNumberOperation(mBudgetData.get(kindToNum.get(data.get(i).getKind())).getBalance(), data.get(i).getMoney()));
            }else{
//                for(int j = 0; j < mBudgetData.size(); j++){
//                    mBudgetData.get(kindToNum.get(data.get(i).getKind())).
//                            setBalance(OtherUtil.bigNumberOperation(mBudgetData.get(kindToNum.get(data.get(i).getKind())).getBalance(),
//                                    mBudgetData.get(kindToNum.get(data.get(i).getKind())).getBudget()));
//                }
                break;
            }
        }
    }

    public void checkBudget(ArrayList<AccoEntity> data){

    }

    private void initView(){
        binding.recyclerBudget.setAdapter(new BudgetAdapter(mBudgetData));
        binding.recyclerBudget.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.tvBudgetCount.setText(mBudgetCount);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
