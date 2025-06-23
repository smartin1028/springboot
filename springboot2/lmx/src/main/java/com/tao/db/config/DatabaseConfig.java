package com.tao.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final Environment env;  // Spring이 자동으로 주입

    @Value("${spring.datasource.choice:primary}")
    private String choice;

    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
//        config.setReadOnly(true);
        config.setMaximumPoolSize(1);

        env.getProperty("spring.datasource.jdbc-url");
        String firstName = "spring.datasource."+choice;
        // 필수 속성 설정
        config.setJdbcUrl(env.getProperty(firstName + ".jdbc-url"));
        config.setUsername(env.getProperty(firstName + ".username"));
        config.setPassword(env.getProperty(firstName + ".password"));
        config.setDriverClassName(env.getProperty(firstName + ".driver-class-name"));

        extracted(config, choice);

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

    @Bean
    public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // 첫 번째 TransactionManager
    @Bean
    public PlatformTransactionManager primaryTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}