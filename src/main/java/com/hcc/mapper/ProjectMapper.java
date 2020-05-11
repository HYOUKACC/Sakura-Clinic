package com.hcc.mapper;

import com.hcc.pojo.Project;

import java.util.List;

public interface ProjectMapper {


    // 存储所有的治疗单
    public int insertAllPro(List<Project> projects);

    // 根据病历id获取
    public List<Project> selectProByMrId(String medicalRecordId);

    // 根据病历id获取治疗单待发药列表
    public List<Project> selectWaitSendProByMrId(String medicalRecordId);

    // 批量更新治疗单
    public int updateProList(List<Project> projects);


    // 批量更新退药信息
    public int updateReturnMedical(List<Project> prescribes);



}
