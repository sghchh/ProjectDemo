package com.starstudio.projectdemo.todo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;

/**
 * created by sgh
 * 2021-8-12
 * "待办事项"对应的Data Access Object
 */
@Dao
public interface TodoDao {

    @Insert
    Completable insert(TodoEntity data);

    @Delete
    Completable delete(TodoEntity data);

    @Update
    Completable update(TodoEntity nData);

    @Query("select * from todo_table order by rowid desc")
    Flowable<List<TodoEntity>> loadAllTodo();
}
