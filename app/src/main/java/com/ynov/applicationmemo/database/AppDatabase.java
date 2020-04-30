package com.ynov.applicationmemo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ynov.applicationmemo.dao.MemoDAO;
import com.ynov.applicationmemo.dto.MemoDTO;

@Database(entities = {MemoDTO.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract MemoDAO memoDAO();
}
