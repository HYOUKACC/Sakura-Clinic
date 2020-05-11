package com.hcc.mapper;

import com.hcc.pojo.DosageForm;

import java.util.List;

public interface DosageFormMapper {

    // 检索剂型
    public List<DosageForm> selectDosageForm();
}
