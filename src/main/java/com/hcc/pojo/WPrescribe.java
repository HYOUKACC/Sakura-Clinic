package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
public class WPrescribe {

    private BigInteger id;

    private MedicalRecord medicalRecord;

    private MedicineItem medicineItem;

    private String medicinesName;

    private String medicinesBarcode;

    private String medicinesSpec;

    private String medicinesUnit;

    private String dosageForm;

    private double packingSpec;

    private String saleUnit;

    private String batchNumber;

    private Date productionDate;

    private Date expiryDate;

    private String wDirection;

    private double singleDose;

    private String frequency;

    private double duration;

    private String frequencyUnit;

    private double number;

    private double price;

    private String wPrescribeInfo;

    private int isSend;

    private int isReturn;

    private String wPrescribeStatus;


    // lombok插件（第一个字母小写，第二个字母大写，无法正确填写get，set方法）
    public String getwDirection() {
        return wDirection;
    }

    public void setwDirection(String wDirection) {
        this.wDirection = wDirection;
    }

    public String getwPrescribeInfo() {
        return wPrescribeInfo;
    }

    public void setwPrescribeInfo(String wPrescribeInfo) {
        this.wPrescribeInfo = wPrescribeInfo;
    }

    public String getwPrescribeStatus() {
        return wPrescribeStatus;
    }

    public void setwPrescribeStatus(String wPrescribeStatus) {
        this.wPrescribeStatus = wPrescribeStatus;
    }


//    medicinesName: "999感冒灵"
//    medicinesBarcode: "6901339913419"
//    medicinesSpec: "14克/袋"
//    dosageForm: "配方颗粒"
//    packingSpec: 20
//    medicinesUnit: "袋"
//    saleUnit: "盒"
//    wDirection: ""
//    singleDose: ""
//    frequency: ""
//    frequencyTimes: ""
//    frequencyNumber: ""
//    duration: ""
//    frequencyUnit: ""
//    number: ""
//    price: 20
//    wPrescribeInfo: ""


}
