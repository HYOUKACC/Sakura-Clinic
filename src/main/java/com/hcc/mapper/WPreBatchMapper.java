package com.hcc.mapper;

import com.hcc.pojo.WPreBatch;

import java.util.List;

public interface WPreBatchMapper {


    // 批量插入西药批次详情表
    public int insertWPreBatch(List<WPreBatch> wPreBatches);

    // 根据wprescribeId获取列表
    public List<WPreBatch> selectWPreBatchByWPId(String wprescribeId);

    // 批量更新退药标志
    public int upadetReturnWPreBatch(List<WPreBatch> wPreBatches);

    // 批量更新确认退费标志
    public int updateRefundWPreBatch(List<WPreBatch> wPreBatches);
}
