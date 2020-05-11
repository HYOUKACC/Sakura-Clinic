package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Setter@Getter
public class StaffInfo {

    private BigInteger staffId;
    private String password;
    private String name;
    private String nameCode;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date entryDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leaveDate;
    private StaffDepartments staffDepartment; // 科室
    //private StaffPosition staffPosition; // 角色
    //private StaffAuthority staffAuthority; // 权限
    private String status;

}
