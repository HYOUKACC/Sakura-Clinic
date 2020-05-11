package com.hcc.util;

import com.hcc.pojo.CPreBatch;
import com.hcc.pojo.ProBatch;
import com.hcc.pojo.WPreBatch;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class SendMedicalDetail {

    private Map< String,List<WPreBatch> > wPBatchData;

    private Map< String,List<CPreBatch> > cPBatchData;

    private List<ProBatch> projectBatch;

    private Map<String,List<ProBatch>> proBatchData;

    public Map<String, List<ProBatch>> getProBatchData() {
        return proBatchData;
    }

    public void setProBatchData(Map<String, List<ProBatch>> proBatchData) {
        this.proBatchData = proBatchData;
    }

    private BigInteger medicalRecordId;

    public BigInteger getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(BigInteger medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public List<ProBatch> getProjectBatch() {
        return projectBatch;
    }

    public void setProjectBatch(List<ProBatch> projectBatch) {
        this.projectBatch = projectBatch;
    }

    public Map<String, List<WPreBatch>> getwPBatchData() {
        return wPBatchData;
    }

    public void setwPBatchData(Map<String, List<WPreBatch>> wPBatchData) {
        this.wPBatchData = wPBatchData;
    }

    public Map<String, List<CPreBatch>> getcPBatchData() {
        return cPBatchData;
    }

    public void setcPBatchData(Map<String, List<CPreBatch>> cPBatchData) {
        this.cPBatchData = cPBatchData;
    }


}
