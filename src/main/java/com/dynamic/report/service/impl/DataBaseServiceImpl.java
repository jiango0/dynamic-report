package com.dynamic.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dynamic.report.common.cache.DataSourceCache;
import com.dynamic.report.common.datasource.CreateDataSource;
import com.dynamic.report.dao.DataSourceInfoMapper;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.entity.ModelInfo;
import com.dynamic.report.service.DataBaseService;
import com.dynamic.report.service.ModelInfoService;
import com.dynamic.report.vo.DataSourceInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Value("classpath:json/data")
    private Resource data;

    @Autowired
    private DataSourceInfoMapper dataSourceInfoMapper;

    @Autowired
    private ModelInfoService modelInfoService;

    public List<DataSourceInfoVO> listAll() {
        List<DataSourceInfoVO> result = null;
        List<DataSourceInfo> list = dataSourceInfoMapper.list(null);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> ids = list.stream().map(DataSourceInfo::getId).collect(Collectors.toList());
            Map<String, Object> param = new HashMap<>();
            param.put("ids", ids);
            List<ModelInfo> modelInfoList = modelInfoService.list(param);
            Map<Long, List<ModelInfo>> modelInfoMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(modelInfoList)) {
                modelInfoList.forEach(modelInfo -> {
                    if (modelInfoMap.containsKey(modelInfo.getDataSourceId())) {
                        modelInfoMap.get(modelInfo.getDataSourceId()).add(modelInfo);
                    } else {
                        modelInfoMap.put(modelInfo.getDataSourceId(),
                                new ArrayList<ModelInfo>(){{
                                    add(modelInfo);
                                }}
                        );
                    }
                });
            }
            result = list.stream().map(dataSourceInfo -> {
                DataSourceInfoVO vo = new DataSourceInfoVO();
                BeanUtils.copyProperties(dataSourceInfo, vo);
                vo.setModelInfoList(modelInfoMap.get(dataSourceInfo.getId()));
                return vo;
            }).collect(Collectors.toList());
        }

        return result;
    }

    public List<DataSourceInfo> list() {
        return dataSourceInfoMapper.list(null);
    }

    public void save(DataSourceInfo dataSourceInfo) {
        dataSourceInfoMapper.save(dataSourceInfo);
        DataSource dataSource = CreateDataSource.get(
                dataSourceInfo.getDriverClassName(),
                dataSourceInfo.getUrl(),
                dataSourceInfo.getUsername(),
                dataSourceInfo.getPassword()
        );
        DataSourceCache.cacheDataSource.put(dataSourceInfo.getId().toString(), dataSource);
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
