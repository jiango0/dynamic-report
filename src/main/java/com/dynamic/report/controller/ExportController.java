package com.dynamic.report.controller;

import com.alibaba.druid.util.StringUtils;
import com.dynamic.report.common.entity.SQLEntity;
import com.dynamic.report.entity.BasicResult;
import com.dynamic.report.service.BasicService;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("export")
public class ExportController {

    @Autowired
    BasicService basicService;

    @RequestMapping(value = "")
    public void exportExcel(String sql, HttpServletResponse response) {
        //查询结果
        BasicResult result = basicService.select(sql, "");

        //表头
        List<SQLEntity> sqlEntitieList = result.getSqlEntitieList();
        //数据
        List<Map<String, Object>> list = result.getList();

        List<String> keys = new ArrayList<>();


        int h = 0;
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheetAt = workbook.createSheet();
        SXSSFRow headRow = sheetAt.createRow(h);

        for (int i=0; i<sqlEntitieList.size(); i++) {
            SQLEntity sqlEntity = sqlEntitieList.get(i);
            headRow.createCell(i).setCellValue(!StringUtils.isEmpty(sqlEntity.getFieldName()) ? sqlEntity.getFieldName() : sqlEntity.getAliasCode());
            keys.add(!StringUtils.isEmpty(sqlEntity.getAliasCode()) ? sqlEntity.getAliasCode() : sqlEntity.getField());
        }
        h++;

        for (Map<String, Object> map : list) {
            SXSSFRow row = sheetAt.createRow(h);

            for (int j=0; j<keys.size(); j++) {
                row.createCell(j).setCellValue(map.get(keys.get(j)) == null ? "" : map.get(keys.get(j)).toString());
            }

            h++;
        }

        try {
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + "report.xlsx");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
