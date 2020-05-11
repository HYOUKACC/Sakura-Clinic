package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class RecordCategory {

    private BigInteger id;
    private String category;
    private double price;

}
