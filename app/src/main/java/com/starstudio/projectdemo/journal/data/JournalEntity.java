package com.starstudio.projectdemo.journal.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.huawei.hms.videoeditor.sdk.p.S;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * created by sgh
 * 2021-8-9
 * "心情日记"每个日记对应的数据库表
 */
@Entity(tableName = "journal_table")
public class JournalEntity implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "post_time")
    private long postTime;  // 日记发表时间戳

    private String weather; // 天气
    private String location;  // 定位
    private String content;     // 日记文本内容
    private String month;     // 发表日记的日期
    private String week;     // 发表日及的星期

    @ColumnInfo(name = "picture_array")
    private List<String> pictureArray = new ArrayList<>();   // 日记中所包含的图片
    private String video;         // 日记中所包含的视频
    private String audio;          // 日记中所包含的音频

    @Override
    public String toString() {
        return "JournalEntity{" +
                "postTime='" + postTime + '\'' +
                ", weather='" + weather + '\'' +
                ", location='" + location + '\'' +
                ", content='" + content + '\'' +
                ", month='" + month + '\'' +
                ", week='" + week + '\'' +
                ", pictureArray=" + pictureArray +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<String> getPictureArray() {
        return pictureArray;
    }

    public void setPictureArray(List<String> pictureArray) {
        this.pictureArray = pictureArray;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}


