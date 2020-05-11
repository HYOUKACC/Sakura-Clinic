package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
public class WPreBatch {

    private BigInteger id;

    private BigInteger wPrescribeId;

    private BigInteger medicinesId;

    private String medicinesBarcode;

    private String medicinesName;

    private String batchNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private int sendNumber;

    private int nowReturnNumber; // 临时存储数据

    private int returnNumber;

    private int isSend;

    private int isReturn;

    private String wbStatus;

    public BigInteger getwPrescribeId() {
        return wPrescribeId;
    }

    public void setwPrescribeId(BigInteger wPrescribeId) {
        this.wPrescribeId = wPrescribeId;
    }
}
