package com.dynamic.report.controller;

import com.dynamic.report.common.entity.ResultEntity;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("data/base")
public class DataBaseController {

    @Autowired
    DataBaseService dataBaseService;

    @RequestMapping(value = "list")
    public ResultEntity<List<DataSourceInfo>> list() {
        return ResultEntity.returnSuccess(null);
    }

    @RequestMapping(value = "save")
    public ResultEntity save() {
        return ResultEntity.returnSuccess(null);
    }

    @RequestMapping(value = "delete")
    public ResultEntity delete() {
        return ResultEntity.returnSuccess(null);
    }

}
