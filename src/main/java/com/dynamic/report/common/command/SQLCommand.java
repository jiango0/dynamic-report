package com.dynamic.report.common.command;

import com.dynamic.report.common.entity.SQLEntity;
import com.dynamic.report.common.entity.TableInfo;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SQLCommand {

    public static String FROM = "from";

    public static String formatSql(String sql, TableInfo tableInfo) {
        StringBuilder stringBuffer = new StringBuilder("select ");
        int index = SQLCommand.getSelectIndex(sql);
        String[] selectArr = sql.substring(6, index).split("[,]");
        for(int i=0; i<selectArr.length; i++) {
            String select = selectArr[i];
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
                sqlEntity.setAliasCode(entity.substring(entity.indexOf(")") + 1).trim());
                sqlEntity.setFieldCode(entity.substring(0, entity.indexOf(")") + 1).trim());
            }
            else if (entity.contains("as")) {
                String[] asArr = entity.split("as");
                sqlEntity.setAliasCode(asArr[asArr.length-1].trim());
                sqlEntity.setFieldCode(asArr[0].trim());
            }
            else if (entity.contains(" ")) {
                String[] spaceArr = entity.split(" ");
                sqlEntity.setAliasCode(spaceArr[spaceArr.length-1].trim());
                sqlEntity.setFieldCode(spaceArr[0].trim());
            }

            result.add(sqlEntity);
        }

        return result;
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
