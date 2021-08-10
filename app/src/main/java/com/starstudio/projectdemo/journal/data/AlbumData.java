package com.starstudio.projectdemo.journal.data;

/**
 * created by sgh
 * 2021-8-10
 * 心情日记“相册”页面中RecyclerView的Adapter所需的数据
 * 用来封装用于展示每一个相册的数据
 */
public class AlbumData {
    private String cover;   // 所选的相册封面图
    private String name;    // 相册名字

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
