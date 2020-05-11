package com.hcc.util;


import com.hcc.pojo.ProjectItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProjectItemEs extends ElasticsearchRepository<ProjectItem,Long> {
}
