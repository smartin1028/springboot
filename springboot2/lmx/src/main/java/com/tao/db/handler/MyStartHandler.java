package com.tao.db.handler;

import com.tao.db.code.DataSourceType;
import com.tao.db.service.DBService;
import com.tao.db.service.OracleService;
import com.tao.db.service.SqlServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 해당 클래스는 app이 실행되고 호출됩니다
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MyStartHandler  {
    // private final SqlServerRepository sqlServerRepository;
    private final DBService sqlServerService;
    private final DBService oracleService;

    @Value("${spring.datasource.choice:primary}")
    private String choice;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        log.info("###### @EventListener(ApplicationReadyEvent.class)");

        // 모든 빈이 초기화된 후에 실행되므로 Proxy 적용 완료 상태
        DataSourceType type = DataSourceType.valueOf(choice.toUpperCase());
        if (type == DataSourceType.PRIMARY) {
            sqlServerService.selectTest();
        } else if (type == DataSourceType.SECONDARY) {
            oracleService.selectTest();
        }
    }


    //    @Bean
    public String runAndExit() {
        DataSourceType type = DataSourceType.valueOf(choice.toUpperCase());
        if (type == DataSourceType.PRIMARY) {
            sqlServerService.selectTest();
        } else if (type == DataSourceType.SECONDARY) {
            oracleService.selectTest();
        }


        return "string";
    }

    private void myCustomMethod() {
        // 사용자 정의 로직
        System.out.println("사용자 정의 메소드 실행");
    }
}
