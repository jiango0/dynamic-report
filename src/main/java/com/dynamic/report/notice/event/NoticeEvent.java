package com.dynamic.report.notice.event;

import com.dynamic.report.entity.DataSourceInfo;
import org.springframework.context.ApplicationEvent;

public class NoticeEvent extends ApplicationEvent {

    private DataSourceInfo dataSourceInfo;

    public NoticeEvent(Object source, DataSourceInfo dataSourceInfo) {
        super(source);
        this.dataSourceInfo = dataSourceInfo;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }
}
