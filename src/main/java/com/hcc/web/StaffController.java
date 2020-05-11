package com.hcc.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hcc.pojo.StaffDepartments;
import com.hcc.pojo.StaffInfo;
import com.hcc.service.StaffInfoService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Controller // 员工管理控制
public class StaffController {

    @Autowired
    private StaffInfoService staffInfoService;

    // 跳转到员工界面
//    @RequestMapping("/staffTable")
//    public String turnToStaffTable(){
//        return "/page/staffTable";
//    }


    // 加载员工表格
    @RequestMapping("/selectAllStaff")
    @ResponseBody
    public String showStaffTable(@RequestParam(required=false,defaultValue="1") Integer page,
                                 @RequestParam(required=false,defaultValue="5") Integer limit, StaffInfo staffInfo, StaffDepartments staffDepartments){

       // System.out.println(staffDepartments);
        staffInfo.setStaffDepartment(staffDepartments);

        // 调用service对象，返回分装好的PageInfo类型，已经执行好分页操作
        PageInfo<StaffInfo> pageInfo= staffInfoService.selectAllStaff(page, limit,staffInfo);

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",pageInfo.getTotal()); // 获取查询的总条数
        jsonObject.put("data", JSONArray.toJSON(pageInfo.getList())); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json

    }




    // 更加科室查询员工（挂号处使用）
    @RequestMapping("/selectStaffDep")
    @ResponseBody
    public String selectStaffD(String departmentId){

        List<StaffInfo> staffInfos=staffInfoService.selectStaffDep(departmentId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON(staffInfos)); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json

    }


