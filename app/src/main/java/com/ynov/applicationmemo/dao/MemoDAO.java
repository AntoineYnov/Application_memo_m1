package com.ynov.applicationmemo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import com.ynov.applicationmemo.dto.MemoDTO;

@Dao
public abstract  class MemoDAO {

    @Query("SELECT * FROM memo")    public abstract List<MemoDTO> getListeMemos();
    @Query("SELECT COUNT(*) FROM memo WHERE intitule = :intitule")
    public abstract long countMemosParIntitule(String intitule);
    @Insert
    public abstract void insert(MemoDTO... courses);
    @Update
    public abstract void update(MemoDTO... courses);
    @Delete
    public abstract void delete(MemoDTO... courses);

    @Transaction
    public void insertDelete(MemoDTO courseDTO1, MemoDTO courseDTO2)    {
        insert(courseDTO1);
        delete(courseDTO2);
    }

}
