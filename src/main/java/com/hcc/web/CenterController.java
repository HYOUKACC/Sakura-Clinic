package com.hcc.web;

import com.alibaba.fastjson.JSONObject;
import com.hcc.pojo.StaffInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletResponse;

@Controller // 中央跳转控制
public class CenterController {

    // 进入默认页面
    @RequestMapping("/index")
    public String showIndexPage(Model model){

        // 判断当前用户
        Subject subject=SecurityUtils.getSubject();
        StaffInfo staffInfo=(StaffInfo)subject.getPrincipal();


        // 根据用户名查用户信息

        model.addAttribute("staffInfo",staffInfo);
        model.addAttribute("staffDepartment",staffInfo.getStaffDepartment());
        return "/index";
    }

    // 进入欢迎页面
    @RequestMapping("/welcome")
    public String showWelcomePage(){
        return "/page/welcome";
    }

    // 跳转到table页面（药品管理页面）
    @RequestMapping("/showMedicinePage")
    @RequiresPermissions("medicine:index")
    public String showMedicinePage(){
        return "/page/medicinepage/medicineCenter";
    }

    // 跳转到挂号登记页面（患者管理页面）
    @RequestMapping("/patientCenter")
    @RequiresPermissions("register:index")
    public String showPainterCenterPage(){
        return "/page/patientpage/patientCenter";
    }

    @RequestMapping("/test")
    public String showTest(){
        return "page/test";
    }

    // 跳转今日候诊
    @RequestMapping("/waitPatient")
    @RequiresPermissions("wait:index")
    public String showWait(){ return "/page/doctorpage/doctorCenter";}

    // 跳转窗口收费
    @RequestMapping("/chargeCenter")
    @RequiresPermissions("charge:index")
    public String showCharge(){return "/page/patientpage/chargeCenter";}

//    // 跳转窗口退费
//    @RequestMapping("/refundCenter")
//    public String showRefund(){return "/page/patientpage/refundCenter";}

    // 跳转收费明细
    @RequestMapping("/chargeDetail")
    public String showChargeDetail(){return "/page/patientpage/chargeDetail";}

    // 跳转窗口取药
    @RequestMapping("/sendMedicine")
    @RequiresPermissions("sendme:index")
    public String showSendMed(){return "/page/medicinepage/sendMedicalCenter";}

    // 跳转窗口发药详情
    @RequestMapping("/sendMedicineDetail")
    public String showSendMedicaneDetail(){return "/page/medicinepage/sendMedicalDetail";}


    // 窗口退药
    @RequestMapping("/returnMedicine")
    @RequiresPermissions("return:index")
    public String showReturnMedicineCenter(){return "/page/medicinepage/returnMedicalCenter";}

    // 窗口退药详情单
    @RequestMapping("/returnMedicineDetail")
    public String showReturnMedicineDetail(){return "/page/medicinepage/returnMedicalDetail";}


    // 跳转窗口退费
    @RequestMapping("/refundCenter")
    @RequiresPermissions("refund:index")
    public String showReturnChargeCenter(){return "/page/patientpage/refundCenter";}

    // 跳转退费详情
    @RequestMapping("/refundDetail")
    public String showRefundDetail(){return "/page/patientpage/refundDetail";}

    // 跳转中药入库页面
    @RequestMapping("/addMedicineTraditional")
    public String showAddMedicineTra(){return "/page/medicinepage/addmedicineTraditional";}

    // 跳转中药编辑页面
    @RequestMapping("/editmedicineTraditional")
    public String editMedicineTraditional(){return "/page/medicinepage/editmedicineTraditional";}

    // 跳转数据监控中心
    @RequestMapping("/showDataMonitor")
    @RequiresPermissions("data:index")
    public String showDataMonitor(){return  "/page/medicinepage/dataMonitorCenter";}

    // 跳转患者历史病历详情
    @RequestMapping("/historyDetail")
    public String showHistoryDetail(){return "/page/doctorpage/historyDetail";}

    @RequestMapping({"/loginPage","","/"})
    public String showLoginPage(){
        return "/login";
    }

    // 系统内部认证
    @RequestMapping("/login")
    public String login(StaffInfo staffInfo){
        // 默认认证方法
        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(staffInfo.getName(),staffInfo.getPassword());
        try {
            subject.login(usernamePasswordToken);
            return "/index";
        }catch (AuthenticationException e){
            return "/login";
        }catch (AuthorizationException e){
            return "/login";
        }

    }

    // 登录页面认证
    @RequestMapping("/loginForm")
    @ResponseBody
    public String loginCheck(StaffInfo staffInfo){
        // Ajax表单添加用户认证
        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(staffInfo.getName(),staffInfo.getPassword());
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        try {
            subject.login(usernamePasswordToken);
            jsonObject.put("msg","success");
        }catch (AuthenticationException e){
            jsonObject.put("msg","用户或密码错误");
        }catch (AuthorizationException e){
            jsonObject.put("msg","账户未激活");
        }
        return jsonObject.toJSONString();
    }


    // 跳转无权限页面
    @RequestMapping("/noAuthority")
    public String noAuthority(){
        return "/noAuthority";
    }

    // 捕获授权失败异常
    @ExceptionHandler(AuthorizationException.class)
    public void ErrorHandle(HandlerMethod method, HttpServletResponse response) throws Exception {
        // 目标：跳转一个页面，提示木有权限
        // 问题：需要判断是否是ajax请求，返回json
        // method 发送异常的方法
        ResponseBody responseBody=method.getMethodAnnotation(ResponseBody.class);
        if(responseBody!=null){
            // 返回json
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("code",0);
            jsonObject.put("msg","暂无权限");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(jsonObject.toJSONString());
        }else {
            // 跳转无权限页面
            response.sendRedirect("/noAuthority");
        }

    }

    // 跳转员工注册
    @RequestMapping("/registerStaffPage")
    public String showRegisterStaffPage(){return "/registerStaff";}

    // 员工管理中心
    @RequestMapping("/staffCenter")
    @RequiresPermissions("staff:index")
    public String showPatientCenter(){return "/page/staffPage/staffCenter";}

    // 员工信息查看修改
    @RequestMapping("/staffDetailPage")
    public String showStaffDetailPage(){return "/page/staffPage/updateStaffInfo";}

    // 跳转删除角色页面
    @RequestMapping("/deleteiPosition")
    public String showDeletePosition(){return "/page/staffPage/deletePosition";}

    // 跳转添加角色页面
    @RequestMapping("/addPositionUser")
    public String showAddPositionUser(){return  "/page/staffPage/addPosition";}

    // 跳转账号审核页面
    @RequestMapping("/staffVerifyPage")
    @RequiresPermissions("staff:active")
    public String showStaffVerifyPage(){return "/page/staffPage/staffVerify";}

    // 跳转权限管理页面
    @RequestMapping("/staffAuthorityCenterPage")
    @RequiresPermissions("authority:index")
    public String showStaffAuthorityCenterPage(){return "/page/staffPage/staffPositionCenter";}

    // 跳转删除权限页面
    @RequestMapping("/deleteAuthorityPage")
    public String showDeleteAuthotityPage(){return "/page/staffPage/deleteAuthority";}

    // 跳转添加权限页面
    @RequestMapping("/addAuthorityPage")
    public String showaddAuthotityPage(){return "/page/staffPage/addAuthority";}

}
