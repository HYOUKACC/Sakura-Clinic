package com.hcc.service;


import com.github.pagehelper.PageInfo;
import com.hcc.pojo.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface MedicineItemService {

    // 搜索药品
    public PageInfo<MedicineItem> getMedicineItem(Integer page, Integer limit, MedicineItem medicineItem);

    // 添加药品
    public boolean addMedicineItem(MedicineItem medicineItem);

    // 修改药品信息
    public boolean updateMedicineItem(MedicineItem medicineItem);

    // 修改药品的销售状态
    public boolean updateMedicineStatus(MedicineItem medicineItem);

    // 根据药品名自动转换拼音
    public String transPinYin(String str);

    // 根据条形码调取网络接口数据
    public Map<String, String> getInfoByWebBar(MedicineBarcodeInfo medicineBarcodeInfo);

    // 获取待发药列表
    public PageInfo<MedicalRecord> showWaitSendMedical(Integer page, Integer limit, PatientInfo patientInfo);

    // 获取西药待发药列表
    public List<WPrescribe> showWaitSendWP(String medicialRecordId);
    // 获取中药待发药列表
    public List<CPrescribe> showWaitSendCP(String medicialRecordId);

    // 收费界面获取治疗单明细
    public List<Project> showWaitSendPro(String medicialRecordId);

    // 根据药品名搜索药品
    public List<MedicineItem> getMedicineItemByName(MedicineItem medicineItem);

    // 窗口确认发药模块
    public boolean saveAllSendMedical(List<WPreBatch> wPreBatches, List<CPreBatch> cPreBatches, List<ProBatch> proBatches, BigInteger medicalRecordId);

    // 窗口退药列表
    public PageInfo<MedicalRecord> showReturnMedicine(Integer page, Integer limit, PatientInfo patientInfo);

    // 西药退药批次详情
    public List<WPreBatch> showReturnWPreBath(String wPrescribeId);

    // 中药退药批次详情
    public List<CPreBatch> showReturnCPreBath(String cPrescribeId);

    // 治疗单批次xiangq
    public List<ProBatch> showReturnProBath(String projectId);

    // 窗口确认退药模块
    public boolean saveAllReturnMedical(List<WPreBatch> wPreBatches, List<CPreBatch> cPreBatches, List<ProBatch> proBatches, BigInteger medicalRecordId);


    // 检索药品单位
    public List<MedicinesUnit> selectMedicinesUnit();

    // 检索药品剂型
    public List<DosageForm> selectDosageForm();

    // 展示三个月内即将过期的药品
    public PageInfo<MedicineItem> showWillExpiry(Integer page, Integer limit, MedicineItem medicineItem);

    // 质量监控统计
    public Map<String,String> countQuality();


    // 销售监控统计
    public Map<String,String> countSale();
}
