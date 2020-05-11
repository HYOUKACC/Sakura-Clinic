package com.hcc.mapper;

import com.hcc.pojo.StaffInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface StaffInfoMapper {

    // 查询所有员工
    public List<StaffInfo> selectAllStaff(StaffInfo staffInfo);


    // 根据科室查询员工
    public List<StaffInfo> selectStaffDep(String departmentId);



    // 根据医生Id查询医生表
    public List<StaffInfo> selectStaffId(BigInteger staffId);

    // 根据姓名查询用户（认证）
    public StaffInfo selestStaffByNmae(String staffName);

    // 根据用户ID查询用户角色
    public List<String> selectPositionByStaffId(BigInteger staffId);

    // 根据用户Id，查询用户所拥有的权限
    public List<String> selectAuthByStaffId(BigInteger staffId);

    // 锁定用户
    public int setStatusLock(StaffInfo staffInfo);

    // 解锁用户,激活
    public int setStatusUnlock(StaffInfo staffInfo);

    // 修改用户密码(默认)
    public int defaultPassword(StaffInfo staffInfo);

    // 修改密码（主动）
    public int selfPassword(StaffInfo staffInfo);

    // 修改信息(管理员)
    public int updateStaffInfo(StaffInfo staffInfo);

    // 查询用户没有哪些角色(员工管理)
    public List<Map<String,String>> selectNoPositionById(BigInteger staffId);

    // 查询用户的角色
    public List<Map<String,String>> selectPositionUser(BigInteger staffId);

    // 删除当前员工角色
    public int deletePositionUser(BigInteger staffId, BigInteger positionId);

    // （员工管理）为员工添加角色
    public int addPositionUser(BigInteger staffId, BigInteger positionId);

    // 员工注册申请
    public int registerStaffInfo(StaffInfo staffInfo);

    // 显示待激活用户
    public List<StaffInfo> selectNoActivateStaff();

    // 激活用户
    public int updateActivateStaff(BigInteger staffId);


    // 显示当前系统里的角色
    public List<Map<String,String>> selectNowPosition();

    // 显示当前角色用于哪些权限
    public List<Map<String,String>> selectNowPositionAuthority(BigInteger positionId);

    // 显示当前角色没有哪些权限
    public List<Map<String,String>> selectNowPositionAuthorityNot(BigInteger positionId);

    // 为当前角色添加一个权限
    public int insertAuthorityForPositon(BigInteger authorityId, BigInteger positionId);


    // 为当前角色删除一个权限
    public int deleteAuthorityForPosition(BigInteger authorityId, BigInteger positionId);

}
