# 스프링에서 애플리케이션 시작 후 사용자 정의 Java 파일 실행 및 종료 방법

스프링 부트 애플리케이션에서 시작 후 특정 Java 코드를 실행하고 애플리케이션을 종료시키는 방법을 설명드리겠습니다.

## 1. CommandLineRunner 또는 ApplicationRunner 인터페이스 구현

가장 간단한 방법은 `CommandLineRunner` 또는 `ApplicationRunner` 인터페이스를 구현하는 것입니다.

```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Bean
    public CommandLineRunner runAndExit() {
        return args -> {
            // 여기에 실행할 코드 작성
            System.out.println("내가 만든 Java 파일 실행 중...");
            myCustomMethod();
            
            // 애플리케이션 종료
            System.exit(0);
        };
    }
    
    private void myCustomMethod() {
        // 사용자 정의 로직
        System.out.println("사용자 정의 메소드 실행");
    }
}
```

## 2. ApplicationReadyEvent 리스너 사용

애플리케이션이 완전히 시작된 후에 코드를 실행하려면 `ApplicationReadyEvent`를 리스닝할 수 있습니다.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("애플리케이션이 시작된 후 실행되는 코드");
        myCustomMethod();
        
        // 애플리케이션 종료
        System.exit(0);
    }
    
    private void myCustomMethod() {
        // 사용자 정의 로직
    }
}
```

## 3. 별도의 서비스 컴포넌트 생성

더 구조화된 접근 방식:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyApplication.class, args);
        
        MyCustomService service = context.getBean(MyCustomService.class);
        service.executeAndExit();
    }
}

@Service
public class MyCustomService {
    public void executeAndExit() {
        // 비즈니스 로직 실행
        System.out.println("비즈니스 로직 실행 중...");
        
        // 애플리케이션 종료
        System.exit(0);
    }
}
```

## 주의사항

1. `System.exit(0)`는 JVM을 완전히 종료시킵니다. 필요한 경우에만 사용하세요.
2. 프로덕션 환경에서는 이런 방식의 강제 종료를 피하는 것이 좋습니다.
3. 종료 전에 필요한 정리 작업(리소스 해제 등)을 수행해야 합니다.
4. 종료 코드(exit code)는 관례에 따라 성공 시 0, 오류 시 1 이상을 사용합니다.

이 방법들 중 하나를 선택하여 스프링 부트 애플리케이션이 시작된 후 사용자 정의 코드를 실행하고 종료시킬 수 있습니다.