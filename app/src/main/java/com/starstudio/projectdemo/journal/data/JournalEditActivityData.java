package com.starstudio.projectdemo.journal.data;

import java.util.ArrayList;

/**
 * created by sgh
 * 2021-8-11
 *
 * 整个添加日记的媒体内容封装在JournalEditActivityData
 * 该Data由JournalEditActivity持有
 * 相关子Fragment持有引用来共享数据
 */
public class JournalEditActivityData {
    private ArrayList<PictureWithCategory> pictures = new ArrayList<>();
    private int currentPostion = -1;
    private String videoPath = null;
    private String audioPath = null;

    public ArrayList<PictureWithCategory> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<PictureWithCategory> pictures) {
        this.pictures = pictures;
    }

    public int getCurrentPostion() {
        return currentPostion;
    }

    public void setCurrentPostion(int currentPostion) {
        this.currentPostion = currentPostion;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public static class PictureWithCategory {
        private String picturePath;
        private String category = "其他";   // 为该picture的分类结果

        public String getPicturePath() {
            return picturePath;
        }

        public void setPicturePath(String picturePath) {
            this.picturePath = picturePath;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
