package com.starstudio.projectdemo.account.data;

import java.util.ArrayList;

public class AccoData {
    private String mMonth;
    private String mDay;
    private String mCount;  // 账单总支出
    private ArrayList<AccoDailyData> mDaily;    //账单每日收支详情

    public AccoData(String mMonth, String mDay, String mCount, ArrayList<AccoDailyData> mDaily) {
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mCount = mCount;
        this.mDaily = mDaily;
    }

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
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
        private String mKindDetail;   //账单详情细分类别
        private String mComment;    //账单详情备注
        private String mMoney;  //账单详情支出金额

        public AccoDailyData(String mKind, String mKindDetail, String mComment, String mMoney) {
            this.mKind = mKind;
            this.mKindDetail = mKindDetail;
            this.mComment = mComment;
            this.mMoney = mMoney;
        }

        public String getmKind() {
            return mKind;
        }

        public void setmKind(String mKind) {
            this.mKind = mKind;
        }

        public String getmKindDetail() {
            return mKindDetail;
        }

        public void setmKindDetail(String mKindDetail) {
            this.mKindDetail = mKindDetail;
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
