package com.hcc.mapper;

import com.hcc.pojo.ProBatch;

import java.util.List;

public interface ProBatchMapper {

    // 批量插入治疗批次详情
    public int insertProBatchList(List<ProBatch> proBatchList);

    // 根据治疗id查询治疗批次
    public List<ProBatch> selectProBatchByProId(String projectId);

    // 批量更新退药标志
    public int upadetReturnProBatch(List<ProBatch> proBatches);


    // 批量更新确认退费信息
    public int updateReturnProBath(List<ProBatch> proBatches);
}
