package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class MedicineFrequency {

    private BigInteger medicineFrequencyId;

    private String medicineFrequencyCode;

    private String medicineFrequencyText;

    private double frequencyTimes;

    private double frequencyNumber;

    private String frequencyUnit;

    private String frequencyStatus;
}
