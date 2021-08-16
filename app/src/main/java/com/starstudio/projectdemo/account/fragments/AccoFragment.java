package com.starstudio.projectdemo.account.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.huawei.hms.image.vision.A;
import com.huawei.hms.image.vision.B;
import com.huawei.hms.videoeditor.ui.mediaeditor.menu.M;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.account.BaseFragment;
import com.starstudio.projectdemo.account.adapter.AccoAdapter;
import com.starstudio.projectdemo.account.api.AccoDao;
import com.starstudio.projectdemo.account.api.AccoDatabase;
import com.starstudio.projectdemo.account.data.AccoData;
import com.starstudio.projectdemo.account.data.AccoEntity;
import com.starstudio.projectdemo.databinding.FragmentAccoBinding;
import com.starstudio.projectdemo.databinding.FragmentAccountBinding;
import com.starstudio.projectdemo.utils.HandlerHelper;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.starstudio.projectdemo.account.fragments.AccoAddFragment.ML_ASR_CAPTURE_CODE;
import static com.starstudio.projectdemo.account.fragments.AccoAddFragment.REAL_VOICE_SUCCESS_CODE;

/**
 * 为“记账”页面的“账单”
 */
public class AccoFragment extends BaseFragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private TextView tvIncomeCount,tvDisburseCount;
    private FragmentAccoBinding binding;
    private AccoDatabase mAccoDatabase;
    private AccoDao mAccoDao;
    private  ArrayList<AccoData> mClassifyData = new ArrayList<>();
    private AccoAdapter mAccoAdapter;
    String monthIncome , monthExpend;
    private IntentFilter mIntentFilter;
    private LoaclBroadcastReceiver mLocalBroadcastReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private static final int HANDLE_INIT = 0;
    private static final int HANDLE_INSERT = 1;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case HANDLE_INIT:
                    initView();
                    break;
                case HANDLE_INSERT:
                    refreshData();
                    break;
            }
            return false;
        }
    });

    private class LoaclBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("insert".equals(action)){
                Message msg = new Message();
                msg.what = HANDLE_INSERT;
                handler.sendMessage(msg);
            }
        }
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        binding = FragmentAccoBinding.inflate(inflater, container,false);
        super.onCreateView(inflater, container,savedInstanceState);
        mView = View.inflate(getContext(), R.layout.fragment_acco,null);
        mRecyclerView = mView.findViewById(R.id.recycler_acco);
        tvIncomeCount = mView.findViewById(R.id.tv_income_count);
        tvDisburseCount = mView.findViewById(R.id.tv_disburse_count);

        mAccoAdapter = new AccoAdapter(mClassifyData);
//        binding.recyclerAcco.setAdapter(mAccoAdapter);
//        binding.recyclerAcco.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Log.e(getClass().getSimpleName(), "monthIncome + " + monthIncome);
//        Log.e(getClass().getSimpleName(), "monthExpend + " + monthExpend);
//        binding.tvIncomeCount.setText(monthIncome);
//        binding.tvDisburseCount.setText(monthExpend);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAccoAdapter);

        refreshData();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("insert");
        mLocalBroadcastReceiver = new LoaclBroadcastReceiver();
        mLocalBroadcastManager.registerReceiver(mLocalBroadcastReceiver,mIntentFilter);

//        initView();
//        return binding.getRoot();
        return mView;
    }

    private void initView() {
        Log.e(getClass().getSimpleName(), "执行了initView");
        tvIncomeCount.setText(monthIncome);
        tvDisburseCount.setText(monthExpend);
        mAccoAdapter.notifyDataSetChanged();
    }

    private void refreshData() {
        mAccoDatabase = Room.databaseBuilder(this.getContext(),AccoDatabase.class,"acco_database").build();
        mAccoDao = mAccoDatabase.getAccoDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                classifyData((ArrayList<AccoEntity>) mAccoDao.getPreAccos());
                Log.e(getClass().getSimpleName(), "执行了refreshData");
                Message msg = new Message();
                msg.what = HANDLE_INIT;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void classifyData(ArrayList<AccoEntity> data){
        mClassifyData.clear();
        String year = "", month = "", day = "";
        monthIncome = "0";
        monthExpend = "0";
        for(int i = 0; i < data.size(); i++){
//            Log.e(getClass().getSimpleName(), "year = " + year + ";" + "data.get(i).getYear() = " + data.get(i).getYear());
//            Log.e(getClass().getSimpleName(), "month = " + month + ";" + "data.get(i).getMonth()  = " + data.get(i).getMonth() );
//            Log.e(getClass().getSimpleName(), "day = " + day + ";" + "data.get(i).getDay() = " + data.get(i).getDay());
            if(year.equals(data.get(i).getYear()) && month.equals(data.get(i).getMonth()) && day.equals(data.get(i).getDay())){
                mClassifyData.get(mClassifyData.size() - 1).setmCount(OtherUtil.bigNumberOperation(mClassifyData.get(mClassifyData.size() - 1).getmCount(), data.get(i).getMoney()));
            }else{
                year = data.get(i).getYear();
                month = data.get(i).getMonth();
                day = data.get(i).getDay();
                mClassifyData.add(new AccoData(year,month,day, data.get(i).getMoney(),new ArrayList<AccoData.AccoDailyData>()));
            }
            mClassifyData.get(mClassifyData.size() - 1).getmDailyData().add(new AccoData.AccoDailyData(data.get(i).getPostTime(),data.get(i).getKind(),
                    data.get(i).getKindDetail(),
                    data.get(i).getComment(),
                    data.get(i).getMoney()));
            Log.e(getClass().getSimpleName(),"data.get(i).getMonth()的值: " + data.get(i).getMonth());
            Log.e(getClass().getSimpleName(),"OtherUtil.getSystemMonthToNumber()的值: " + OtherUtil.getSystemMonthToNumber());
            if(data.get(i).getMonth().equals(OtherUtil.getSystemMonthToNumber())){
                if(data.get(i).getMoney().charAt(0) == '-'){
                    monthExpend = OtherUtil.bigNumberOperation(monthExpend, data.get(i).getMoney().substring(1));
                }else{
                    monthIncome = OtherUtil.bigNumberOperation(monthIncome, data.get(i).getMoney());
                }
            }
        }
        year = null;
        month = null;
        day = null;
    }




    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocalBroadcastManager.unregisterReceiver(mLocalBroadcastReceiver);
//        binding = null;
    }

}
