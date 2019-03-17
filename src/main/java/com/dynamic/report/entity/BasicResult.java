package com.dynamic.report.entity;

import com.dynamic.report.common.entity.SQLEntity;

import java.util.List;
import java.util.Map;

public class BasicResult {

    private List<Map<String, Object>> list;

    private List<SQLEntity> sqlEntitieList;

    private String sql;

    private List<SQLCondition> conditions;

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public List<SQLEntity> getSqlEntitieList() {
        return sqlEntitieList;
    }

    public void setSqlEntitieList(List<SQLEntity> sqlEntitieList) {
        this.sqlEntitieList = sqlEntitieList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<SQLCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<SQLCondition> conditions) {
        this.conditions = conditions;
    }
}
