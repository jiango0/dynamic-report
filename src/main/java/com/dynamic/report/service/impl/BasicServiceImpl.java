package com.dynamic.report.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.StringUtils;
import com.dynamic.report.common.command.SQLCommand;
import com.dynamic.report.common.entity.ColumnInfo;
import com.dynamic.report.common.entity.SQLEntity;
import com.dynamic.report.common.entity.TableInfo;
import com.dynamic.report.dao.BasicMapper;
import com.dynamic.report.entity.BasicResult;
import com.dynamic.report.entity.SQLCondition;
import com.dynamic.report.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class BasicServiceImpl implements BasicService {

    @Autowired
    BasicMapper basicMapper;

    public BasicResult select(String sql) {
        BasicResult basicResult = this.list(sql);
        sql = SQLCommand.removeCondition(sql);
        sql = sql + " limit 100";
        List<Map<String, Object>> select = basicMapper.select(sql);
        basicResult.setList(select);

        return basicResult;
    }

    public BasicResult list(String sql) {
        BasicResult basicResult = new BasicResult();
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        List<TableInfo> tableInfoList = new ArrayList<>();
        for (int i=0; i<sqlStatements.size(); i++) {
            SQLStatement stmt = sqlStatements.get(i);
            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            stmt.accept(visitor);
            //tables
            Map<TableStat.Name, TableStat> tabmap = visitor.getTables();
            for (Iterator iterator = tabmap.keySet().iterator(); iterator.hasNext();) {
                TableStat.Name name = (TableStat.Name) iterator.next();
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(name.getName());
                tableInfoList.add(tableInfo);
            }
            //alias
            Map<String, String> aliasmap = visitor.getAliasMap();
            for (Iterator iterator = aliasmap.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next().toString();
                String value = aliasmap.get(key);

                for (TableInfo tableInfo : tableInfoList) {
                    if (tableInfo.getTableName().equals(value) && !key.equals(value)) {
                        tableInfo.setTableAlias(key);
                        break;
                    }
                }
            }
        }

        if (tableInfoList.size() == 1) {
            TableInfo tableInfo = tableInfoList.get(0);
            sql = SQLCommand.formatSql(sql, tableInfo);
        }

        Map<String, String> tableInfo = this.getTableInfo(tableInfoList);
        List<SQLEntity> sqlEntitieList = SQLCommand.analyzeSql(sql);
        for (SQLEntity sqlEntity : sqlEntitieList) {
            sqlEntity.setFieldName(tableInfo.get(sqlEntity.getFieldCode()));
        }
        basicResult.setSqlEntitieList(sqlEntitieList);
        basicResult.setSql(sql);

        if (SQLCommand.containCondition(sql)) {
            List<SQLCondition> sqlConditions = SQLCommand.analyzeCondition(sql);
            if (!CollectionUtils.isEmpty(sqlConditions)) {
                basicResult.setConditions(sqlConditions);
            }
        }

        return basicResult;
    }

    public Map<String, String> getTableInfo(List<TableInfo> tableInfoList) {

        for (TableInfo tableInfo : tableInfoList) {
            List<Map<String, String>> maps = basicMapper.tableInfo(tableInfo.getTableName());
            for (Map<String, String> map : maps) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(map.get("column_name"));
                columnInfo.setColumnComment(map.get("column_comment"));
                if (tableInfo.getList() == null) {
                    tableInfo.setList(new ArrayList<>());
                }
                tableInfo.getList().add(columnInfo);
            }
        }

        Map<String, String> param = new HashMap<>();
        for (TableInfo tableInfo : tableInfoList) {
            for (ColumnInfo columnInfo : tableInfo.getList()) {
                param.put(tableInfo.getTableName() + "." + columnInfo.getColumnName(), columnInfo.getColumnComment());
                if (!StringUtils.isEmpty(tableInfo.getTableAlias())) {
                    param.put(tableInfo.getTableAlias() + "." + columnInfo.getColumnName(), columnInfo.getColumnComment());
                }
            }
        }

        return param;
    }

}
