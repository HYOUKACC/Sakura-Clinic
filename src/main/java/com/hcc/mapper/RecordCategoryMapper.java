package com.hcc.mapper;

import com.hcc.pojo.RecordCategory;

import java.util.List;

public interface RecordCategoryMapper {

    // 查询挂号类型
    public List<RecordCategory> selectAll();

}
