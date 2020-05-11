package com.hcc.service;

import com.github.pagehelper.PageInfo;
import com.hcc.pojo.StaffDepartments;
import com.hcc.pojo.StaffInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface StaffInfoService {

    // 查询所有员工信息
    public PageInfo<StaffInfo> selectAllStaff(Integer page, Integer limit, StaffInfo staffInfo);

    // 根据科室查询医生
    public List<StaffInfo> selectStaffDep(String departmentId);

    // 根据用户名查询用户（登录认证）
    public StaffInfo selectStaffByName(String staffName);

    // 根据用户ID，查询用户角色
    public List<String> selectPositionByStaffId(BigInteger staffId);

    // 根据用户Id，查询用户所拥有的角色权限
    public List<String> selectAuthByStaffId(BigInteger staffId);


    // 锁定用户
    public boolean setStatusLock(StaffInfo staffInfo);

    // 解锁用户,激活
    public boolean setStatusUnlock(StaffInfo staffInfo);

    // 修改用户密码(默认)
    public boolean defaultPassword(StaffInfo staffInfo);

    // 修改密码（主动）
    public boolean selfPassword(StaffInfo staffInfo);

    // 修改信息(管理员)
    public boolean updateStaffInfo(StaffInfo staffInfo);

    // 查询所有科室
    public List<StaffDepartments> selectAllDep();

    // 员工管理查询用户的所有角色
    public List<Map<String,String>> selectPositionUser(BigInteger staffId);

    // 删除当前员工角色
    public int deletePositionUser(BigInteger staffId, BigInteger positionId);

    // 员工管理查询当前员工没有哪些角色
    public List<Map<String,String>> selectpositionNot(BigInteger staffId);

    // 为员工添加角色
    public boolean addPositionUser(BigInteger staffId, BigInteger positionId);

    // 员工注册申请
    public boolean registerStaffInfo(StaffInfo staffInfo, BigInteger departmentId);

    // 显示待激活用户
    public PageInfo<StaffInfo> staffVerifyTableSer(Integer page, Integer limit);

    // 激活用户
    public boolean activateStaffSer(BigInteger staffId);


    // 显示当前系统里的角色
    public List<Map<String,String>> selectNowPosition();

    // 显示当前角色用于哪些权限
    public List<Map<String,String>> selectNowPositionAuthority(BigInteger positionId);

    // 显示当前角色没有哪些权限
    public List<Map<String,String>> selectNowPositionAuthorityNot(BigInteger positionId);

    // 为当前角色添加一个权限
    public boolean insertAuthorityForPositon(BigInteger authorityId, BigInteger positionId);


    // 为当前角色删除一个权限
    public boolean deleteAuthorityForPosition(BigInteger authorityId, BigInteger positionId);

}
