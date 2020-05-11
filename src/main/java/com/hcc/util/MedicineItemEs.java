package com.hcc.util;

import com.hcc.pojo.MedicineItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MedicineItemEs extends ElasticsearchRepository<MedicineItem,Long> {
}
