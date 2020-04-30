package com.ynov.applicationmemo.dto;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "memo")
public class MemoDTO {

    @PrimaryKey(autoGenerate = true)
    public long courseId = 0;

    @SerializedName("memo")
    @Expose
    public String intitule;

    // Constructeur public vide (obligatoire si autre constructeur existant) :
    public MemoDTO() {}

    // Autre constructeur :
    public MemoDTO(String intitule)    {
        this.intitule = intitule;
    }

    public String getIntitule()
    {
        return intitule;
    }
}
