package com.tao.db.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
//@Configuration
public class OracleDBConfig {
    // Oracle 설정 값 주입
    @Value("${spring.oracle.datasource.url}")
    private String url;

    @Value("${spring.oracle.datasource.username}")
    private String username;

    @Value("${spring.oracle.datasource.password}")
    private String password;

    @Value("${spring.oracle.datasource.driver-class-name}")
    private String driverClassName;

    // Oracle DataSource 빈 생성
    @Bean(name = "oracleDataSource")
    public DataSource getOracleDataSource() {
        log.info("oracle url : {}", url);
        log.info("oracle driverClassName : {}", driverClassName);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);


//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.url(url);
//        dataSourceBuilder.username(username);
//        dataSourceBuilder.password(password);
//
//        return new LazyConnectionDataSourceProxy(
//            new DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build()
//        );
        return dataSource;
    }

    @Override
    public String toString() {
        return "OracleDBConfig{" +
                "oracleUrl='" + url + '\'' +
                ", oracleDriverClassName='" + driverClassName + '\'' +
                '}';
    }
}
