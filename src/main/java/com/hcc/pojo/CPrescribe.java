package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
public class CPrescribe {

    private BigInteger id;

    private MedicalRecord medicalRecord;

    private MedicineItem medicineItem;

    private String medicinesName;

    private String batchNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private String sourceArea;

    private String dosageForm;

    private String saleUnit;

    private String cDirection;

    private double singleDose;

    private String frequency;

    private int duration;

    private double number;

    private double price;

    private String cPrescribeInfo;

    private int isSend;

    private int isReturn;

    private String cPrescribeStatus;

    public String getcDirection() {
        return cDirection;
    }

    public void setcDirection(String cDirection) {
        this.cDirection = cDirection;
    }

    public String getcPrescribeInfo() {
        return cPrescribeInfo;
    }

    public void setcPrescribeInfo(String cPrescribeInfo) {
        this.cPrescribeInfo = cPrescribeInfo;
    }

    public String getcPrescribeStatus() {
        return cPrescribeStatus;
    }

    public void setcPrescribeStatus(String cPrescribeStatus) {
        this.cPrescribeStatus = cPrescribeStatus;
    }


    /*
    * medicinesName: "白术"
      medicinesSpec: "1g"
      dosageForm: "饮片"
      saleUnit: "g"
      cDirection: ""
      singleDose: ""
      frequency: ""
      duration: ""
      number: ""
      price: 2
      cPrescribeInfo: ""
    * */

}
