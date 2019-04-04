package com.dynamic.report.entity;

public class SQLCondition {

    public enum moduleType {
        input,
        date,
        combox
    }

    private String moduleCode;

    private String moduleName;

    private String moduleField;

    private String moduleType;

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleField() {
        return moduleField;
    }

    public void setModuleField(String moduleField) {
        this.moduleField = moduleField;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
