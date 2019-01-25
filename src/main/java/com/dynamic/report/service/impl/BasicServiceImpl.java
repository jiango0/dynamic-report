package com.dynamic.report.service.impl;

import com.dynamic.report.dao.BasicMapper;
import com.dynamic.report.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BasicServiceImpl implements BasicService {

    @Autowired
    BasicMapper basicMapper;

    public List<Map<String, Object>> select(String sql) {
        return basicMapper.select(sql);
    }

}
