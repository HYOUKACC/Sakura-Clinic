package com.hcc.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.hcc.pojo.*;
import com.hcc.service.MedicineItemService;
import com.hcc.service.PatientInfoService;
import com.hcc.util.SendMedicalDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller // 药品管理控制
public class MedicineController {

    @Autowired
    private MedicineItemService medicineItemService;

    @Autowired
    private PatientInfoService patientInfoService;


    // 跳转到添加药品页面
    @RequestMapping("/addmedicine")
    public String showAddMedicinePage(){
        return "/page/medicinepage/addmedicine";
    }

    // 更新药品销售状态
    @RequestMapping("/updateStatus")
    @ResponseBody
    public String updateMedicineStatus(MedicineItem medicineItem){
        boolean success=medicineItemService.updateMedicineStatus(medicineItem);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(success){
            jsonObject.put("msg","success");
            jsonObject.put("count",1); // 获取查询的总条数
            jsonObject.put("data","操作成功");
        }else{
            jsonObject.put("msg","flase");
            jsonObject.put("count",0); // 获取查询的总条数
            jsonObject.put("data","操作失败");
        }
        return jsonObject.toJSONString();

    }

    // 跳转到编辑药品页面
    @RequestMapping("/editmedicine")
    public String showEditMedicinePage(){return  "/page/medicinepage/editmedicine";}

    // 更新药品操作
    @RequestMapping("/upadteMedicineItem")
    @ResponseBody
    public String updateMedicine(MedicineItem medicineItem){
        boolean success=medicineItemService.updateMedicineItem(medicineItem);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(success){
            jsonObject.put("msg","success");
            jsonObject.put("count",1); // 获取查询的总条数
            jsonObject.put("data","更新成功");
        }else{
            jsonObject.put("msg","flase");
            jsonObject.put("count",0); // 获取查询的总条数
            jsonObject.put("data","更新失败");
        }
        return jsonObject.toJSONString();
    }

    // 添加药品操作
    @RequestMapping("/addmedicinenow")
    @ResponseBody
    public String addMedicineNow(MedicineItem medicineItem){

        boolean success=medicineItemService.addMedicineItem(medicineItem);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(success){
            jsonObject.put("msg","success");
            jsonObject.put("count",1); // 获取查询的总条数
            jsonObject.put("data","插入成功");
        }else{
            jsonObject.put("msg","flase");
            jsonObject.put("count",0); // 获取查询的总条数
            jsonObject.put("data","插入失败");
        }
        return jsonObject.toJSONString();
    }

    // 获取药品表格数据
    @RequestMapping("/showMedicineTable")
    @ResponseBody
    public String showMedicineTable(@RequestParam(required=false,defaultValue="1") Integer page,
                        @RequestParam(required=false,defaultValue="5") Integer limit,
                        MedicineItem medicineItem){
        // 调用service对象，返回分装好的PageInfo类型，已经执行好分页操作
        PageInfo<MedicineItem> pageInfo=medicineItemService.getMedicineItem(page, limit,medicineItem);

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",pageInfo.getTotal()); // 获取查询的总条数
        jsonObject.put("data",JSONArray.toJSON(pageInfo.getList())); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json

    }

    // 根据药品名快速转换为药品简码
    @RequestMapping("/transToFirst")
    @ResponseBody
    public String transToFirst(String str){

        String result=medicineItemService.transPinYin(str);

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",result); // 放入转换数据
        return jsonObject.toJSONString(); // 返回json

    }

    // 添加药品界面，根据药品条形码快速获取药品信息
    @RequestMapping("/addPageGetInfo")
    @ResponseBody
    public String addPageGetInfo(MedicineBarcodeInfo medicineBarcodeInfo){

        // 调用服务
        Map<String,String> result=medicineItemService.getInfoByWebBar(medicineBarcodeInfo);

        // 把map集合转换为json
        JSONObject jsonObject=new JSONObject();

        return jsonObject.toJSONString(result);
    }

