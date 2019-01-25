package com.dynamic.report.controller;

import com.dynamic.report.common.entity.ResultEntity;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
        return ResultEntity.returnSuccess(dataBaseService.list());
    }

    @RequestMapping(value = "save")
    public ResultEntity save(@RequestBody DataSourceInfo dataSourceInfo) {
        dataBaseService.save(dataSourceInfo);
        return ResultEntity.returnSuccess(1);
    }

    @RequestMapping(value = "delete")
    public ResultEntity delete(String name) {
        dataBaseService.delete(name);
        return ResultEntity.returnSuccess(1);
    }

}
