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
@Setter@Getter
@Document(indexName = "medicine_item" ,type = "medicine_item",shards = 3,replicas = 1)
public class MedicineItem {

    @Id
    private BigInteger medicinesId;

    @Field(type = FieldType.Keyword)
    private String medicinesBarcode;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String medicinesName;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String medicinesCode;

    @Field(type = FieldType.Keyword)
    private String medicinesCategory;

    @Field(type = FieldType.Keyword)
    private String dosageForm;

    @Field(type = FieldType.Keyword)
    private String medicinesSpec;

    @Field(type = FieldType.Keyword)
    private double packingSpec; // 包装规格，售卖单位内的药品数

    @Field(type = FieldType.Keyword)
    private String medicinesUnit;

    @Field(type = FieldType.Keyword)
    private String saleUnit; // 最小销售单位

    @Field(type = FieldType.Keyword)
    private Integer currentNumber; // 现库存

    @Field(type = FieldType.Keyword)
    private Integer number; // 入库库存

    @Field(type = FieldType.Keyword)
    private String batchNumber;

    @Field(type = FieldType.Date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    @Field(type = FieldType.Date)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 注解强制把字符串转换为对应的日期格式
    private Date expiryDate;

    @Field(type = FieldType.Date)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date storageDate;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String oem;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String supplier;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String sourceArea; // 中药产地

    @Field(type = FieldType.Keyword)
    private double primePrice;

    @Field(type = FieldType.Keyword)
    private double price;

    @Field(type = FieldType.Keyword)
    private String reviewer;

    @Field(type = FieldType.Date)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Field(type = FieldType.Keyword)
    private String status;


}
