package com.dynamic.report.service.impl;

import com.dynamic.report.dao.ModelMapper;
import com.dynamic.report.entity.ModelInfo;
import com.dynamic.report.service.ModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelInfoServiceImpl implements ModelInfoService {

    @Autowired
    ModelMapper modelMapper;

    public void save(ModelInfo modelInfo) {
        if (modelInfo.getId() == null) {
            modelMapper.save(modelInfo);
        } else {
            modelMapper.update(modelInfo);
        }
    }

    public List<ModelInfo> list(Map<String, Object> param) {
        return modelMapper.list(param);
    }

    public ModelInfo detail(Long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        List<ModelInfo> list = modelMapper.list(param);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }

        return null;
    }

}
