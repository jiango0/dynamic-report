package com.dynamic.report.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BasicMapper {

    @Select({"<script>", "${sql}", "</script>"})
    List<Map<String, Object>> select(@Param("sql") String sql);

}
