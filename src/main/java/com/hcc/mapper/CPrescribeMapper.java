package com.hcc.mapper;

import com.hcc.pojo.CPrescribe;

import java.util.List;

public interface CPrescribeMapper {

    // 存储中药处方单
    public int saveAllCP(List<CPrescribe> cPrescribes);

    // 根据病历id获取
    public List<CPrescribe> selectCPByMrId(String medicalRecordId);

//    // 根据病历id获取中药待发药列表
//    public List<CPrescribe> selectWaitSendCPByMrId(String medicalRecordId);

    // 发药窗口，批量更新西药处方单的is_send
    public int updateSendMedical(List<CPrescribe> prescribes);


    // 退药窗口，批量更新西药处方单is_return
    public int updateReturnMedical(List<CPrescribe> prescribes);

}
