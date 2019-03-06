package com.dynamic.report.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration

public class DynamicSqlSessionFactory {

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        return new DynamicDataSourceConfigurer();
    }

}
