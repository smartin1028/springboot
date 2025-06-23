package com.tao.db.service;

import com.tao.db.repository.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;

@Service
@Slf4j
@Transactional(transactionManager = "secondaryTransactionManager", readOnly = true)
@RequiredArgsConstructor
public class OracleService {
    private final SqlReader sqlReader;


    private final DataSource primaryDataSource;
    private final PlatformTransactionManager secondaryTransactionManager;

    public void selectTest(DataSource secondaryDataSource, JdbcTemplate secondaryJdbcTemplate) {

        // 현재 트랜잭션 매니저 로그 출력
        log.info("Current Transaction Manager: {}", TransactionAspectSupport.currentTransactionStatus());
        log.info("Current Transaction Name: {}", TransactionSynchronizationManager.getCurrentTransactionName());
        log.info("Actual DataSource: {}", TransactionSynchronizationManager.getResource(secondaryDataSource));


        TransactionTemplate transactionTemplate = new TransactionTemplate(secondaryTransactionManager);
        transactionTemplate.setReadOnly(true);

        transactionTemplate.execute(status -> {
            log.info("######### : {}", secondaryDataSource == primaryDataSource);

            // 현재 트랜잭션 매니저 로그 출력
            log.info("##### Current Transaction Manager: {}", TransactionAspectSupport.currentTransactionStatus());
            log.info("##### Current Transaction Name: {}", TransactionSynchronizationManager.getCurrentTransactionName());
            log.info("##### Actual DataSource: {}", TransactionSynchronizationManager.getResource(secondaryDataSource));

            // 트랜잭션 내에서 실행할 코드
            String sql = sqlReader.readSqlFile("oracle-test-01.sql");
            List<String> integers = secondaryJdbcTemplate.queryForList(sql, String.class);
            for (String integer : integers) {
                log.info("oracle : {}", integer);
            }
            return null;
        });
//        String sql = sqlReader.readSqlFile("oracle-test-01.sql");
//        List<String> integers = secondaryJdbcTemplate.queryForList(sql, String.class);
//        for (String integer : integers) {
//            log.info("oracle : {}", integer);
//        }
    }
}
