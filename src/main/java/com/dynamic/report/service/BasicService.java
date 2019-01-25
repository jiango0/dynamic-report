package com.dynamic.report.service;

import java.util.List;
import java.util.Map;

public interface BasicService {

    List<Map<String, Object>> select(String sql);

}
