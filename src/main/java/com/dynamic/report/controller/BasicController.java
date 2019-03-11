package com.dynamic.report.controller;

import com.dynamic.report.common.command.SQLCommand;
import com.dynamic.report.common.entity.ResultEntity;
import com.dynamic.report.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("basic")
public class BasicController {

    @Autowired
    BasicService basicService;

    @RequestMapping(value = "sql")
    public ResultEntity<List<Map<String, Object>>> execute(String sql) {
        return ResultEntity.returnSuccess(basicService.select(sql));
    }

}
