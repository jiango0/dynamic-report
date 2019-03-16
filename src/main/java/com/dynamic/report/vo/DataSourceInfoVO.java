package com.dynamic.report.vo;

import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.entity.ModelInfo;

import java.util.List;

public class DataSourceInfoVO extends DataSourceInfo {

    private List<ModelInfo> modelInfoList;

    public List<ModelInfo> getModelInfoList() {
        return modelInfoList;
    }

    public void setModelInfoList(List<ModelInfo> modelInfoList) {
        this.modelInfoList = modelInfoList;
    }
}
