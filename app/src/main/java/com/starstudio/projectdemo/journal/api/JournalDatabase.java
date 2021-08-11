package com.starstudio.projectdemo.journal.api;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.starstudio.projectdemo.journal.data.JournalEntity;
import com.starstudio.projectdemo.journal.data.PictureArrayConverter;

/**
 * created by sgh
 * 2021-8-9
 * 用来获取JournalDAO实例的数据库
 */
@Database(entities = {JournalEntity.class}, version = 1)
@TypeConverters({PictureArrayConverter.class})
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
