package com.starstudio.projectdemo.account.data;

import java.util.ArrayList;

/**
 *  记账“记账”页面中RecyclerView的Adapter所需的数据
 *  用来封装展示每一天的账单数据
 */
public class AccoData {
    private String mYear;
    private String mMonth;
    private String mDay;
    private String mCount;  // 账单总支出
    private ArrayList<AccoDailyData> mDaily;    //账单每日收支详情

    public AccoData(String mYear, String mMonth, String mDay, String mCount, ArrayList<AccoDailyData> mDaily) {
        this.mYear = mYear;
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

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }


    public static class AccoDailyData{
        private long postTime;  // 日记发表时间戳
        private String mKind;   //账单详情类别
        private String mKindDetail;   //账单详情细分类别
        private String mComment;    //账单详情备注
        private String mMoney;  //账单详情支出金额

        public AccoDailyData(long postTime, String mKind, String mKindDetail, String mComment, String mMoney) {
            this.postTime = postTime;
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

        public long getPostTime() {
            return postTime;
        }

        public void setPostTime(long postTime) {
            this.postTime = postTime;
        }
    }

}
