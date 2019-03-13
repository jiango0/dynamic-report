package com.dynamic.report.common.entity;

import java.util.List;

public class TableInfo {

    private String tableName;

    private String tableAlias;

    private List<ColumnInfo> list;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public List<ColumnInfo> getList() {
        return list;
    }

    public void setList(List<ColumnInfo> list) {
        this.list = list;
    }
}
