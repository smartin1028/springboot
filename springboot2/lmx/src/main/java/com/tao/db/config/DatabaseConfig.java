package com.tao.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final Environment env;  // Spring이 자동으로 주입
//    private final OracleDBConfig oracleDBConfig;
//    private final SqlServerDBConfig serverDBConfig;

    // 첫 번째 데이터소스 설정
    @Primary
    @Bean(name = "primaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        HikariConfig config = new HikariConfig();
        // 필수 속성 설정
        config.setJdbcUrl(env.getProperty("spring.datasource.primary.jdbc-url"));
        config.setUsername(env.getProperty("spring.datasource.primary.username"));
        config.setPassword(env.getProperty("spring.datasource.primary.password"));
        config.setDriverClassName(env.getProperty("spring.datasource.primary.driver-class-name"));

        extracted(config, "Primary");

        DataSource dataSource = new HikariDataSource(config);
        log.info("######## sql server datasource: {}", dataSource);
        return dataSource;
    }

    private static void extracted(HikariConfig config, String str) {
        String username = config.getUsername();
        log.info("##### " + str + " DataSource username: {}", username);
        String jdbcUrl = config.getJdbcUrl();
        log.info("##### " + str + " DataSource jdbcUrl: {}", jdbcUrl);
    }

    // 두 번째 데이터소스 설정
    @Bean(name = "secondaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        HikariConfig config = new HikariConfig();
        // 필수 속성 설정
        config.setJdbcUrl(env.getProperty("spring.datasource.secondary.jdbc-url"));
        config.setUsername(env.getProperty("spring.datasource.secondary.username"));
        config.setPassword(env.getProperty("spring.datasource.secondary.password"));
        config.setDriverClassName(env.getProperty("spring.datasource.secondary.driver-class-name"));

        extracted(config, "Secondary");

        DataSource dataSource = new HikariDataSource(config);
        log.info("######## sql oracle datasource: {}", dataSource);
        return dataSource;
    }

    // 첫 번째 JdbcTemplate
    @Primary
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // 두 번째 JdbcTemplate
    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // 첫 번째 TransactionManager
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // 두 번째 TransactionManager
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}