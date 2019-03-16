package com.dynamic.report.service;

import com.dynamic.report.entity.ModelInfo;

import java.util.List;
import java.util.Map;

public interface ModelInfoService {

    void save(ModelInfo modelInfo);

    List<ModelInfo> list(Map<String, Object> param);

    ModelInfo detail(Long id);

}
