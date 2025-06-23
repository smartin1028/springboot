package com.tao.db.handler;

import com.tao.db.code.DataSourceType;
import com.tao.db.service.DBService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class AppStartupRunner implements CommandLineRunner {

    private final DBService sqlServerService;
    private final DBService oracleService;

    @Value("${spring.datasource.choice:primary}")
    private String choice;
    @Override
    public void run(String... args) throws Exception {
        // 애플리케이션 시작 완료 후 실행할 코드
        System.out.println("애플리케이션이 완전히 시작된 후 실행됩니다.");
        yourMethod();
    }

    private void yourMethod() {
        // 실행할 비즈니스 로직
                // 모든 빈이 초기화된 후에 실행되므로 Proxy 적용 완료 상태
        DataSourceType type = DataSourceType.valueOf(choice.toUpperCase());
        if (type == DataSourceType.PRIMARY) {
            sqlServerService.selectTest();
        } else if (type == DataSourceType.SECONDARY) {
            oracleService.selectTest();
        }
    }
}