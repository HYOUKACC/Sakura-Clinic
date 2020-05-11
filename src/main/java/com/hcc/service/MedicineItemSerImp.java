package com.hcc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcc.mapper.*;
import com.hcc.pojo.*;
import com.hcc.util.PinYinTransImpl;
import com.hcc.util.WebUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class MedicineItemSerImp implements MedicineItemService {

    @Autowired
    private MedicineItemMapper medicineItemMapper;

    @Autowired
    private MedicineBarcodeInfoMapper medicineBarcodeInfoMapper;

    @Autowired
    private WebUtilImpl webUtil;

    @Autowired
    private PinYinTransImpl pinYinTrans;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private WPrescribeMapper wPrescribeMapper;

    @Autowired
    private CPrescribeMapper cPrescribeMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private WPreBatchMapper wPreBatchMapper;

    @Autowired
    private CPreBatchMapper cPreBatchMapper;

    @Autowired
    private ProBatchMapper proBatchMapper;

    @Autowired
    private MedicinesUnitMapper medicinesUnitMapper;

    @Autowired
    private DosageFormMapper dosageFormMapper;

    // 查询药品
    @Override
    public PageInfo<MedicineItem> getMedicineItem(Integer page, Integer  limit, MedicineItem medicineItem) {

//        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
//        List<MedicineItem> allItems = medicineItemService.getAllItem(); // 执行数据库查询语句，查询所有数据
//        PageInfo<MedicineItem> medicineItemPageInfo=new PageInfo<>(allItems); // 使用pageinfo，对上面查询的数据进行分装
//
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("code",0);
//        jsonObject.put("msg","success");
//        jsonObject.put("count",medicineItemPageInfo.getTotal()); // 获取查询的总条数
//        jsonObject.put("data",JSONArray.toJSON(medicineItemPageInfo.getList())); // 获取数据，转换成json格式
//        return jsonObject.toJSONString(); // 返回json

        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicineItem> allItems = medicineItemMapper.selectAllItem(medicineItem);// 把对象传递过去
        PageInfo<MedicineItem> medicineItemPageInfo=new PageInfo<>(allItems); // 使用pageinfo，对上面查询的数据进行分装
        return medicineItemPageInfo;
    }

    // 添加药品
    @Override
    public boolean addMedicineItem(MedicineItem medicineItem) {

//        int flag=0;
//        System.out.println(medicineItem);
        int flag=medicineItemMapper.addMedicine(medicineItem);
        if (flag==0){
            return false;
        }else {
            return true;
        }

    }

    // 修改药品信息
    @Override
    public boolean updateMedicineItem(MedicineItem medicineItem) {
        int flag=medicineItemMapper.updateMedicine(medicineItem);
        if (flag==0){
            return false;
        }else {
            return true;
        }
    }

    // 修改药品销售状态，（后期要修改，增加判断条件）
    @Override
    public boolean updateMedicineStatus(MedicineItem medicineItem) {

       if(medicineItem.getMedicinesId()!=null){
           if(medicineItem.getStatus().equals("在售")){
               medicineItem.setStatus("在库");
           }else if(medicineItem.getStatus().equals("在库")){
               // 后期要添加判断，如果已经有相同条码的药品在销售了，就不能更新，同时只能有一个药品在售
               medicineItem.setStatus("在售");
           }
           int flag=medicineItemMapper.updateMedicineStatus(medicineItem);
           if(flag==0){
               return false;
           }else {
               return true;
           }
       }else {
           return false;
       }


    }


    // 根据药品名自动转换拼音
    @Override
    public String transPinYin(String str) {
        return pinYinTrans.transToFirst(str).toUpperCase();
    }

    // 根据条形码自动获取药品的基本信息
    @Override
    public Map<String, String> getInfoByWebBar(MedicineBarcodeInfo medicineBarcodeInfo) {

       Map<String,String> result=new HashMap<>() ; // 接收网络数据

        // 先去本地数据库找，如果没有数据，则去网络接口中调用
        List<MedicineBarcodeInfo> medicineBarcodeInfos=medicineBarcodeInfoMapper.selectByBar(medicineBarcodeInfo);
        if(medicineBarcodeInfos.size()>0){ // 本地数据中有结果
            // 查询的结果只有一条，因为是根据条形码来精确查询的
            // 获取集合中的结果,遍历赋值到map中
            medicineBarcodeInfo=medicineBarcodeInfos.get(0); // 搜索到第一个数据
            result.put("status","200");
            result.put("message","查询成功！");
            result.put("medicinesBarcode",medicineBarcodeInfo.getMedicinesBarcode());
            result.put("oem",medicineBarcodeInfo.getOem());
            result.put("medicinesName",medicineBarcodeInfo.getMedicinesName());
            result.put("medicinesCode",medicineBarcodeInfo.getMedicinesCode());
            result.put("img",medicineBarcodeInfo.getMedicineImg());
        }else{
            // 调用网络接口，返回数据
            medicineBarcodeInfo.getMedicinesBarcode();
            result=webUtil.getInfoByWeb(medicineBarcodeInfo.getMedicinesBarcode());

            // 判断status，成功则存储到数据库中
            if(result.get("status").equals("200")){
                medicineBarcodeInfo.setMedicinesBarcode(result.get("medicinesBarcode"));
                medicineBarcodeInfo.setMedicinesName(result.get("medicinesName"));
                medicineBarcodeInfo.setMedicinesCode(result.get("medicinesCode"));
                medicineBarcodeInfo.setOem(result.get("oem"));
                medicineBarcodeInfo.setMedicineImg(result.get("img"));
                medicineBarcodeInfoMapper.addWebInfo(medicineBarcodeInfo); // 存储数据
            }

        }
        return result;
    }

    // 获取待发药列表
    @Override
    public PageInfo<MedicalRecord> showWaitSendMedical(Integer page, Integer limit, PatientInfo patientInfo) {

        MedicalRecord medicalRecord=new MedicalRecord();
        medicalRecord.setPatientInfo(patientInfo);
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showWaitSendMedical(medicalRecord);
        PageInfo<MedicalRecord> medicalRecordsPageInfo =new PageInfo<>(medicalRecords);
        return medicalRecordsPageInfo;

    }

    // 获取西药待发药列表
    @Override
    public List<WPrescribe> showWaitSendWP(String medicialRecordId) {
        return null;
    }

    // 获取中药待发药列表
    @Override
    public List<CPrescribe> showWaitSendCP(String medicialRecordId) {
        return null;
    }

    // 获取治疗待发药列表
    @Override
    public List<Project> showWaitSendPro(String medicialRecordId) {
        return null;
    }

    // 根据药品名搜索药品
    @Override
    public List<MedicineItem> getMedicineItemByName(MedicineItem medicineItem) {

        List<MedicineItem> allItems = medicineItemMapper.selectMedicalItemByName(medicineItem);// 把对象传递过去
        return allItems;
    }

    // 发药窗口存储所有发药详情
    @Override
    public boolean saveAllSendMedical(List<WPreBatch> wPreBatches, List<CPreBatch> cPreBatches, List<ProBatch> proBatches, BigInteger medicalRecordId) {


        MedicalRecord medicalRecord=new MedicalRecord();

        List<MedicineItem> wmedicineItemList=new ArrayList<>();
        List<MedicineItem> cmedicineItemList=new ArrayList<>();
        List<WPrescribe> wPrescribeList=new ArrayList<>();
        List<CPrescribe> cPrescribeList=new ArrayList<>();
        List<Project> projectList=new ArrayList<>();

        if(wPreBatches.size()!=0){

            for (WPreBatch wPreBatch:wPreBatches){
                wPreBatch.setIsSend(1); // 遍历批次列表，设置已发药 // 西药批次详情单is_send=1
                wPreBatch.setWbStatus("1");

                MedicineItem medicineItem=new MedicineItem();
                medicineItem.setMedicinesId(wPreBatch.getMedicinesId()); // 根据id扣减现库存
                medicineItem.setCurrentNumber(wPreBatch.getSendNumber());
                wmedicineItemList.add(medicineItem); // 存储药品扣减库存列表

                WPrescribe wPrescribe=new WPrescribe();
                wPrescribe.setId(wPreBatch.getwPrescribeId()); // 设置西药处方单
                wPrescribe.setIsSend(1);  // 设置西药处方单is_send=1
                wPrescribeList.add(wPrescribe);
            }
//            System.out.println("西药批次详情"+wPreBatches);
//            System.out.println("西药处方单"+wPrescribeList);
//            System.out.println("西药药品更新"+wmedicineItemList);

            // 插入西药批次详情单(批量)
            wPreBatchMapper.insertWPreBatch(wPreBatches);

            // 修改西药处方单（批量）
            wPrescribeMapper.updateSendMedical(wPrescribeList);

            // 修改药品库存（批量）
            medicineItemMapper.updateMedicialCurrentNumber(wmedicineItemList);
        }
        if(cPreBatches.size()!=0){
            // 设置西药处方单is_send=1
            // 西药批次详情单is_send=1
            // 根据id修改库存更新
            for (CPreBatch cPreBatch:cPreBatches){
                cPreBatch.setIsSend(1); // 遍历批次列表，设置已发药 // 西药批次详情单is_send=1
                cPreBatch.setCbStatus("1");

                MedicineItem medicineItem=new MedicineItem();
                medicineItem.setMedicinesId(cPreBatch.getMedicinesId()); // 根据id扣减现库存
                medicineItem.setCurrentNumber(cPreBatch.getSendNumber());
                cmedicineItemList.add(medicineItem); // 存储药品扣减库存列表

                CPrescribe cPrescribe=new CPrescribe();
                cPrescribe.setId(cPreBatch.getcPrescribeId()); // 设置西药处方单
                cPrescribe.setIsSend(1);  // 设置西药处方单is_send=1
                cPrescribeList.add(cPrescribe);
            }

//            System.out.println("中药批次详情"+cPreBatches);
//            System.out.println("中药处方单"+cPrescribeList);
//            System.out.println("中药药品更新"+cmedicineItemList);

            // 插入中药批次详情单(批量)
            cPreBatchMapper.insertCPreBatchList(cPreBatches);

            // 修改中药处方单（批量）
            cPrescribeMapper.updateSendMedical(cPrescribeList);

            // 修改药品库存（批量）
            medicineItemMapper.updateMedicialCurrentNumber(cmedicineItemList);
        }
        if (proBatches.size()!=0){
            // 设置治疗处方单is_send=1
            // 治疗批次详情单is_send=1
            for (ProBatch proBatch:proBatches){
                proBatch.setIsSend(1); // 遍历批次列表，设置已发药 // 治疗批次详情单is_send=1
                proBatch.setProStatus("1");

                Project project=new Project();
                project.setId(proBatch.getProjectId()); // 设置西药处方单
                project.setIsSend(1);  // 设置治疗处方单is_send=1
                projectList.add(project);
            }
//            System.out.println("治疗批次详情"+proBatches);
//            System.out.println("治疗处方单"+projectList);
//            //System.out.println("西药药品更新"+wmedicineItemList);

            // 插入治疗批次详情单(批量)
            proBatchMapper.insertProBatchList(proBatches);

            // 修改治疗处方单（批量）
            projectMapper.updateProList(projectList);

        }

        // 修改病历状态
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setRegisterStatus("已取药");
        int right=medicalRecordMapper.updateSendMedicine(medicalRecord);
        if(right==1){
            return true;
        }else {
            return false;
        }
   //     return false;

    }


    // 窗口退药列表
    @Override
    public PageInfo<MedicalRecord> showReturnMedicine(Integer page, Integer limit, PatientInfo patientInfo) {

        MedicalRecord medicalRecord=new MedicalRecord();
        medicalRecord.setPatientInfo(patientInfo);
        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        List<MedicalRecord> medicalRecords=medicalRecordMapper.showReturnMedical(medicalRecord);
        PageInfo<MedicalRecord> medicalRecordsPageInfo =new PageInfo<>(medicalRecords);
        return medicalRecordsPageInfo;


    }

    // 退药详情单获取西药批次详情
    @Override
    public List<WPreBatch> showReturnWPreBath(String wPrescribeId) {

        List<WPreBatch> wPreBatches=wPreBatchMapper.selectWPreBatchByWPId(wPrescribeId);

        return wPreBatches;
    }


    // 退药详情单获取中药批次详情
    @Override
    public List<CPreBatch> showReturnCPreBath(String cPrescribeId) {

        List<CPreBatch> cPreBatches=cPreBatchMapper.selectCPreBatchByWPId(cPrescribeId);

        return cPreBatches;

    }

    // 退药治疗单批次查询
    @Override
    public List<ProBatch> showReturnProBath(String projectId) {

        List<ProBatch> proBatchList=proBatchMapper.selectProBatchByProId(projectId);

        return proBatchList;
    }


    // 退药窗口存储所有退药详情
    @Override
    public boolean saveAllReturnMedical(List<WPreBatch> wPreBatches, List<CPreBatch> cPreBatches, List<ProBatch> proBatches, BigInteger medicalRecordId) {

        MedicalRecord medicalRecord=new MedicalRecord();

        List<MedicineItem> wmedicineItemList=new ArrayList<>();
        List<MedicineItem> cmedicineItemList=new ArrayList<>();
        List<WPrescribe> wPrescribeList=new ArrayList<>();
        List<CPrescribe> cPrescribeList=new ArrayList<>();
        List<Project> projectList=new ArrayList<>();

        if(wPreBatches.size()!=0){

            for (WPreBatch wPreBatch:wPreBatches){
                wPreBatch.setIsReturn(1); // 遍历批次列表，设置已退药//
                wPreBatch.setReturnNumber(wPreBatch.getNowReturnNumber());// 设置退药数


                MedicineItem medicineItem=new MedicineItem();
                medicineItem.setMedicinesId(wPreBatch.getMedicinesId()); // 根据id扣减现库存
                medicineItem.setCurrentNumber(wPreBatch.getNowReturnNumber());// 药品库退药数
                wmedicineItemList.add(medicineItem); // 存储药品增加库存列表

                WPrescribe wPrescribe=new WPrescribe();
                wPrescribe.setId(wPreBatch.getwPrescribeId()); // 设置西药处方单
                wPrescribe.setIsReturn(1);  // 设置西药处方单is_return=1
                wPrescribeList.add(wPrescribe);
            }
//            System.out.println("西药批次详情"+wPreBatches);
//            System.out.println("西药处方单"+wPrescribeList);
//            System.out.println("西药药品更新"+wmedicineItemList);

            // 修改西药批次详情单(批量)
           wPreBatchMapper.upadetReturnWPreBatch(wPreBatches);

            // 修改西药处方单（批量）
           wPrescribeMapper.updateReturnMedical(wPrescribeList);

            // 修改药品库存（批量）
            medicineItemMapper.updateReturnMedical(wmedicineItemList);
        }
        if(cPreBatches.size()!=0){

            for (CPreBatch cPreBatch:cPreBatches){
                cPreBatch.setIsReturn(1);
                cPreBatch.setReturnNumber(cPreBatch.getNowReturnNumber());

                MedicineItem medicineItem=new MedicineItem();
                medicineItem.setMedicinesId(cPreBatch.getMedicinesId()); // 根据id扣减现库存
                medicineItem.setCurrentNumber(cPreBatch.getNowReturnNumber());
                cmedicineItemList.add(medicineItem); // 存储药品增加库存列表

                CPrescribe cPrescribe=new CPrescribe();
                cPrescribe.setId(cPreBatch.getcPrescribeId());
                cPrescribe.setIsReturn(1);
                cPrescribeList.add(cPrescribe);
            }

//            System.out.println("中药批次详情"+cPreBatches);
//            System.out.println("中药处方单"+cPrescribeList);
//            System.out.println("中药药品更新"+cmedicineItemList);

            // 修改中药批次详情单(批量)
            cPreBatchMapper.upadetReturnCPreBatch(cPreBatches);

            // 修改中药处方单（批量）
            cPrescribeMapper.updateReturnMedical(cPrescribeList);

            // 修改药品库存（批量）
            medicineItemMapper.updateReturnMedical(cmedicineItemList);
        }
        if (proBatches.size()!=0){
            // 设置治疗处方单is_send=1
            // 治疗批次详情单is_send=1
            for (ProBatch proBatch:proBatches){
                proBatch.setIsReturn(1); // 遍历批次列表，设置已发药 // 治疗批次详情单is_send=1
                proBatch.setReturnNumber(proBatch.getNowReturnNumber());

                Project project=new Project();
                project.setId(proBatch.getProjectId());
                project.setIsReturn(1);
                projectList.add(project);
            }
//            System.out.println("治疗批次详情"+proBatches);
//            System.out.println("治疗处方单"+projectList);
//            //System.out.println("西药药品更新"+wmedicineItemList);

            // 修改治疗批次详情单(批量)
            proBatchMapper.upadetReturnProBatch(proBatches);

            // 修改治疗处方单（批量）
            projectMapper.updateReturnMedical(projectList);

        }

        // 修改病历状态
        medicalRecord.setMedicalRecordId(medicalRecordId);
        //medicalRecord.setRegisterStatus("已取药");
        //已经取药为最终状态，病历状态不再修改
        // 仅修改退药标记，is_return
        medicalRecord.setIsReturn(1);
        int right=medicalRecordMapper.updateReturnMedical(medicalRecord);
        if(right==1){
            return true;
        }else {
            return false;
        }
       // return false;
    }

    // 检索药品单位
    @Override
    public List<MedicinesUnit> selectMedicinesUnit() {

        List<MedicinesUnit> medicinesUnits=medicinesUnitMapper.selectMedicinesUnit();
        return medicinesUnits;
    }

    // 检索剂型
    @Override
    public List<DosageForm> selectDosageForm() {

        List<DosageForm> dosageForms=dosageFormMapper.selectDosageForm();

        return dosageForms;
    }


    // 搜索即将过期药品
    @Override
    public PageInfo<MedicineItem> showWillExpiry(Integer page, Integer limit, MedicineItem medicineItem) {

        PageHelper.startPage(page,limit); // 传入当前页面数，和每页显示的条数
        Calendar calendar = Calendar.getInstance();//默认是当前日期
        calendar.add(Calendar.MONTH,3);// 三个月内
        medicineItem.setExpiryDate(calendar.getTime()); // 设置时间
        List<MedicineItem> allItems = medicineItemMapper.willExpirySelect(medicineItem);// 把对象传递过去
        PageInfo<MedicineItem> medicineItemPageInfo=new PageInfo<>(allItems); // 使用pageinfo，对上面查询的数据进行分装
        return medicineItemPageInfo;
        //return null;
    }

    // 质量监控统计
    @Override
    public Map<String, String> countQuality() {
        Calendar calendar = Calendar.getInstance();//默认是当前日期
        calendar.add(Calendar.MONTH,3);// 三个月内
        String expiryDate=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
        Map<String,String> countQualityMap= medicineItemMapper.countQuality(expiryDate);
        return countQualityMap;
    }

    // 销售监控统计
    @Override
    public Map<String, String> countSale() {
        Map<String,String> countSale=medicineItemMapper.countSale();
        return countSale;
    }


}
