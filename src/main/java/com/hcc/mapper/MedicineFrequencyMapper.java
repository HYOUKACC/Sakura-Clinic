package com.hcc.mapper;

import com.hcc.pojo.MedicineFrequency;

import java.util.List;

public interface MedicineFrequencyMapper {

    // 搜索频次表
    public List<MedicineFrequency> searchFrequency(MedicineFrequency medicineFrequency);
}
