package com.dynamic.report.controller;

import com.dynamic.report.common.datasource.SwitchDataSource;
import com.dynamic.report.dao.BasicMapper;
import com.dynamic.report.dao.HelloMapper;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.entity.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Autowired
    HelloMapper helloMapper;

    @Autowired
    BasicMapper basicMapper;

    @RequestMapping(value = "welcome")
    public List<Hello> welcome(@RequestBody DataSourceInfo dataSourceInfo) {
        String switchKey = dataSourceInfo.getSwitchKey();
        if(!"master".equals(switchKey)) {
            SwitchDataSource.setDataSources(dataSourceInfo);
        }
        List<Hello> select = helloMapper.select();
        SwitchDataSource.clear();

        return select;
    }

    @RequestMapping(value = "basic")
    public List<Map<String, Object>> basic(HttpServletRequest request, String sql) {
        String name = request.getHeader("name");
        String driverClassName = request.getHeader("driverClassName");
        String url = request.getHeader("url");
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        dataSourceInfo.setName(name);
        dataSourceInfo.setDriverClassName(driverClassName);
        dataSourceInfo.setUrl(url);
        dataSourceInfo.setUsername(username);
        dataSourceInfo.setPassword(password);
        SwitchDataSource.setDataSources(dataSourceInfo);

        return basicMapper.select(sql);
    }


}
