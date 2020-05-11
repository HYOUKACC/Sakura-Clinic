package com.hcc.Realm;

import com.hcc.mapper.StaffInfoMapper;
import com.hcc.pojo.StaffDepartments;
import com.hcc.pojo.StaffInfo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// 自定义检查器，包含认证和授权
public class ClinicRealm extends AuthorizingRealm {

    @Autowired
    private StaffInfoMapper staffInfoMapper;

    // 认证（登录）
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }

        // 去数据库查询用户信息，通过姓名
        String userName=authenticationToken.getPrincipal().toString();
        StaffInfo staffInfo=staffInfoMapper.selestStaffByNmae(userName);


        StaffInfo saveInfo=new StaffInfo();
        StaffDepartments staffDepartments=new StaffDepartments();

        //System.out.println("认证");

        // 判断信息
        if(staffInfo==null){
            return null;
        }else {
            // 获取数据库中的密码
            String password=staffInfo.getPassword();


            // 保存管理器中的用户信息
            saveInfo.setStaffId(staffInfo.getStaffId());// 员工号
            saveInfo.setName(staffInfo.getName());// 姓名
            saveInfo.setSex(staffInfo.getSex());// 性别

            staffDepartments.setDepartmentId(staffInfo.getStaffDepartment().getDepartmentId());
            staffDepartments.setDepartmentName(staffInfo.getStaffDepartment().getDepartmentName());

            saveInfo.setStaffDepartment(staffDepartments);// 部门

            // 进入认证 当前请求认证的用户名，数据库中的密码，盐，散列次数，当前realm名
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(saveInfo,password, ByteSource.Util.bytes("Sakura"),this.getName());
            return info;
        }


    }

    // 授权（判断权限）// 方法中有权限注解的时候就会调用，页面中有权限标签也会调用
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        System.out.println("授权调用-------------");
        StaffInfo staffInfo=(StaffInfo) principalCollection.getPrimaryPrincipal();
        System.out.println("staffInfo");

        // 查询用户角色
        List<String> positions=staffInfoMapper.selectPositionByStaffId(staffInfo.getStaffId());

        // 查询用户所拥有的权限
        List<String> authority=staffInfoMapper.selectAuthByStaffId(staffInfo.getStaffId());

        //System.out.println("用户角色:"+positions);
        //System.out.println("用户权限:"+authority);

        // 传递授权信息
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addRoles(positions);// 角色
        info.addStringPermissions(authority);//权限
        return info;
    }
}
