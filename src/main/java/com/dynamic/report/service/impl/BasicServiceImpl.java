package com.dynamic.report.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.dynamic.report.common.command.SQLCommand;
import com.dynamic.report.common.entity.SQLEntity;
import com.dynamic.report.common.entity.TableInfo;
import com.dynamic.report.dao.BasicMapper;
import com.dynamic.report.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class BasicServiceImpl implements BasicService {

    @Autowired
    BasicMapper basicMapper;

    public List<Map<String, Object>> select(String sql) {

        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        List<String> tableList = new ArrayList<>();
        for (int i=0; i<sqlStatements.size(); i++) {
            SQLStatement stmt = sqlStatements.get(i);
            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            stmt.accept(visitor);
            Map<String, String> aliasmap = visitor.getAliasMap();
            for (Iterator iterator = aliasmap.keySet().iterator(); iterator.hasNext();) {
                tableList.add(iterator.next().toString());
            }
        }

        if (tableList.size() == 1) {
            SQLCommand.formatSql(sql, null);
        }

        List<Map<String, Object>> select = basicMapper.select(sql);

        List<SQLEntity> sqlEntitieList = SQLCommand.analyzeSql(sql);
        List<TableInfo> tableInfoList = this.getTableInfo(tableList);

        return basicMapper.select(sql);
    }

    public List<TableInfo> getTableInfo(List<String> tableList) {
        List<TableInfo> tableInfoList = new ArrayList<>();
        for(String tableName : tableList) {
            List<Map<String, String>> maps = basicMapper.tableInfo(tableName);
            for(Map<String, String> map : maps ) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setColumnName(map.get("column_name"));
                tableInfo.setColumnComment(map.get("column_comment"));

                tableInfoList.add(tableInfo);
            }
        }

        return tableInfoList;
    }

}
