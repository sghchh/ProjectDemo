package com.starstudio.projectdemo.account.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AccoEntity {
    @PrimaryKey
    @ColumnInfo(name = "post_time")
    private long postTime;  // 日记发表时间戳

    private String year;
    private String month;
    private String day;
    private String kind;   //账单详情类别
    private String kindDetail;   //账单详情细分类别
    private String comment;    //账单详情备注
    private String money;  //账单详情支出金额

    public AccoEntity(long postTime, String year, String month, String day, String kind, String kindDetail, String comment, String money) {
        this.postTime = postTime;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
        this.kindDetail = kindDetail;
        this.comment = comment;
        this.money = money;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKindDetail() {
        return kindDetail;
    }

    public void setKindDetail(String kindDetail) {
        this.kindDetail = kindDetail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
