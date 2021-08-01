package com.starstudio.projectdemo.journal.data;

import com.starstudio.projectdemo.R;

/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面“日记”板块每一条日记对应的Java类型
 */
public class JourData {
    private String week;  // 日记发表的星期
    private String date; // 日记发表的日期
    private String weather;  // 日记发表时的天气信息
    private String loaction;  // 日记发表时的位置信息
    private String content;  // 日记的文本内容
    private String[] imgs; // 日记的所有图片内容

    public JourData(String week, String date, String weather, String loaction, String content, String[] imgs) {
        this.week = week;
        this.date = date;
        this.weather = weather;
        this.loaction = loaction;
        this.content = content;
        this.imgs = imgs;
    }

    public String getWeek() {
        return week;
    }

    public String getDate() {
        return date;
    }

    public String getWeather() {
        return weather;
    }

    public String getLoaction() {
        return loaction;
    }

    public String getContent() {
        return content;
    }

    public String[] getImgs() {
        return imgs;
    }

    public static JourData[] testData = new JourData[] {
            new JourData("星期一", "7月31日", "晴", "四川.成都", "今天好热啊", new String[]{"test", "test", "test", "test", "test", "test", "test", "test", "test", "test"}),
            new JourData("星期一", "7月31日", "晴", "四川.成都", "今天好热啊", new String[]{"test"}),
            new JourData("星期一", "7月31日", "晴", "四川.成都", "今天好热啊", new String[]{"test", "test"}),
            new JourData("星期一", "7月31日", "晴", "四川.成都", "今天好热啊", new String[]{"test", "test", "test", "test", "test", "test", "test", "test", "test"}),
            new JourData("星期一", "7月31日", "晴", "四川.成都", "今天好热啊", new String[]{"test", "test", "test", "test", "test", "test", "test", "test", "test", "test"})
    };
}
