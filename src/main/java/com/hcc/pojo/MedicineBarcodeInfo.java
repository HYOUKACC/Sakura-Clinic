package com.hcc.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter@Setter
public class MedicineBarcodeInfo {

   private String medicinesBarcode;
   private String medicinesName;
   private String medicinesCode;
   private String oem;
   private String medicineImg;

}
