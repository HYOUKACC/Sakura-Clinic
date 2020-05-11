package com.hcc.mapper;

import com.hcc.pojo.MedicineItem;

import java.util.List;
import java.util.Map;

public interface MedicineItemMapper {

    // 查询药品信息
    public List<MedicineItem> selectAllItem(MedicineItem medicineItem);

    // 添加药品
    public int addMedicine(MedicineItem medicineItem);

    // 修改药品信息
    public int updateMedicine(MedicineItem medicineItem);

    // 修改药品销售状态
    public int updateMedicineStatus(MedicineItem medicineItem);

    // 发药界面，根据药品名查询信息
    public List<MedicineItem> selectMedicalItemByName(MedicineItem medicineItem);

    // 批量更新药品库存，发药界面
    public int updateMedicialCurrentNumber(List<MedicineItem> medicineItems);

    // 批量更新退药库存
    public int updateReturnMedical(List<MedicineItem> medicineItems);


    // 过期监控(自动检查)
    public void expiryDateCheck(String expiryDate);

    // 即将过期数据监控
    public List<MedicineItem> willExpirySelect(MedicineItem medicineItem);

    // 质量监控统计
    public Map<String,String> countQuality(String expiryDate);

    // 销售监控统计
    public Map<String,String> countSale();
}
