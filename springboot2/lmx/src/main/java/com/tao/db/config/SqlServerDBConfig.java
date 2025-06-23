package com.tao.db.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
//@Configuration
public class SqlServerDBConfig {
    // MSSQL 설정 값 주입
    @Value("${spring.mssql.datasource.url}")
    private String url;

    @Value("${spring.mssql.datasource.username}")
    private String username;

    @Value("${spring.mssql.datasource.password}")
    private String password;

    @Value("${spring.mssql.datasource.driver-class-name}")
    private String driverClassName;

    @Bean(name = "mssqlDataSource")
    public DataSource getDataSource() {
        log.info("url : {}", url);
        log.info("driverClassName : {}", driverClassName);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Override
    public String toString() {
        return "SqlServerDBConfig{" +
                "mssqlUrl='" + url + '\'' +
                ", mssqlDriverClassName='" + driverClassName + '\'' +
                '}';
    }
}
