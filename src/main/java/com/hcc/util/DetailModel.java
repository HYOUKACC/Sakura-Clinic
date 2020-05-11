package com.hcc.util;

import com.hcc.pojo.*;

import java.util.List;
import java.util.Map;

public class DetailModel {
    // springMVC 数据接收模型

    private MedicalRecord medicalRecordDetail;

    private Map<String,String> medicalRecordCheck;

    private PatientInfo patientInfoDetail;

    private List<WPrescribe> wMedicineItemDetail;

    private List<CPrescribe> cMedicineItemDetail;

    private List<Project> projectDetail;




    public List<WPrescribe> getwMedicineItemDetail() {
        return wMedicineItemDetail;
    }

    public void setwMedicineItemDetail(List<WPrescribe> wMedicineItemDetail) {
        this.wMedicineItemDetail = wMedicineItemDetail;
    }

    public List<CPrescribe> getcMedicineItemDetail() {
        return cMedicineItemDetail;
    }

    public void setcMedicineItemDetail(List<CPrescribe> cMedicineItemDetail) {
        this.cMedicineItemDetail = cMedicineItemDetail;
    }

    public List<Project> getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(List<Project> projectDetail) {
        this.projectDetail = projectDetail;
    }


    public PatientInfo getPatientInfoDetail() {
        return patientInfoDetail;
    }

    public void setPatientInfoDetail(PatientInfo patientInfoDetail) {
        this.patientInfoDetail = patientInfoDetail;
    }


    public MedicalRecord getMedicalRecordDetail() {
        return medicalRecordDetail;
    }

    public void setMedicalRecordDetail(MedicalRecord medicalRecordDetail) {
        this.medicalRecordDetail = medicalRecordDetail;
    }

    public Map<String, String> getMedicalRecordCheck() {
        return medicalRecordCheck;
    }

    public void setMedicalRecordCheck(Map<String, String> medicalRecordCheck) {
        this.medicalRecordCheck = medicalRecordCheck;
    }
}
