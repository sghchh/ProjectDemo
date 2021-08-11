package com.starstudio.projectdemo.account.api;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.starstudio.projectdemo.account.data.AccoEntity;

@Database(entities = {AccoEntity.class}, version = 1, exportSchema = false)
public abstract class AccoDatabase extends RoomDatabase {
    public abstract AccoDao getAccoDao();
}
