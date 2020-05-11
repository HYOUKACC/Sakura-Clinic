package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class DosageForm {

    private BigInteger dosageFormId;

    private String dosageFormName;

    private String dosageFormCode;

    private String dosageFormStatus;

}
