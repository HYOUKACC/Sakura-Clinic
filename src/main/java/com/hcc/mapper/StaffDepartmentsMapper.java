package com.hcc.mapper;

import com.hcc.pojo.StaffDepartments;

import java.util.List;

public interface StaffDepartmentsMapper {

    // 检索对外科室
    public List<StaffDepartments> selectOutDepart();

    // 检索所有科室
    public List<StaffDepartments> selectAllDepart();

}
