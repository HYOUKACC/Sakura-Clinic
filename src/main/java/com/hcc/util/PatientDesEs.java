package com.hcc.util;


import com.hcc.pojo.PatientDescribe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description:定义接口
 * @Param:
 * 	PatientDescribe:为实体类
 * 	Long:为Item实体类中主键的数据类型
 */

public interface PatientDesEs extends ElasticsearchRepository<PatientDescribe,Long> {
}
