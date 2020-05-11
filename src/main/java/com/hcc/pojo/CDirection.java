package com.hcc.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
//@Getter@Setter
@Document(indexName = "c_direction",type = "c_direction",shards = 3,replicas = 1)
public class CDirection {

    @Id
    private BigInteger id;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String cDirectionText;

    @Field(type = FieldType.Text,analyzer = "analysis-pinyin")
    private String cDirectionCode;

    @Field(type = FieldType.Keyword)
    private String cDirectionStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date)
    private Date updateTime;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getcDirectionText() {
        return cDirectionText;
    }

    public void setcDirectionText(String cDirectionText) {
        this.cDirectionText = cDirectionText;
    }

    public String getcDirectionCode() {
        return cDirectionCode;
    }

    public void setcDirectionCode(String cDirectionCode) {
        this.cDirectionCode = cDirectionCode;
    }

    public String getcDirectionStatus() {
        return cDirectionStatus;
    }

    public void setcDirectionStatus(String cDirectionStatus) {
        this.cDirectionStatus = cDirectionStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
