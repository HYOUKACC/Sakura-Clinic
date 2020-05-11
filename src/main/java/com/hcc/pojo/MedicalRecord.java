package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
public class MedicalRecord {

    private BigInteger medicalRecordId;
    private PatientInfo patientInfo;
    private StaffInfo staffInfo;
    private String diagnosisTimes;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consultationDate;
    private String registerCategory;
    private double registerPrice;
    private double patientHeight;
    private double patientWeight;
    private double patientTemperature;
    private String patientBp;
    private String patientDescribe;
    private String clinicalDiagnosis;
    private int isSend;// 是否收费
    private int isReturn;// 是否退费
    private int isNeed;// 是否需要缴费
    private String registerStatus;

}
