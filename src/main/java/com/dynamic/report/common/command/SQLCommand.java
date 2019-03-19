package com.dynamic.report.common.command;

import com.dynamic.report.common.entity.ColumnInfo;
import com.dynamic.report.common.entity.SQLEntity;
import com.dynamic.report.common.entity.TableInfo;
import com.dynamic.report.entity.SQLCondition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLCommand {

    public static String FROM = "from";

    public static String CONDITION_FLAG = "@@";

    public static String ALL_RESULT = "*";

    public static String formatSql(String sql, TableInfo tableInfo) {
        StringBuilder stringBuffer = new StringBuilder("select ");
        int index = SQLCommand.getSelectIndex(sql);
        String[] selectArr = sql.substring(6, index).split("[,]");
        for(int i=0; i<selectArr.length; i++) {
            String select = selectArr[i].trim();
            if (!select.contains(".")) {
                stringBuffer.append(!StringUtils.isEmpty(tableInfo.getTableAlias()) ? tableInfo.getTableAlias() : tableInfo.getTableName());
                stringBuffer.append(".");
            }
            stringBuffer.append(select);
            stringBuffer.append(" ");
            if (i != selectArr.length -1) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(sql.substring(index));

        return stringBuffer.toString();
    }

    public static List<SQLEntity> analyzeSql (String sql) {
        List<SQLEntity> result = new ArrayList<>();
        String[] sqlArr = SQLCommand.getSelectArr(sql);
        for(String entity : sqlArr) {
            entity = entity.trim();
            SQLEntity sqlEntity = new SQLEntity();

            if (entity.contains("(") && entity.contains(")")) {
                sqlEntity.setSubQuery(Boolean.TRUE);
                sqlEntity.setFieldCode(entity.substring(0, entity.indexOf(")") + 1).trim());
                String alias = entity.substring(entity.indexOf(")") + 1);
                if (alias.contains("as")) {
                    sqlEntity.setAliasCode(alias.substring(alias.indexOf("as") + 2).trim());
                }
            }
            else if (entity.contains("as")) {
                String[] asArr = entity.split("as");
                sqlEntity.setAliasCode(asArr[asArr.length-1].trim());
                sqlEntity.setFieldCode(asArr[0].trim());
                sqlEntity.setField(asArr[0].substring(asArr[0].indexOf(".")+1).trim());
            }
            else if (entity.contains(" ")) {
                String[] spaceArr = entity.split("[ ]");
                sqlEntity.setAliasCode(spaceArr[spaceArr.length-1].trim());
                sqlEntity.setFieldCode(spaceArr[0].trim());
                sqlEntity.setField(spaceArr[0].substring(spaceArr[0].indexOf(".")+1).trim());
            }
            else {
                sqlEntity.setFieldCode(entity);
                sqlEntity.setField(entity.substring(entity.indexOf(".")+1).trim());
            }

            result.add(sqlEntity);
        }

        return result;
    }

    /**
     * 查询SQL中是否包含 “*”
     * @param   sql
     * @return  sql
     * */
    public static String replaceSQL (String sql, List<TableInfo> tableInfoList) {
        StringBuffer result = new StringBuffer("select ");

        Map<String, List<ColumnInfo>> tableMap = new HashMap<>();
        for (TableInfo tableInfo : tableInfoList) {
            tableMap.put(tableInfo.getTableName(), tableInfo.getList());
            tableMap.put(tableInfo.getTableAlias(), tableInfo.getList());
        }
        int index = SQLCommand.getSelectIndex(sql);
        String[] sqlArr = sql.substring(6, index).split("[,]");
        for (String field : sqlArr) {
            StringBuffer stringBuffer = new StringBuffer(field);
            if (field.contains("*")) {
                String[] fieldArr = field.split("[.]");
                List<ColumnInfo> columnInfos = tableMap.get(fieldArr[0].trim());
                if (!CollectionUtils.isEmpty(columnInfos)) {
                    stringBuffer.delete(0, stringBuffer.length());
                    for (int i=0; i<columnInfos.size(); i++) {
                        if (i != 0) {
                            stringBuffer.append(",");
                        }
                        stringBuffer.append(fieldArr[0]);
                        stringBuffer.append(".");
                        stringBuffer.append(columnInfos.get(i).getColumnName());
                    }
                }
            }

            result.append(stringBuffer.toString());
            result.append(" ");
        }

        result.append(sql.substring(index));

        return result.toString();
    }

    public static boolean containCondition(String sql) {
        return sql.contains(SQLCommand.CONDITION_FLAG) || sql.contains("{") || sql.contains("}");
    }

    public static List<SQLCondition> analyzeCondition(String sql) {
        List<SQLCondition> list = new ArrayList<>();
        int index = 0;
        while (sql.indexOf(SQLCommand.CONDITION_FLAG, index) != -1) {
            SQLCondition sqlCondition = new SQLCondition();
            index = sql.indexOf(SQLCommand.CONDITION_FLAG, index);
            String moduleCode = sql.substring(index, sql.indexOf("}", index) + 1);
            String field = moduleCode.substring(moduleCode.indexOf("{") + 1, moduleCode.indexOf("}"));
            if (field.contains(",")) {
                String[] fieldArray = field.split("[,]");
                sqlCondition.setModuleField(fieldArray[0]);
                sqlCondition.setModuleName(fieldArray[1]);
            } else {
                sqlCondition.setModuleField(field);
                sqlCondition.setModuleName(field);
            }
            sqlCondition.setModuleCode(moduleCode);

            SQLCondition.moduleType[] values = SQLCondition.moduleType.values();
            for (SQLCondition.moduleType value : values) {
                if (sqlCondition.getModuleField().contains(value.name())) {
                    sqlCondition.setModuleType(value.name());
                }
            }
            if (StringUtils.isEmpty(sqlCondition.getModuleType())) {
                sqlCondition.setModuleType(SQLCondition.moduleType.input.name());
            }

            list.add(sqlCondition);
            index++;
        }

        return list;
    }

    public static String removeCondition(String sql) {
        if (sql.contains(SQLCommand.CONDITION_FLAG)) {
            String where = "";
            int index = sql.lastIndexOf("where");
            String whereSQL = sql.substring(index + 6);
            String[] whereArray = whereSQL.split("and|or");
            for (String key : whereArray) {
                if (!key.contains(SQLCommand.CONDITION_FLAG)) {
                    where += key;
                }
            }
            if (where.length() > 0) {
                where = sql.substring(0, index + 6) + where;
            } else {
                where = sql.substring(0, index);
            }

            return where;
        }

        return sql;
    }

    public static int getSelectIndex(String sql) {
        sql = sql.toLowerCase();
        int index = sql.length() - 1;
        while (sql.lastIndexOf(SQLCommand.FROM, index -1) != -1) {
            index = sql.lastIndexOf(SQLCommand.FROM, index - 1);
            int l = sql.lastIndexOf("(", index);
            int r = sql.lastIndexOf(")", index);
            if (l != -1 && r != -1 && l - r > 0) {
                continue;
            } else if (l != -1  && r == -1) {
                continue;
            }

            break;
        }

        return index;
    }

    public static String[] getSelectArr(String sql) {
        int index = SQLCommand.getSelectIndex(sql);
        return sql.substring(6, index).split("[,]");
    }

}