    // 锁定用户
    @RequiresRoles("admin")
    @RequestMapping("/setStatusLock")
    @ResponseBody
    public String setStatusLock(StaffInfo staffInfo){

        boolean b=staffInfoService.setStatusLock(staffInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(b){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","锁定失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }

    // 解锁用户,激活
    @RequiresRoles("admin")
    @RequestMapping("/setStatusUnlock")
    @ResponseBody
    public String setStatusUnlock(StaffInfo staffInfo){
        boolean b=staffInfoService.setStatusUnlock(staffInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(b){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","激活失败");
        }

        return jsonObject.toJSONString(); // 返回json
    }

    // 修改用户密码(默认)
    @RequiresRoles("admin")
    @RequestMapping("/defaultPassword")
    @ResponseBody
    public String defaultPassword(StaffInfo staffInfo){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        boolean b=staffInfoService.defaultPassword(staffInfo);
        if(b){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","重置密码失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }

    // 修改密码（主动）

    @RequestMapping("/selfPassword")
    @ResponseBody
    public String selfPassword(StaffInfo staffInfo){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        boolean b=staffInfoService.selfPassword(staffInfo);
        if(b){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","修改密码失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }

    // 修改信息(管理员)
    @RequestMapping("/updateStaffInfo")
    @ResponseBody
    public String updateStaffInfo(StaffInfo staffInfo, BigInteger departmentId){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        StaffDepartments staffDepartments=new StaffDepartments();
        staffDepartments.setDepartmentId(departmentId);
        staffInfo.setStaffDepartment(staffDepartments);
        boolean b=staffInfoService.updateStaffInfo(staffInfo);
        if(b){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","修改失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }

    // 检索所有科室
    @RequestMapping("/selectAllDp")
    @ResponseBody
    public String selectAllDp(){

         List<StaffDepartments> staffDepartments=staffInfoService.selectAllDep();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON(staffDepartments));
        return jsonObject.toJSONString(); // 返回json
    }

    // 员工管理-查询当前员工用于的角色
    @RequestMapping("/selectAllPositionUser")
    @ResponseBody
    public String selectAllPositionUser(BigInteger staffId){
        List<Map<String,String>> mapList=staffInfoService.selectPositionUser(staffId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON( mapList));
        return jsonObject.toJSONString(); // 返回json
    }

    // 员工管理-删除当前员工角色
    @RequiresRoles("admin")
    @RequestMapping("/deletePositionUser")
    @ResponseBody
    public String deletePositionUser(BigInteger staffId,BigInteger positionId){

        int i=staffInfoService.deletePositionUser(staffId,positionId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);

        if(i==1){
            jsonObject.put("msg","success");
        }else{
            jsonObject.put("msg","删除失败");
        }
        return jsonObject.toJSONString(); // 返回json


    }

    // 根据员工id查询当前员工没有哪些角色
    @RequestMapping("/selectAllPositionUserNot")
    @ResponseBody
    public String selectAllPositionUserNot(BigInteger staffId){

        List<Map<String,String>> mapList=staffInfoService.selectpositionNot(staffId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON( mapList));
        return jsonObject.toJSONString(); // 返回json

    }

    // 为员工添加一个角色
    @RequiresRoles("admin")
    @RequestMapping("/addPositionUserDetail")
    @ResponseBody
    public String addPositionUser(BigInteger staffId,BigInteger positionId){

        boolean flag=staffInfoService.addPositionUser(staffId,positionId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);

        if(flag){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","添加角色失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }


    // 注册用户
    @RequestMapping("/registerStaffInfo")
    @ResponseBody
    public String registerStaffInfo(StaffInfo staffInfo, StaffDepartments staffDepartments){

        boolean flag=staffInfoService.registerStaffInfo(staffInfo,staffDepartments.getDepartmentId());

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(flag){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","申请失败");
        }
        return jsonObject.toJSONString(); // 返回json
        //return null;
    }


    // 显示待激活用户
    @RequestMapping("/staffVerifyTable")
    @ResponseBody
    public String staffVerifyTable(@RequestParam(required=false,defaultValue="1") Integer page,
                                   @RequestParam(required=false,defaultValue="5") Integer limit){

        PageInfo<StaffInfo> staffInfoPageInfo=staffInfoService.staffVerifyTableSer(page,limit);

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",staffInfoPageInfo.getTotal()); // 获取查询的总条数
        jsonObject.put("data", JSONArray.toJSON(staffInfoPageInfo.getList())); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json

    }

    // 激活用户
    @RequiresRoles("admin")
    @RequestMapping("/activateStaff")
    @ResponseBody
    public String activateStaff(BigInteger staffId){

        boolean flag=staffInfoService.activateStaffSer(staffId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);

        if(flag){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","激活失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }


    // 显示当前系统里的角色
    @RequestMapping("/selectNowPosition")
    @ResponseBody
    public String selectNowPosition(){
        List<Map<String,String>> mapList=staffInfoService.selectNowPosition();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON( mapList));
        return jsonObject.toJSONString(); // 返回json

    }

    // 显示当前角色用于哪些权限
    @RequestMapping("/selectNowPositionAuthority")
    @ResponseBody
    public String selectNowPositionAuthority(BigInteger positionId){
        List<Map<String,String>> mapList=staffInfoService.selectNowPositionAuthority(positionId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON( mapList));
        return jsonObject.toJSONString(); // 返回json
    }

    // 显示当前角色没有哪些权限
    @RequestMapping("/selectNowPositionAuthorityNot")
    @ResponseBody
    public String selectNowPositionAuthorityNot(BigInteger positionId){
        List<Map<String,String>> mapList=staffInfoService.selectNowPositionAuthorityNot(positionId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data", JSONArray.toJSON( mapList));
        return jsonObject.toJSONString(); // 返回json
    }

    // 为当前角色添加一个权限
    @RequiresRoles("admin")
    @RequestMapping("/insertAuthorityForPositon")
    @ResponseBody
    public String insertAuthorityForPositon(BigInteger authorityId,BigInteger positionId){
        boolean flag=staffInfoService.insertAuthorityForPositon(authorityId,positionId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);

        if(flag){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","添加失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }


    // 为当前角色删除一个权限
    @RequiresRoles("admin")
    @RequestMapping("/deleteAuthorityForPosition")
    @ResponseBody
    public String deleteAuthorityForPosition(BigInteger authorityId,BigInteger positionId){
        boolean flag=staffInfoService.deleteAuthorityForPosition(authorityId,positionId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);

        if(flag){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","删除失败");
        }
        return jsonObject.toJSONString(); // 返回json
    }

}
