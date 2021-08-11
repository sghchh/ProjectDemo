package com.starstudio.projectdemo.journal.api;

import com.starstudio.projectdemo.journal.data.JournalEntity;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JournalDaoService {
    private JournalDatabase database = JournalDatabase.getInstance();

    private JournalDaoService(){}
    private static volatile JournalDaoService INSTANCE;

    public static synchronized JournalDaoService getInstance() {
        if (INSTANCE == null) {
            synchronized (JournalDaoService.class) {
                INSTANCE = new JournalDaoService();
            }
        }
        return INSTANCE;
    }

    public Completable insert(JournalEntity journalEntity) {
        return database.journalDAO().insertJournal(journalEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delte(JournalEntity journalEntity) {
        return database.journalDAO().deleteJournal(journalEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable loadAll() {
        return database.journalDAO().loadAllJournal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
