package com.hcc.mapper;

import com.hcc.pojo.CPreBatch;

import java.util.List;

public interface CPreBatchMapper {

    // 批量插入中药批次详情
    public int insertCPreBatchList(List<CPreBatch> cPreBatches);

    // 根据wprescribeId获取列表
    public List<CPreBatch> selectCPreBatchByWPId(String cprescribeId);


    // 批量更新退药标志
    public int upadetReturnCPreBatch(List<CPreBatch> cPreBatches);

    // 批量更新确认退费信息
    public int updateRefundCPreBatch(List<CPreBatch> cPreBatches);
}
