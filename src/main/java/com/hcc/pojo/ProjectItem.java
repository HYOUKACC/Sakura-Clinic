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
@Document(indexName = "project_item",type = "project_item",shards = 3,replicas = 1)
public class ProjectItem {

    @Id
    private BigInteger projectItemId;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String projectName;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String projectCode;

    @Field(type = FieldType.Keyword)
    private double price;

    @Field(type = FieldType.Keyword)
    private String projectStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date)
    private Date updateTime;

}
