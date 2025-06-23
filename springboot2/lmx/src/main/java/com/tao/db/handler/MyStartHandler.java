package com.tao.db.handler;

import com.tao.db.repository.SqlReader;
import com.tao.db.repository.SqlServerRepository;
import com.tao.db.service.OracleService;
import com.tao.db.service.SqlServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 해당 클래스는 app이 실행되고 호출됩니다
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MyStartHandler {
    // private final SqlServerRepository sqlServerRepository;

    private final OracleService oracleService;
    private final SqlServerService sqlServerService;


    @Bean
    public String runAndExit(@Qualifier("secondaryDataSource") DataSource dataSource02, @Qualifier("primaryDataSource") DataSource dataSource01
    , @Qualifier("secondaryJdbcTemplate") JdbcTemplate secondaryJdbcTemplate) {
        log.info("########## secondaryDataSource : {}", dataSource02);
        log.info("########## secondaryDataSource : {}", dataSource02 == dataSource01);

        sqlServerService.selectTest();
        oracleService.selectTest(dataSource02, secondaryJdbcTemplate);
        return "string";
//
//
//        return args -> {
//            // 여기에 실행할 코드 작성
//            System.out.println("내가 만든 Java 파일 실행 중...");
//            myCustomMethod();
//            log.info("##########3 secondaryDataSource : {}", dataSource);
////            List<Map<String, Object>> al = sqlServerRepository.findAllAiReq();
////            for (Map<String, Object> stringObjectMap : al) {
////                log.info("{}", stringObjectMap);
////            }
//        };
    }

    private void myCustomMethod() {
        // 사용자 정의 로직
        System.out.println("사용자 정의 메소드 실행");
    }
}
