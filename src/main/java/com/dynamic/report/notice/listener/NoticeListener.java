package com.dynamic.report.notice.listener;

import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.notice.event.NoticeEvent;
import com.dynamic.report.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NoticeListener {

    @Autowired
    private DataBaseService dataBaseService;

    @EventListener
    public void message(NoticeEvent noticeEvent) {
        DataSourceInfo dataSourceInfo = noticeEvent.getDataSourceInfo();
        dataBaseService.save(dataSourceInfo);
    }

}
