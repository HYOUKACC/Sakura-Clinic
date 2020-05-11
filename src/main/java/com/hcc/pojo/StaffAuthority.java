package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Setter@Getter
public class StaffAuthority {
    private BigInteger authorityId;
    private String authorityInfo;
}
