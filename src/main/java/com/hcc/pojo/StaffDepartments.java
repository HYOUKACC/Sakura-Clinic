package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class StaffDepartments {

    private BigInteger departmentId; // 科室id
    private String departmentName; // 科室名
    private String departmentAuthority; // 科室对外权限

}
