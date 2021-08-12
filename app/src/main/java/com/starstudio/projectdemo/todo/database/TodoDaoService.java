package com.starstudio.projectdemo.todo.database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * creted by sgh
 * 2021-8-12
 * 为todo页面提供数据库服务
 */
public class TodoDaoService {
    private TodoDatabase database = TodoDatabase.getInstance();

    private TodoDaoService() {
    }

    private static volatile TodoDaoService INSTANCE;

    public static synchronized TodoDaoService getInstance() {
        if (INSTANCE == null) {
            synchronized (TodoDaoService.class) {
                INSTANCE = new TodoDaoService();
            }
        }
        return INSTANCE;
    }

    public Completable insert(TodoEntity data) {
        return database.todoDAO().insert(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delte(TodoEntity data) {
        return database.todoDAO().delete(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable update(TodoEntity nData) {
        return database.todoDAO().delete(nData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<TodoEntity>> loadAllTodo() {
        return database.todoDAO().loadAllTodo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
