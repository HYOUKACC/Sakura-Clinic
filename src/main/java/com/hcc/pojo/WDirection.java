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
@Document(indexName = "w_direction",type = "w_direction",shards = 3,replicas = 1)
public class WDirection {

    @Id
    private BigInteger id;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String wDirectionText;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String wDirectionCode;

    @Field(type = FieldType.Keyword)
    private int wDirectionStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date)
    private Date updateTime;


}
