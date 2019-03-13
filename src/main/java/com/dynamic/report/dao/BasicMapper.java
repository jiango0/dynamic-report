package com.dynamic.report.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BasicMapper {

    @Select({"<script>", "${sql}", "</script>"})
    List<Map<String, Object>> select(@Param("sql") String sql);

    @Select({"<script>", "SELECT column_name, column_comment FROM INFORMATION_SCHEMA.Columns WHERE table_name = '${tableName}'", "</script>"})
    List<Map<String, String>> tableInfo(@Param("tableName") String tableName);

}
