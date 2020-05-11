package com.hcc.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hcc.pojo.*;
import com.hcc.service.PatientInfoService;
import com.hcc.util.DetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller // 患者控制跳转
public class PatientInfoController {

    @Autowired
    private PatientInfoService patientInfoService;



    // 检索患者
    @RequestMapping("showPatientTable")
    @ResponseBody
    public String showPatienTable(@RequestParam(required=false,defaultValue="1") Integer page,
                                  @RequestParam(required=false,defaultValue="5") Integer limit,
                                  PatientInfo patientInfo){

        // 调用service对象，返回分装好的PageInfo类型，已经执行好分页操作
        PageInfo<PatientInfo> pageInfo=patientInfoService.selectPatientInfo(page,limit,patientInfo);

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",pageInfo.getTotal()); // 获取查询的总条数
        jsonObject.put("data", JSONArray.toJSON(pageInfo.getList())); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json

    }

    // 跳转患者登记页面
    @RequestMapping("/addpatient")
    public String addpatientPage(){return "page/patientpage/addpatient";}

    // 跳转患者挂号页面
    @RequestMapping("/patientregister")
    public String patientregisterPage(){return "page/patientpage/patientregister";}

    // 跳转患者预诊页面
    @RequestMapping("/preReceive")
    public String showPre(){return "page/doctorpage/preReceive";}

    // 跳转接诊患者界面
    @RequestMapping("/receive")
    public String showRce(){return "page/doctorpage/receive";}

    // 添加患者登记
    @RequestMapping("/savepatient")
    @ResponseBody
    public String savepatient(String province, PatientInfo patientInfo,
                              String city, String county, String addD){
        String address=province+"-"+city+"-"+county+"-"+addD;
        patientInfo.setAddress(address);
        boolean result=patientInfoService.savePatientInfo(patientInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(result){
            jsonObject.put("msg","success");
            jsonObject.put("count",1); // 获取查询的总条数
            jsonObject.put("data","登记成功");
        }else{
            jsonObject.put("msg","flase");
            jsonObject.put("count",0); // 获取查询的总条数
            jsonObject.put("data","登记失败");
        }
        return jsonObject.toJSONString();
    }

    // 检索挂号科室
    @RequestMapping("/selectOutDp")
    @ResponseBody
    public String selectOutD(){
        List<StaffDepartments> staffDepartments=patientInfoService.selectOutDpart();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(staffDepartments));
        return jsonObject.toJSONString();
    }

