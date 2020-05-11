package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
public class CPreBatch {

    private BigInteger id;

    private BigInteger cPrescribeId;

    private BigInteger medicinesId;

    //private String medicinesBarchode;

    private String medicinesName;

    private String batchNumber;

    private String sourceArea;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private int isSend;

    private int isReturn;

    private int sendNumber;

    private int returnNumber;

    private int nowReturnNumber; // 临时存储数据

    private String cbStatus;

    public BigInteger getcPrescribeId() {
        return cPrescribeId;
    }

    public void setcPrescribeId(BigInteger cPrescribeId) {
        this.cPrescribeId = cPrescribeId;
    }
}
