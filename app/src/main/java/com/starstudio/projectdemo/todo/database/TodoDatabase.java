package com.starstudio.projectdemo.todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * created by sgh
 * 2021-8-12
 * todo页面的数据库支持
 */
@Database(entities = {TodoEntity.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDAO();

    private static TodoDatabase INSTANCE;
    public synchronized static void init(Context context) {
        if (INSTANCE == null) {
            synchronized (TodoDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, TodoDatabase.class, "todo").build();
            }
        }
    }

    public static TodoDatabase getInstance() {
        if (INSTANCE == null)
            throw new RuntimeException("请先调用init(ApplicationContext)方法");
        return INSTANCE;
    }
}
