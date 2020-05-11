package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Data
@Getter@Setter
public class MedicinesUnit {

    private BigInteger medicinesUnitId;

    private String medicinesUnitText;

    private String medicinesUnitCode;

    private String medicinesUnitStatus;
}
