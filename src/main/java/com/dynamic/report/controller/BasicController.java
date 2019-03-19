package com.dynamic.report.controller;

import com.dynamic.report.common.entity.ResultEntity;
import com.dynamic.report.entity.BasicResult;
import com.dynamic.report.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("basic")
public class BasicController {

    @Autowired
    BasicService basicService;

    @RequestMapping(value = "sql")
    public ResultEntity<BasicResult> execute(String sql) {
        return ResultEntity.returnSuccess(basicService.select(sql, " limit 100 "));
    }

    @RequestMapping(value = "getTableHead")
    public ResultEntity<BasicResult> getTableHead(String sql) {
        return ResultEntity.returnSuccess(basicService.list(sql));
    }

}
