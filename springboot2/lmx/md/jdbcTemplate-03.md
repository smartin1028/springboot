# Secondary TransactionManager 사용 문제 해결

문제 상황을 분석해보니 `@Transactional(transactionManager = "secondaryTransactionManager")`를 명시했음에도 기본(default) 트랜잭션 매니저를 사용하려는 문제가 발생하고 있습니다. 이에 대한 해결 방안을 제시드리겠습니다.

## 문제 원인 분석

1. **트랜잭션 매니저 빈 이름 불일치**: `secondaryTransactionManager`라는 이름의 빈이 정확히 정의되어 있는지 확인 필요
2. **트랜잭션 관리자 설정 문제**: Spring 설정에서 트랜잭션 관리가 제대로 구성되지 않았을 가능성
3. **프록시 생성 문제**: AOP 프록시가 제대로 생성되지 않아 어노테이션이 적용되지 않을 수 있음

## 해결 방법

### 1. 트랜잭션 매니저 빈 설정 확인

먼저 `secondaryTransactionManager` 빈이 정확히 정의되어 있는지 확인하세요:

```java
@Configuration
public class DatabaseConfig {
    
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    // 다른 설정들...
}
```

### 2. 클래스 수준과 메서드 수준 어노테이션 확인

현재 코드에서는 클래스와 메서드 모두에 `@Transactional`이 적용되어 있습니다. 이 경우 메서드 수준의 어노테이션이 우선 적용됩니다. 현재는 동일하게 설정되어 문제가 없지만, 혹시 다른 설정이 있는지 확인하세요.

### 3. 트랜잭션 로그 추가

더 자세한 로그를 추가하여 문제를 진단할 수 있습니다:

```java
@Transactional(transactionManager = "secondaryTransactionManager", readOnly = true)
public void selectTest() {
    // 현재 활성화된 트랜잭션 매니저 확인
    TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
    DataSourceTransactionManager txManager = 
        (DataSourceTransactionManager) status.getTransactionManager();
    log.info("Actual TransactionManager Bean Name: {}", txManager.getBeanName());
    
    // DataSource 확인
    DataSource actualDataSource = txManager.getDataSource();
    log.info("Actual DataSource: {}", actualDataSource);
    
    // 나머지 코드...
}
```

### 4. Spring 설정 확인

`@EnableTransactionManagement` 어노테이션이 적용되어 있는지 확인하세요:

```java
@SpringBootApplication
@EnableTransactionManagement
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 5. 트랜잭션 매니저 강제 지정

만약 위 방법으로 해결되지 않는다면, 프로그래밍 방식으로 트랜잭션 매니저를 지정할 수 있습니다:

```java
public void selectTest() {
    TransactionTemplate transactionTemplate = new TransactionTemplate(secondaryTransactionManager);
    transactionTemplate.setReadOnly(true);
    
    transactionTemplate.execute(status -> {
        // 트랜잭션 내에서 실행할 코드
        String sql = sqlReader.readSqlFile("oracle-test-01.sql");
        List<String> integers = secondaryJdbcTemplate.queryForList(sql, String.class);
        for (String integer : integers) {
            log.info("oracle : {}", integer);
        }
        return null;
    });
}
```

### 6. AOP 프록시 생성 확인

Spring이 트랜잭션 프록시를 생성하는지 확인하기 위해 다음 로그를 추가하세요:

```java
@PostConstruct
public void init() {
    log.info("Is this class a proxy? {}", AopUtils.isAopProxy(this));
    log.info("Is this class a JDK dynamic proxy? {}", AopUtils.isJdkDynamicProxy(this));
    log.info("Is this class a CGLIB proxy? {}", AopUtils.isCglibProxy(this));
}
```

## 추가 확인 사항

1. `secondaryJdbcTemplate`이 `secondaryDataSource`를 사용하도록 설정되어 있는지 확인
2. Spring Boot를 사용하는 경우 `application.properties`/`application.yml`에서 트랜잭션 관련 설정 확인
3. 다른 AOP 설정이 트랜잭션 AOP와 충돌하지 않는지 확인

이러한 점들을 확인하면 문제를 해결할 수 있을 것입니다. 만약 그래도 해결되지 않는다면 전체 설정 코드와 로그 출력을 함께 공유해주시면 더 정확한 진단이 가능할 것입니다.