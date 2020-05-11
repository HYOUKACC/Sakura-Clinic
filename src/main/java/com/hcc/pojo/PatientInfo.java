package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
public class PatientInfo {

    private BigInteger patientId;
    private String patientName;
    private String nameCode;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String address;
    private String phone;
    private String allergies;// 过敏史
    private String chronic;// 慢性病史
    private String familyMedicalHistory;// 家族病史
    private String status;// 账户状态，正常，冻结


}
