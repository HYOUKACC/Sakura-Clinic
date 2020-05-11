package com.hcc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcc.mapper.StaffDepartmentsMapper;
import com.hcc.mapper.StaffInfoMapper;
import com.hcc.pojo.StaffDepartments;
import com.hcc.pojo.StaffInfo;
import com.hcc.util.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class StaffInfoSreviceImp implements StaffInfoService {

    @Autowired
    private StaffInfoMapper staffInfoMapper;

    @Autowired
    private StaffDepartmentsMapper staffDepartmentsMapper;

    // 查询所有医生
    @Override
    public PageInfo<StaffInfo> selectAllStaff(Integer page, Integer  limit, StaffInfo staffInfo) {

        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        // 搜索列表
        List<StaffInfo> staffInfos=staffInfoMapper.selectAllStaff(staffInfo);

        PageInfo<StaffInfo> staffInfoPageInfo=new PageInfo<>(staffInfos);

        return staffInfoPageInfo;
    }

    // 根据科室查询医生
    @Override
    public List<StaffInfo> selectStaffDep(String departmentId) {
        List<StaffInfo> staffInfos=staffInfoMapper.selectStaffDep(departmentId);
        return staffInfos;
    }


    // 根据用户名查询用户信息
    @Override
    public StaffInfo selectStaffByName(String staffName) {

        StaffInfo staffInfo=staffInfoMapper.selestStaffByNmae(staffName);

        return staffInfo;
    }

    // 根据用户id，查询用户角色
    @Override
    public List<String> selectPositionByStaffId(BigInteger staffId) {

        List<String> positions=staffInfoMapper.selectPositionByStaffId(staffId);
        return positions;
    }


    // 根据当前用户的id，查询用户所拥有的权限
    @Override
    public List<String> selectAuthByStaffId(BigInteger staffId) {

        List<String> authoritys=staffInfoMapper.selectAuthByStaffId(staffId);
        return authoritys;
    }


    // 锁定用户
    public boolean setStatusLock(StaffInfo staffInfo){
        int i=staffInfoMapper.setStatusLock(staffInfo);
        if(i==1){
            return true;
        }else {
            return false;
        }
    }

    // 解锁用户,激活
    public boolean setStatusUnlock(StaffInfo staffInfo){
        int i=staffInfoMapper.setStatusUnlock(staffInfo);
        if(i==1){
            return true;
        }else {
            return false;
        }
    }

    // 修改用户密码(默认)
    public boolean defaultPassword(StaffInfo staffInfo){
        Encrypt encrypt=new Encrypt();
        staffInfo.setPassword(encrypt.registerEncryptPassword("123456"));
        int i=staffInfoMapper.defaultPassword(staffInfo);
        if(i==1){
        return true;
        }else {
            return false;
        }
    }

    // 修改密码（主动）
    public boolean selfPassword(StaffInfo staffInfo){
        Encrypt encrypt=new Encrypt();
        staffInfo.setPassword(encrypt.registerEncryptPassword(staffInfo.getPassword()));
        int i=staffInfoMapper.selfPassword(staffInfo);
        if(i==1){
            return true;
        }else {
            return false;
        }
    }

    // 修改信息(管理员)
    public boolean updateStaffInfo(StaffInfo staffInfo){
        int i=staffInfoMapper.updateStaffInfo(staffInfo);
        if(i==1){
            return true;
        }else {
            return false;
        }
    }

    // 查询所有科室
    @Override
    public List<StaffDepartments> selectAllDep() {

        List<StaffDepartments> staffDepartments=staffDepartmentsMapper.selectAllDepart();
        return staffDepartments;
    }

    // 员工管理查询用户的角色
    @Override
    public List<Map<String, String>> selectPositionUser(BigInteger staffId) {

        List<Map<String,String>> mapList=staffInfoMapper.selectPositionUser(staffId);

        return mapList;
    }

    // 员工管理删除当前员工角色
    @Override
    public int deletePositionUser(BigInteger staffId, BigInteger positionId) {

        int i=staffInfoMapper.deletePositionUser(staffId,positionId);

        return i;
    }


    // 员工管理查询当前员工没有哪些角色
    @Override
    public List<Map<String, String>> selectpositionNot(BigInteger staffId) {

        List<Map<String,String>> mapList=staffInfoMapper.selectNoPositionById(staffId);

        return mapList;
    }

    // 为员工添加角色
    @Override
    public boolean addPositionUser(BigInteger staffId, BigInteger positionId) {

        int i=staffInfoMapper.addPositionUser(staffId,positionId);
        if(i==1){
            return true;
        }
        return false;
    }

    // 员工注册申请
    @Override
    public boolean registerStaffInfo(StaffInfo staffInfo, BigInteger departmentId) {

        StaffDepartments staffDepartments=new StaffDepartments();
        Encrypt encrypt=new Encrypt();
        staffInfo.setPassword(encrypt.registerEncryptPassword(staffInfo.getPassword()));
        staffDepartments.setDepartmentId(departmentId);
        staffInfo.setStaffDepartment(staffDepartments);
        staffInfo.setStatus("待激活");
        int i=staffInfoMapper.registerStaffInfo(staffInfo);
        if(i==1){
            return true;
        }

        return false;
    }

    // 显示待激活用户
    @Override
    public PageInfo<StaffInfo> staffVerifyTableSer(Integer page, Integer  limit) {

        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        // 搜索列表
        List<StaffInfo> staffInfos=staffInfoMapper.selectNoActivateStaff();

        PageInfo<StaffInfo> staffInfoPageInfo=new PageInfo<>(staffInfos);

        return staffInfoPageInfo;
    }

    // 激活用户
    @Override
    public boolean activateStaffSer(BigInteger staffId) {

        int i=staffInfoMapper.updateActivateStaff(staffId);
        if(i==1){
            return true;
        }
        return false;
    }

    // 当前系统的角色
    @Override
    public List<Map<String, String>> selectNowPosition() {

        List<Map<String, String>> maps=staffInfoMapper.selectNowPosition();

        return maps;
    }


    // 当前角色用于的权限
    @Override
    public List<Map<String, String>> selectNowPositionAuthority(BigInteger positionId) {

        List<Map<String, String>> mapList=staffInfoMapper.selectNowPositionAuthority(positionId);
        return mapList;
    }

    // 当前角色没有的权限
    @Override
    public List<Map<String, String>> selectNowPositionAuthorityNot(BigInteger positionId) {

        List<Map<String, String>> mapList=staffInfoMapper.selectNowPositionAuthorityNot(positionId);

        return mapList;
    }

    // 添加权限
    @Override
    public boolean insertAuthorityForPositon(BigInteger authorityId, BigInteger positionId) {

        int i=staffInfoMapper.insertAuthorityForPositon(authorityId,positionId);
        if(i==1){
            return true;
        }
        return false;
    }

    // 删除权限
    @Override
    public boolean deleteAuthorityForPosition(BigInteger authorityId, BigInteger positionId) {

        int i=staffInfoMapper.deleteAuthorityForPosition(authorityId,positionId);
        if(i==1){
            return true;
        }
        return false;
    }


}
