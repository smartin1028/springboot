package com.tao.db.handler;

import com.tao.db.code.DataSourceType;
import com.tao.db.service.OracleService;
import com.tao.db.service.SqlServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 해당 클래스는 app이 실행되고 호출됩니다
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MyStartHandler {
    // private final SqlServerRepository sqlServerRepository;

    private final SqlServerService sqlServerService;
    private final OracleService oracleService;

    @Value("${spring.datasource.choice:primary}")
    private String choice;

    @Bean
    public String runAndExit() {

        DataSourceType type = DataSourceType.valueOf(choice.toUpperCase());

        if(type == DataSourceType.PRIMARY) {
            sqlServerService.selectTest();
        }else if(type == DataSourceType.SECONDARY) {
            oracleService.selectTest();
        }


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
