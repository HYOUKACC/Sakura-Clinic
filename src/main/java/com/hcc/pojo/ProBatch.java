package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class ProBatch {

    private BigInteger id;

    private BigInteger projectId;

    private String projectName;

    private int sendNumber;

    private int returnNumber;

    private int nowReturnNumber; // 临时存储数据

    private int isSend;

    private int isReturn;

    private String proStatus;
}
