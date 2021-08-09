package com.starstudio.projectdemo.journal.api;

import android.content.Context;
import android.media.Image;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.starstudio.projectdemo.journal.data.JournalEntity;

/**
 * created by sgh
 * 2021-8-9
 * 用来获取JournalDAO实例的数据库
 */
@Database(entities = {JournalEntity.class}, version = 1)
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDAO journalDAO();

    private static JournalDatabase INSTANCE;
    public synchronized static void init(Context context) {
        if (INSTANCE == null) {
            synchronized (JournalDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, JournalDatabase.class, "Journal").build();
            }
        }
    }

    public static JournalDatabase getInstance() {
        if (INSTANCE == null)
            throw new RuntimeException("请先调用init(ApplicationContext)方法");
        return INSTANCE;
    }
}
