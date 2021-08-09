package com.starstudio.projectdemo.journal.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.starstudio.projectdemo.journal.data.JournalEntity;

/**
 * created by sgh
 * 2021-8-9
 * 操作JournalEntity的DAO接口
 */
@Dao
public interface JournalDAO {
    @Insert
    public void insertJournal(JournalEntity jounal);     // 插入日记的方法

    @Delete
    public void deleteJournal(JournalEntity journalEntity);     // 根据主键删除实体

    @Query("select * from journal_entity")
    public JournalEntity[] loadAllJournal();
}
