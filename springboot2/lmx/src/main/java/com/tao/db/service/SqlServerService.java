package com.tao.db.service;

import com.tao.db.repository.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SqlServerService implements DBService {
    @Lazy
    private final JdbcTemplate jdbcTemplate;
    @Lazy
    private final SqlReader sqlReader;
    @Lazy
    private final DataSource dataSource;

    public void selectTest() {
        boolean isProxy = AopUtils.isAopProxy(this);
        log.info("isProxy : {}", isProxy);

        log.info("Current Transaction Manager: {}", TransactionAspectSupport.currentTransactionStatus());
        log.info("Current Transaction Name: {}", TransactionSynchronizationManager.getCurrentTransactionName());
        log.info("Actual DataSource: {}", TransactionSynchronizationManager.getResource(dataSource));
        log.info("#### current transaction read only: {}", TransactionSynchronizationManager.isCurrentTransactionReadOnly());

        String sql = sqlReader.readSqlFile("sqlserver-test-01.sql");
        List<String> strings = jdbcTemplate.queryForList(sql, String.class);
        for (String str : strings) {
            log.info("sql server int : {}", str);
        }

    }

}