    // 检索挂号类型
    @RequestMapping("/selectRecodCat")
    @ResponseBody
    public String selectRC(){
        List<RecordCategory> recordCategories=patientInfoService.selectRecCat();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(recordCategories));
        return jsonObject.toJSONString();

    }

    // 患者挂号
    @RequestMapping("/addregister")
    @ResponseBody
    public String addReg(StaffInfo staffInfo, PatientInfo patientInfo,
                         String registerCategory , MedicalRecord medicalRecord){

       boolean result=patientInfoService.saveReg(staffInfo,patientInfo,registerCategory,medicalRecord);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        if(result){
            jsonObject.put("data","success");
        }else {
            jsonObject.put("data","fail");
        }
        return jsonObject.toJSONString();
    }

    // 候诊患者查询默认
    @RequestMapping("/waitPatientListDefault")
    @ResponseBody
    public String waitList(Integer page,Integer limit){

        PageInfo<MedicalRecord> medicalRecords=patientInfoService.showListDefault(page,limit);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecords.getList()));
        return jsonObject.toJSONString();
    }


    // 候诊患者查询-多条件
    @RequestMapping("/waitPatientList")
    @ResponseBody
    public String waitList(Integer page, Integer limit,
                           StaffInfo staffInfo, StaffDepartments staffDepartments,
                           PatientInfo patientInfo, MedicalRecord medicalRecord){

        PageInfo<MedicalRecord> medicalRecords=patientInfoService.showList(page,limit,staffInfo,staffDepartments,patientInfo,medicalRecord);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecords.getList()));
        return jsonObject.toJSONString();
    }

    // 患者预诊页面填充数据
    @RequestMapping("/showMedicalDetail")
    @ResponseBody
    public String showMedDet(MedicalRecord medicalRecord){
        Map resultMap=patientInfoService.showPrePatient(medicalRecord);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(resultMap));
        return jsonObject.toJSONString();

    }

    // 患者主诉检索
    @RequestMapping("/patientDescribe")
    @ResponseBody
    public String patientDes(@RequestParam(defaultValue="1")Integer page,Integer limit,String str){

        // mysql方法
        //PageInfo<PatientDescribe> patientDescribePageInfo=patientInfoService.showPatientDes(page,limit,str);

        // ES方法
        Page<PatientDescribe> patientDescribes=patientInfoService.showPatientDes(page,limit,str);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        // myslq方法
        //jsonObject.put("count",(patientDescribePageInfo.getTotal()/limit)+1);
        //jsonObject.put("data",JSONArray.toJSON(patientDescribePageInfo.getList()));
        List<PatientDescribe> patientDescribesContent=patientDescribes.getContent();
        jsonObject.put("count",patientDescribes.getTotalPages());
        jsonObject.put("data",JSONArray.toJSON(patientDescribesContent));

        return jsonObject.toJSONString();

    }

    // 保存患者预诊页面信息
    @RequestMapping("/savePatientPre")
    @ResponseBody
    public String savePatientPre(String patientHeight, String patientTemperature,
                                 String patientWeight, String patientDescribe, String medicalRecordId,
                                 String patientBp , PatientInfo patientInfo){

        MedicalRecord medicalRecord=new MedicalRecord();
        BigInteger mrid=new BigInteger(medicalRecordId);
        medicalRecord.setMedicalRecordId(mrid);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        // 正则表达式校验 身高，体重，体温，血压
        if(Pattern.matches("^([1-9][0-9]*)+(\\.[0-9]{1,2})?$",patientHeight)){
            medicalRecord.setPatientHeight(Double.parseDouble(patientHeight));
        }else if(!patientHeight.equals("")){
            jsonObject.put("msg","身高格式错误");
            return jsonObject.toJSONString();
        }
        // 验证体温
        if(Pattern.matches("^([1-9][0-9]*)+(\\.[0-9]{1,2})?$",patientTemperature)){
            medicalRecord.setPatientTemperature(Double.parseDouble(patientTemperature));
        }else if(!patientTemperature.equals("")){
            jsonObject.put("msg","体温格式错误");
            return jsonObject.toJSONString();
        }
        // 验证体重
        if(Pattern.matches("^([1-9][0-9]*)+(\\.[0-9]{1,2})?$",patientWeight)){
            medicalRecord.setPatientWeight(Double.parseDouble(patientWeight));
        }else if(!patientWeight.equals("")){
            jsonObject.put("msg","体重格式错误");
            return jsonObject.toJSONString();
        }
        // 验证血压
        if(Pattern.matches("^[0-9]+/[0-9]+$",patientBp)){
            medicalRecord.setPatientBp(patientBp);
        }else if(!patientBp.equals("")){
            jsonObject.put("msg","血压格式错误");
            return jsonObject.toJSONString();
        }
        // 验证通过，存入预诊信息，调用病历表和患者信息表
        // 根据medicalRecord更新
        medicalRecord.setPatientDescribe(patientDescribe);
        medicalRecord.setRegisterStatus("已预诊");
        boolean mr=patientInfoService.updatePreMedicalRecord(medicalRecord);
        boolean pi=patientInfoService.updatePrePatientInfo(patientInfo);
        // 根据patientInfo更新
        if(mr && pi){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","预诊失败！");
        }

        return jsonObject.toJSONString();
    }

    // 检索临床诊断
    @RequestMapping("/clinicDiagnosis")
    @ResponseBody
    public String clinicDiag(@RequestParam(defaultValue="1")Integer page,Integer limit,String str){

        Page<ClinicDiagnosis> clinicDiagnoses=patientInfoService.showClinicDia(page,limit,str);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",clinicDiagnoses.getTotalPages());
        jsonObject.put("data",JSONArray.toJSON(clinicDiagnoses.getContent()));
        return jsonObject.toJSONString();

    }

    // 检索药品条目（ES）
    @RequestMapping("/medicineItemByEs")
    @ResponseBody
    public String medicineItemEs(@RequestParam(defaultValue="1")Integer page,Integer limit,String str,String group){

        Page<MedicineItem> medicineItems=patientInfoService.showMedicineItemEs(page,limit,str,group);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicineItems.getTotalPages());
        jsonObject.put("data",JSONArray.toJSON(medicineItems.getContent()));
        return jsonObject.toJSONString();

    }

    // 检索西药用法表（ES）
    @RequestMapping("/wDirectionByEs")
    @ResponseBody
    public String WDirectionByEs(@RequestParam(defaultValue="1")Integer page,Integer limit,String str){
        Page<WDirection> wDirections=patientInfoService.showWDirectionEs(page,limit,str);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",wDirections.getTotalPages());
        jsonObject.put("data",JSONArray.toJSON(wDirections.getContent()));
        return jsonObject.toJSONString();
    }

    // 检索药品频次表
    @RequestMapping("/searchMedicineFrequency")
    @ResponseBody
    public String showMedFre(@RequestParam(defaultValue="1")Integer page, Integer limit, MedicineFrequency medicineFrequency){

        PageInfo<MedicineFrequency> medicineFrequencyPageInfo=patientInfoService.showMedicineFrequency(page,limit,medicineFrequency);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicineFrequencyPageInfo.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicineFrequencyPageInfo.getList()));
        return jsonObject.toJSONString();

    }

    // 检索中药用法表（ES）
    @RequestMapping("/cDirectionByEs")
    @ResponseBody
    public String CDirectionByEs(@RequestParam(defaultValue="1")Integer page,Integer limit,String str){
        Page<CDirection> cDirections=patientInfoService.showCDirectionEs(page,limit,str);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",cDirections.getTotalPages());
        jsonObject.put("data",JSONArray.toJSON(cDirections.getContent()));
        return jsonObject.toJSONString();
    }

    // 检索治疗项（ES）
    @RequestMapping("/projectItemByEs")
    @ResponseBody
    public String projectItemByEs(@RequestParam(defaultValue="1")Integer page,Integer limit,String str){

        Page<ProjectItem> projectItems=patientInfoService.showProjectItemEs(page,limit,str);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",projectItems.getTotalPages());
        jsonObject.put("data",JSONArray.toJSON(projectItems.getContent()));
        return jsonObject.toJSONString();
    }

    // 存储医生所有的处方单及病历信息
    @RequestMapping("/saveAllMedicalRecordDetail")
    @ResponseBody
    public String saveAllMRD(@RequestBody DetailModel detailModel){

        Map<String,String> medicalRecordCheck=detailModel.getMedicalRecordCheck();
        MedicalRecord medicalRecord=detailModel.getMedicalRecordDetail();
        PatientInfo patientInfo=detailModel.getPatientInfoDetail();
        List<WPrescribe> wPrescribes=detailModel.getwMedicineItemDetail();
        List<CPrescribe> cPrescribes=detailModel.getcMedicineItemDetail();
        List<Project> projects=detailModel.getProjectDetail();

        // 数据输入检查
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);

        String patientHeight=medicalRecordCheck.get("patientHeight");
        String patientTemperature=medicalRecordCheck.get("patientTemperature");
        String patientWeight=medicalRecordCheck.get("patientWeight");
        String patientBp=medicalRecordCheck.get("patientBp");

        // 正则表达式校验 身高，体重，体温，血压
        if(Pattern.matches("^([1-9][0-9]*)+(\\.[0-9]{1,2})?$",patientHeight)){
            medicalRecord.setPatientHeight(Double.parseDouble(patientHeight));
        }else if(!patientHeight.equals("")){
            jsonObject.put("msg","身高格式有误");
            return jsonObject.toJSONString();
        }
        // 验证体温
        if(Pattern.matches("^([1-9][0-9]*)+(\\.[0-9]{1,2})?$",patientTemperature)){
            medicalRecord.setPatientTemperature(Double.parseDouble(patientTemperature));
        }else if(!patientTemperature.equals("")){
            jsonObject.put("msg","体温格式有误");
            return jsonObject.toJSONString();
        }
        // 验证体重
        if(Pattern.matches("^([1-9][0-9]*)+(\\.[0-9]{1,2})?$",patientWeight)){
            medicalRecord.setPatientWeight(Double.parseDouble(patientWeight));
        }else if(!patientWeight.equals("")){
            jsonObject.put("msg","体重格式有误");
            return jsonObject.toJSONString();
        }
        // 验证血压
        if(Pattern.matches("^[0-9]+/[0-9]+$",patientBp)){
            medicalRecord.setPatientBp(patientBp);
        }else if(!patientBp.equals("")){
            jsonObject.put("msg","血压格式有误");
            return jsonObject.toJSONString();
        }

        // 数据传递给服务层进行处理
        boolean isSave=patientInfoService.saveAllDoctorPrescribe(medicalRecord,patientInfo,wPrescribes,cPrescribes,projects);