    // 检索待取药信息
    @RequestMapping("/showWaitMedicine")
    @ResponseBody
    public String showWaitSendMedicine(@RequestParam(required=false,defaultValue="1") Integer page,
                                 @RequestParam(required=false,defaultValue="5") Integer limit, PatientInfo patientInfo){

        PageInfo<MedicalRecord> medicalRecords=medicineItemService.showWaitSendMedical(page,limit,patientInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecords.getList()));
        return jsonObject.toJSONString();

    }

    @RequestMapping("/showWPBatchList")
    @ResponseBody
    public String showWPBatchList(MedicineItem medicineItem){

        //
        List<MedicineItem> medicineItems=medicineItemService.getMedicineItemByName(medicineItem);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        //jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicineItems));
        return jsonObject.toJSONString();
    }

    @RequestMapping("/sendMedicalClick")
    @ResponseBody
    public String sendMedicalClick(@RequestBody SendMedicalDetail sendMedicalDetail){

        Map wpBatch=sendMedicalDetail.getwPBatchData(); // 西药药集合
        Map cpBatch=sendMedicalDetail.getcPBatchData(); // 中药药集合
        List<ProBatch> proBatchList=sendMedicalDetail.getProjectBatch();// 获取处方单集合
        List<WPreBatch> wPreBatchList=new ArrayList<>(); // 整理西药集合
        List<CPreBatch> cPreBatchList=new ArrayList<>(); // 整理中药集合
        BigInteger medicalRecordId=sendMedicalDetail.getMedicalRecordId();
        //List<ProBatch>

        for( Object wPreBatches:wpBatch.values()){  // 整理西药集合
            for (Object o : (List<?>) wPreBatches) {
                // 如果sendNumber=0，不放入集合中
                if(WPreBatch.class.cast(o).getSendNumber()!=0){
                    wPreBatchList.add(WPreBatch.class.cast(o));
                }
            }
        }

        for( Object cPreBatches:cpBatch.values()){  // 整理中药集合
            for (Object o : (List<?>) cPreBatches) {
                // 如果sendNumber=0，不放入集合中
                if(CPreBatch.class.cast(o).getSendNumber()!=0){
                    cPreBatchList.add(CPreBatch.class.cast(o));
                }
            }
        }

        // 整理治疗单

//        System.out.println(wPreBatchList);
//        System.out.println(cPreBatchList);
       //System.out.println(proBatchList);

        boolean flag=medicineItemService.saveAllSendMedical(wPreBatchList,cPreBatchList,proBatchList,medicalRecordId);
//
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(flag) {
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","发药失败");
        }
        return jsonObject.toJSONString();
        //return null;
    }


    // 检索可退药病历
    @RequestMapping("/showWaitReturn")
    @ResponseBody
    public String showWaitReturn(@RequestParam(required=false,defaultValue="1") Integer page,
                                 @RequestParam(required=false,defaultValue="5") Integer limit, PatientInfo patientInfo){

        PageInfo<MedicalRecord> medicalRecords=medicineItemService.showReturnMedicine(page,limit,patientInfo);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",medicalRecords.getTotal());
        jsonObject.put("data",JSONArray.toJSON(medicalRecords.getList()));
        return jsonObject.toJSONString();


    }

    // 查询西药退药批次详情
    @RequestMapping("/showReturnWPBatchList")
    @ResponseBody
    public String showReturnWPBatchList(String wPrescribeId){

        List<WPreBatch> wPreBatches=medicineItemService.showReturnWPreBath(wPrescribeId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(wPreBatches));
        return jsonObject.toJSONString();

    }

    // 查询中药退药批次详情单
    @RequestMapping("/showReturnCPBatchList")
    @ResponseBody
    public String showReturnCPBatchList(String cPrescribeId){

        List<CPreBatch> cPreBatches=medicineItemService.showReturnCPreBath(cPrescribeId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(cPreBatches));
        return jsonObject.toJSONString();

    }

    // 查询治疗批次详情
    @RequestMapping("/showReturnProBatchList")
    @ResponseBody
    public String showReturnProBatchList(String projectId){

        List<ProBatch> proBatchList=medicineItemService.showReturnProBath(projectId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(proBatchList));
        return jsonObject.toJSONString();

    }

    // 获取所有退药详情
    @RequestMapping("/returnMedicineClick")
    @ResponseBody
    public String returnMedicineClick(@RequestBody SendMedicalDetail sendMedicalDetail){

        Map wpBatch=sendMedicalDetail.getwPBatchData(); // 西药药集合
        Map cpBatch=sendMedicalDetail.getcPBatchData(); // 中药药集合
        Map proBatch=sendMedicalDetail.getProBatchData();// 治疗单集合

        List<WPreBatch> wPreBatchList=new ArrayList<>(); // 整理西药集合
        List<CPreBatch> cPreBatchList=new ArrayList<>(); // 整理中药集合
        List<ProBatch> proBatchList=new ArrayList<>(); // 整理治疗集合
        BigInteger medicalRecordId=sendMedicalDetail.getMedicalRecordId();

        for( Object wPreBatches:wpBatch.values()){  // 整理西药集合
            for (Object o : (List<?>) wPreBatches) {
                // 如果nowReturnNumber=0，不放入集合中
                if(WPreBatch.class.cast(o).getNowReturnNumber()!=0){
                    wPreBatchList.add(WPreBatch.class.cast(o));
                }
            }
        }

        for( Object cPreBatches:cpBatch.values()){  // 整理中药集合
            for (Object o : (List<?>) cPreBatches) {
                // 如果nowReturnNumber=0，不放入集合中
                if(CPreBatch.class.cast(o).getNowReturnNumber()!=0){
                    cPreBatchList.add(CPreBatch.class.cast(o));
                }
            }
        }

        for( Object proBatches:proBatch.values()){  // 整理中药集合
            for (Object o : (List<?>) proBatches) {
                // 如果nowReturnNumber=0，不放入集合中
                if(ProBatch.class.cast(o).getNowReturnNumber()!=0){
                    proBatchList.add(ProBatch.class.cast(o));
                }
            }
        }

        boolean flag=medicineItemService.saveAllReturnMedical(wPreBatchList,cPreBatchList,proBatchList,medicalRecordId);

//        System.out.println("西药"+wPreBatchList);
//        System.out.println("中药"+cPreBatchList);
//        System.out.println("治疗"+proBatchList);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        if(flag) {
            jsonObject.put("msg","success");
        }else {
            jsonObject.put("msg","退药失败");
        }
        return jsonObject.toJSONString();

    }

    // 自动加载单位
    @RequestMapping("/selectUnit")
    @ResponseBody
    public String selectUnit(){

        List<MedicinesUnit> medicinesUnits=medicineItemService.selectMedicinesUnit();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(medicinesUnits));
        return jsonObject.toJSONString();

    }

    // 自动加载剂型
    @RequestMapping("/selectDosageForm")
    @ResponseBody
    public String selectDosageForm(){
        List<DosageForm> dosageForms=medicineItemService.selectDosageForm();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(dosageForms));
        return jsonObject.toJSONString();
    }

    // 展示即将过期列表
    @RequestMapping("/showMedicineWillExpire")
    @ResponseBody
    public String showMedicineWillEx(@RequestParam(required=false,defaultValue="1") Integer page,
                                     @RequestParam(required=false,defaultValue="5") Integer limit,
                                     MedicineItem medicineItem){


        // 调用service对象，返回分装好的PageInfo类型，已经执行好分页操作
        PageInfo<MedicineItem> pageInfo=medicineItemService.showWillExpiry(page, limit,medicineItem);

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("count",pageInfo.getTotal()); // 获取查询的总条数
        jsonObject.put("data",JSONArray.toJSON(pageInfo.getList())); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json


    }

    // 数据监控页面，统计质量数据
    @RequestMapping("/countQuality")
    @ResponseBody
    public String countQua(){
        Map<String,String> countQua=medicineItemService.countQuality();

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(countQua)); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json
        //return null;
    }

    // 数据监控页面，统计销售状态
    @RequestMapping("/countSale")
    @ResponseBody
    public String countSale(){
        Map<String,String> countSale=medicineItemService.countSale();

        // 把对象进行json转换
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","success");
        jsonObject.put("data",JSONArray.toJSON(countSale)); // 获取数据，转换成json格式
        return jsonObject.toJSONString(); // 返回json
        //return null;
    }


}
