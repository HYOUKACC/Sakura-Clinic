package com.hcc.mapper;

import com.hcc.pojo.WPrescribe;

import java.util.List;

public interface WPrescribeMapper {

    // 批量插入西药处方(int 0/1 成功或失败)
    public int insertAllWP(List<WPrescribe> wPrescribes);


    // 根据病历id获取
    public List<WPrescribe> selectWPByMrId(String medicalRecordId);

//    // 根据病历id获取西药待发药列表
//    public List<WPrescribe> selectWaitSendWPByMrId(String medicalRecordId);

    // 发药窗口，批量更新西药处方单的is_send
    public int updateSendMedical(List<WPrescribe> prescribes);

    // 退药窗口，批量更新西药处方单is_return
    public int updateReturnMedical(List<WPrescribe> prescribes);

}