//        System.out.println(medicalRecord);
//        System.out.println(patientInfo);
//        System.out.println(wPrescribes);
//        System.out.println(cPrescribes);
//        System.out.println(projects);

        if(isSave){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","保存失败");
        }


        return jsonObject.toJSONString();
    }


    // 获取待收费列表
    @RequestMapping("/showWaitCharge")
    @ResponseBody
    public String showWaitCharge(@RequestParam(required=false,defaultValue="1") Integer page,
                                 @RequestParam(required=false,defaultValue="5") Integer limit, PatientInfo patientInfo){

        PageInfo<MedicalRecord> medicalRecords=patientInfoService.showWaitCharge(page,limit,patientInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecords.getList()));
        return jsonObject.toJSONString();
    }

    // 获取西药处方单详情
    @RequestMapping("/showWaitWPDetail")
    @ResponseBody
    public String showWPDetail(String medicalRecordId){

        List<WPrescribe> wPrescribes=patientInfoService.showWaitWP(medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(wPrescribes));

        return jsonObject.toJSONString();
    }


    // 获取中药处方单详情
    @RequestMapping("/showWaitCPDetail")
    @ResponseBody
    public String showCPDetail(String medicalRecordId){

        List<CPrescribe> cPrescribes=patientInfoService.showWaitCP(medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(cPrescribes));

        return jsonObject.toJSONString();
    }

    // 获取治疗单详情
    @RequestMapping("/showWaitProDetail")
    @ResponseBody
    public String showProDetail(String medicalRecordId){

        List<Project> projects=patientInfoService.showWaitPro(medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(projects));

        return jsonObject.toJSONString();
    }

    // 窗口缴费确认
    @RequestMapping("/chargeMedicalRecord")
    @ResponseBody
    public String chargeMedicalRe(MedicalRecord medicalRecord){

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        int index=patientInfoService.chargeMedicalRe(medicalRecord);
        if(index==1){
            jsonObject.put("msg","success");
        }else{
            jsonObject.put("msg","缴费失败");
        }
        return jsonObject.toJSONString();
    }

    // 窗口待退费列表
    @RequestMapping("/showWaitRefund")
    @ResponseBody
    public String showWaitRefund(@RequestParam(required=false,defaultValue="1") Integer page,
                                 @RequestParam(required=false,defaultValue="5") Integer limit, PatientInfo patientInfo){

        PageInfo<MedicalRecord> medicalRecords=patientInfoService.showWaitRefund(page,limit,patientInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecords.getList()));
        return jsonObject.toJSONString();

    }

    // 窗口获取西药待退费批次
    @RequestMapping("/showWaitRefundWBatch")
    @ResponseBody
    public String showWaitRefundWBatch(String medicalRecordId){
        List<Map<String,String>> waitRefundWBtch=patientInfoService.showWaitRefundWBtch(medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(waitRefundWBtch));
        return jsonObject.toJSONString();

    }

    // 窗口获取中药待退费批次
    @RequestMapping("/showWaitRefundCBatch")
    @ResponseBody
    public String showWaitRefundCBatch(String medicalRecordId){
        List<Map<String,String>> waitRefundCBtch=patientInfoService.showWaitRefundCBtch(medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(waitRefundCBtch));
        return jsonObject.toJSONString();

    }

    // 窗口获取西药待退费批次
    @RequestMapping("/showWaitRefundProBatch")
    @ResponseBody
    public String showWaitRefundProBatch(String medicalRecordId){
        List<Map<String,String>> waitRefundProBtch=patientInfoService.showWaitRefundProBtch(medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(waitRefundProBtch));
        return jsonObject.toJSONString();

    }

    // 窗口退费确认
    @RequestMapping("/refundDetailClick")
    @ResponseBody
    public String refundDetailClick(@RequestParam(value = "wpreBatch[]" ,defaultValue = "") List<BigInteger> wpreBatch,@RequestParam(value = "cpreBatch[]" ,defaultValue = "") List<BigInteger> cpreBatch,@RequestParam(value = "proBatch[]" ,defaultValue = "") List<BigInteger> proBatch,BigInteger medicalRecordId){

//        System.out.println(wpreBatch);
//        System.out.println(cpreBatch);
//        System.out.println(proBatch);
//        System.out.println(medicalRecordId);
        boolean flag=patientInfoService.refundDetailClick(wpreBatch,cpreBatch,proBatch,medicalRecordId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(flag){
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","退费失败");
        }
        return jsonObject.toJSONString();


    }

    // 获取历史处方单
    @RequestMapping("/showHistoryMRList")
    @ResponseBody
    public String showHistoryList(@RequestParam(required=false,defaultValue="1") Integer page,
                                  @RequestParam(required=false,defaultValue="5") Integer limit, PatientInfo patientInfo){

        PageInfo<MedicalRecord> medicalRecordPageInfo=patientInfoService.showHistoryList(page,limit,patientInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecordPageInfo.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecordPageInfo.getList()));
        return jsonObject.toJSONString();

    }

}
