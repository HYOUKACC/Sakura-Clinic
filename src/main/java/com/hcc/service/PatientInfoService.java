package com.hcc.service;

import com.github.pagehelper.PageInfo;
import com.hcc.pojo.*;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface PatientInfoService {

    // 检索患者信息
    public PageInfo<PatientInfo> selectPatientInfo(Integer page, Integer limit, PatientInfo patientInfo);

    // 保存患者信息
    public boolean savePatientInfo(PatientInfo patientInfo);

    // 查询挂号科室
    public List<StaffDepartments> selectOutDpart();

    // 查询挂号类型
    public List<RecordCategory> selectRecCat();

    // 保存患者挂号信息
    public boolean saveReg(StaffInfo staffInfo, PatientInfo patientInfo,
                           String registerCategory, MedicalRecord medicalRecord);

    // 今日候诊--默认列表
    public PageInfo<MedicalRecord> showListDefault(Integer page, Integer limit);

    // 今日候诊--患者、医生科室
    public PageInfo<MedicalRecord> showList(Integer page, Integer limit,
                                            StaffInfo staffInfo, StaffDepartments staffDepartments,
                                            PatientInfo patientInfo, MedicalRecord medicalRecord);

    // 获取患者信息--患者预诊界面使用
    public Map<String,Object> showPrePatient(MedicalRecord medicalRecord);

    // 获取患者主诉信息列表（mysql方法）
    //public PageInfo<PatientDescribe> showPatientDes(Integer page,Integer limit,String str);

    // 获取患者主诉信息列表（ES方法）
    public Page<PatientDescribe> showPatientDes(Integer page, Integer limit, String str);

    // 更新患者预诊界面数据，更新病历表
    public boolean updatePreMedicalRecord(MedicalRecord medicalRecord);

    // 更新患者信息表，更新患者信息表
    public boolean updatePrePatientInfo(PatientInfo patientInfo);

    // 获取临床诊断信息列表（ES）
    public Page<ClinicDiagnosis> showClinicDia(Integer page, Integer limit, String str);

    // 获取药品条目（ES）
    public Page<MedicineItem> showMedicineItemEs(Integer page, Integer limit, String str, String group);

    // 检索西药用法（ES）
    public Page<WDirection> showWDirectionEs(Integer page, Integer limit, String str);

    // 检索用药频次表
    public PageInfo<MedicineFrequency> showMedicineFrequency(Integer page, Integer limit, MedicineFrequency medicineFrequency);

    // 检索中药用法（ES）
    public Page<CDirection> showCDirectionEs(Integer page, Integer limit, String str);

    // 检索治疗项（ES）
    public Page<ProjectItem> showProjectItemEs(Integer page, Integer limit, String str);

    // 保存医生所有处方
    public boolean saveAllDoctorPrescribe(MedicalRecord medicalRecord, PatientInfo patientInfo, List<WPrescribe> wPrescribes, List<CPrescribe> cPrescribes, List<Project> projects);

    // 获取待收费列表
    public PageInfo<MedicalRecord> showWaitCharge(Integer page, Integer limit, PatientInfo patientInfo);

    // 收费界面获取西药明细
    public List<WPrescribe> showWaitWP(String medicialRecordId);

    // 收费界面获取中药明细
    public List<CPrescribe> showWaitCP(String medicialRecordId);

    // 收费界面获取治疗单明细
    public List<Project> showWaitPro(String medicialRecordId);

    // 窗口缴费确认
    public int chargeMedicalRe(MedicalRecord medicalRecord);

    // 获取窗口待退费列表
    public PageInfo<MedicalRecord> showWaitRefund(Integer page, Integer limit, PatientInfo patientInfo);

    // 根据病历id获取西药退费列表
    public List<Map<String,String>> showWaitRefundWBtch(String medicalRecordId);

    // 根据病历id或者中药退费列表
    public List<Map<String,String>> showWaitRefundCBtch(String medicalRecordId);

    // 根据病历id获取治疗单退费列表
    public List<Map<String,String>> showWaitRefundProBtch(String medicalRecordId);

    // 确认退费按钮
    public boolean refundDetailClick(List<BigInteger> wpreBatch, List<BigInteger> cpreBatch, List<BigInteger> proBatch, BigInteger medicalRecordId);

    // 获取历史处方单
    public PageInfo<MedicalRecord> showHistoryList(Integer page, Integer limit, PatientInfo patientInfo);
}
