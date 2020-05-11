package com.hcc.util;

import com.hcc.pojo.CDirection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CDirectionEs extends ElasticsearchRepository<CDirection,Long> {
}
