package com.dynamic.report.common.entity;

public class SQLEntity {

    private String fieldName;

    private String fieldCode;

    private String aliasCode;

    private String field;

    private boolean subQuery;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getAliasCode() {
        return aliasCode;
    }

    public void setAliasCode(String aliasCode) {
        this.aliasCode = aliasCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isSubQuery() {
        return subQuery;
    }

    public void setSubQuery(boolean subQuery) {
        this.subQuery = subQuery;
    }
}
