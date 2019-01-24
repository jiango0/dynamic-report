package com.dynamic.report.dao;

import com.dynamic.report.entity.Hello;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HelloMapper {

    @Select("select * from hello")
    List<Hello> select();

}
