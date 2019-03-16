package com.dynamic.report.controller;

import com.dynamic.report.common.entity.ResultEntity;
import com.dynamic.report.entity.ModelInfo;
import com.dynamic.report.service.ModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("model")
public class ModelInfoController {

    @Autowired
    ModelInfoService modelInfoService;

    @RequestMapping(value = "save")
    public ResultEntity save(@RequestBody ModelInfo modelInfo) {
        modelInfoService.save(modelInfo);
        return new ResultEntity();
    }

    @RequestMapping(value = "detail")
    public ResultEntity<ModelInfo> detail(Long id) {
        return ResultEntity.returnSuccess(modelInfoService.detail(id));
    }

}
