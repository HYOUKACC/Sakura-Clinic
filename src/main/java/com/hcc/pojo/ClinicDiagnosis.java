package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Getter@Setter
@Document(indexName = "clinic_diagnosis" ,type = "clinic_diagnosis",shards = 3,replicas = 1)
public class ClinicDiagnosis {

    @Id // 标记为主键
    private BigInteger clinicDiagnosisId;

    // 查询的主要内容，需要分词
    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String clinicDiagnosisCode;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String clinicDiagnosisCategory;

    // 查询的主要内容，需要分词
    @Field(type = FieldType.Keyword)
    private String clinicDiagnosisText;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date)
    private Date updateTime;

    // 不用分词
    @Field(type = FieldType.Keyword)
    private String diagnosisStatus;
}
