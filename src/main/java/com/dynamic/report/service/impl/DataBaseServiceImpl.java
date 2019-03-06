package com.dynamic.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dynamic.report.dao.DataSourceInfoMapper;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Value("classpath:json/data")
    private Resource data;

    @Autowired
    private DataSourceInfoMapper dataSourceInfoMapper;

    public List<DataSourceInfo> list() {
        return dataSourceInfoMapper.list(null);
    }

    public void save(DataSourceInfo dataSourceInfo) {
        dataSourceInfoMapper.save(dataSourceInfo);
    }

    public void delete(String name) {
        JSONArray arrays = JSON.parseArray(readData());
        for(int i=0; i<arrays.size(); i++) {
            JSONObject object = (JSONObject) arrays.get(i);
            if( name.equals(object.get("name")) ) {
                arrays.remove(i);
                break;
            }
        }

        this.writeData(arrays.toString());
    }

    private synchronized String readData() {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(data.getFile()));
            String s = null;
            while ((s = br.readLine()) != null) {
                stringBuffer.append(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stringBuffer.toString();
    }

    private synchronized void writeData(String info) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(data.getFile()));
            bw.write(info);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw != null) {
                    bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
