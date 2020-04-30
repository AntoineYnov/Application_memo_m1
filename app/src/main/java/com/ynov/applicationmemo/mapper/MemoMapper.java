package com.ynov.applicationmemo.mapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ynov.applicationmemo.dto.MemoDTO;

public class MemoMapper {
    @SerializedName("form")
    @Expose
    private MemoDTO memoDTO;

    public MemoDTO getMemoDTO() {
        return memoDTO;
    }

    public void setMemoDTO(MemoDTO form) {
        this.memoDTO = form;
    }
}
