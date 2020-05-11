package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class Project {
    /*
    * projectName: "推拿"
      number: ""
      price: 25
      projectInfo: ""
      */
    private BigInteger id;

    private MedicalRecord medicalRecord;

    private String projectName;

    private int number;

    private double price;

    private String projectInfo;

    private int isSend;

    private int isReturn;

    private String projectStatus;
}
