package com.hcc.mapper;

import com.hcc.pojo.PatientInfo;

import java.util.List;

public interface PatientInfoMapper {

    // 查询患者信息
    public List<PatientInfo> selectPatient(PatientInfo patientInfo);

    // 登记患者
    public int savePatient(PatientInfo patientInfo);

    // 更新患者信息
    public int updatePrePatient(PatientInfo patientInfo);



}
