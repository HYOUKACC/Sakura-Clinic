package com.hcc.util;

import com.hcc.pojo.ClinicDiagnosis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClinicDiagnosisEs extends ElasticsearchRepository<ClinicDiagnosis,Long> {
}
