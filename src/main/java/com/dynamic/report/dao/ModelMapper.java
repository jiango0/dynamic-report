package com.dynamic.report.dao;

import com.dynamic.report.entity.ModelInfo;

import java.util.List;
import java.util.Map;

public interface ModelMapper {

    void save(ModelInfo modelInfo);

    int update(ModelInfo modelInfo);

    List<ModelInfo> list(Map<String, Object> param);

}
