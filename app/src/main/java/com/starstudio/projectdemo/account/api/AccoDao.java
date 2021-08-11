package com.starstudio.projectdemo.account.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.starstudio.projectdemo.account.data.AccoEntity;

import java.util.List;

@Dao
public interface AccoDao {
    @Insert
    void insertAccos(AccoEntity... accoEntities);

    @Update
    void updateAccos(AccoEntity... accoEntities);

    @Delete
    void deleteAccos(AccoEntity... accoEntities);

    @Query("SELECT * FROM AccoEntity ORDER BY POST_TIME DESC")
    List<AccoEntity> getPreAccos();

}
