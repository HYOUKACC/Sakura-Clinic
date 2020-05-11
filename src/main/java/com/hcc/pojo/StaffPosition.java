package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class StaffPosition {
    private BigInteger positionId;
    private String positionInfo;
    private String staffPositionC;
}
