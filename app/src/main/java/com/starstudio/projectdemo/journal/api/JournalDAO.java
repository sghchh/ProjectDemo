package com.starstudio.projectdemo.journal.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.starstudio.projectdemo.journal.data.JournalEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * created by sgh
 * 2021-8-9
 * 操作JournalEntity的DAO接口
 */
@Dao
public interface JournalDAO {
    @Insert
    Completable insertJournal(JournalEntity jounal);     // 插入日记的方法

    @Delete
    Completable deleteJournal(JournalEntity journalEntity);     // 根据主键删除实体

    @Query("select * from journal_entity")
    Flowable<List<JournalEntity>> loadAllJournal();
}
