package com.hcc.util;

import com.hcc.pojo.WDirection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WDirectionEs extends ElasticsearchRepository<WDirection,Long> {
}
