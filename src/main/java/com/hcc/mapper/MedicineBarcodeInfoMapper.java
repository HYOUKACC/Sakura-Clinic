package com.hcc.mapper;

import com.hcc.pojo.MedicineBarcodeInfo;

import java.util.List;

public interface MedicineBarcodeInfoMapper {

    // 根据条码信息，查询本地药品数据库
    public List<MedicineBarcodeInfo> selectByBar(MedicineBarcodeInfo medicineBarcodeInfo);

    // 存储网络数据
    public void addWebInfo(MedicineBarcodeInfo medicineBarcodeInfo);


}
