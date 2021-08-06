package com.starstudio.projectdemo.account.data;

import java.util.ArrayList;

public class AccoData {
    private String mData;   // 账单日期
    private String mCount;  // 账单总支出
    private ArrayList<AccoDailyData> mDaily;    //账单每日收支详情

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public String getmCount() {
        return mCount;
    }

    public void setmCount(String mCount) {
        this.mCount = mCount;
    }

    public ArrayList<AccoDailyData> getmDailyData() {
        return mDaily;
    }

    public void setmDailyData(ArrayList<AccoDailyData> mDailyData) {
        this.mDaily = mDailyData;
    }


    public static class AccoDailyData{
        private String mKind;   //账单详情类别
        private String mComment;    //账单详情备注
        private String mMoney;  //账单详情支出金额


        public String getmKind() {
            return mKind;
        }

        public void setmKind(String mKind) {
            this.mKind = mKind;
        }

        public String getmComment() {
            return mComment;
        }

        public void setmComment(String mComment) {
            this.mComment = mComment;
        }

        public String getmMoney() {
            return mMoney;
        }

        public void setmMoney(String mMoney) {
            this.mMoney = mMoney;
        }


    }

}
