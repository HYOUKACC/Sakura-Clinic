package com.hcc.mapper;

import com.hcc.pojo.MedicalRecord;

import java.util.List;
import java.util.Map;

public interface MedicalRecordMapper {

    // 保存患者挂号信息
    public int saveReg(MedicalRecord medicalRecord);

    // 获取待就诊患者列表--默认
    public List<MedicalRecord> showWaitListDefault();

    // 获取待就诊患者列表--患者、医生、科室
    public List<MedicalRecord> showWaitList(MedicalRecord medicalRecord);

    // 获取待就诊列表，读取病历--患者预诊界面
    public MedicalRecord showMedicalForPre(MedicalRecord medicalRecord);

    // 更新（患者预诊） 填充预诊单内容
    public int updatePreMedicalRe(MedicalRecord medicalRecord);

    // 更新（医生接诊）填充医生患者病历内容
    public int updateAftMedicalRe(MedicalRecord medicalRecord);

    // 获取待收费列表
    public List<MedicalRecord> showWaitCharge(MedicalRecord medicalRecord);

    // 确认缴费
    public int chargeMedicalRecore(MedicalRecord medicalRecord);

    // 获取待发药列表
    public List<MedicalRecord> showWaitSendMedical(MedicalRecord medicalRecord);

    // 发药窗口修改状态
    public int updateSendMedicine(MedicalRecord medicalRecord);

    // 获取待退药列表
    public List<MedicalRecord> showReturnMedical(MedicalRecord medicalRecord);

    // 修改退药的病历信息，设置is_return=1
    public int updateReturnMedical(MedicalRecord medicalRecord);

    // 获取待退费列表
    public List<MedicalRecord> showWaitRefund(MedicalRecord medicalRecord);

    // 根据病历id进行多表连接-西药
    public List<Map<String,String>> showWaitRefundWPre(String medicalRecordId);

    // // 根据病历id进行多表连接-中药
    public List<Map<String,String>> showWaitRefundCPre(String medicalRecordId);

    // 根据病历id进行多表连接-治疗单
    public List<Map<String,String>> showWaitRefundPro(String medicalRecordId);

    // 确认退费按钮，根据id，修改退费标志
    public int updateRefundMedicalRecord(MedicalRecord medicalRecord);

    // 根据患者获取历史处方单
    public List<MedicalRecord> showHistoryList(MedicalRecord medicalRecord);

}
