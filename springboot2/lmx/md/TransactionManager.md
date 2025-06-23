# Spring Boot 2.7에서 다중 데이터소스 구성 및 ReadOnly 설정 방법

Spring Boot 2.7 버전에서 2개 이상의 데이터베이스를 사용하고, 특정 데이터소스에 ReadOnly 기능을 부여하는 방법을 상세히 설명드리겠습니다.

## 1. 기본적인 다중 데이터소스 구성

### 1.1. application.properties 설정

```properties
# 첫 번째 데이터소스 (기본)
spring.datasource.url=jdbc:mysql://localhost:3306/primary_db
spring.datasource.username=user1
spring.datasource.password=pass1
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# 두 번째 데이터소스
spring.second-datasource.url=jdbc:mysql://localhost:3306/secondary_db
spring.second-datasource.username=user2
spring.second-datasource.password=pass2
spring.second-datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 1.2. 데이터소스 구성 클래스

```java
@Configuration
public class DataSourceConfig {

    // 기본 데이터소스 (Primary)
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 두 번째 데이터소스
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
```

## 2. ReadOnly 데이터소스 설정 방법

### 2.1. ReadOnly 전용 데이터소스 구성

```java
@Configuration
@EnableTransactionManagement
public class ReadOnlyDataSourceConfig {

    @Bean(name = "readOnlyDataSource")
    @ConfigurationProperties(prefix = "spring.readonly-datasource")
    public DataSource readOnlyDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "readOnlyTransactionManager")
    public PlatformTransactionManager readOnlyTransactionManager(
            @Qualifier("readOnlyDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "readOnlyJdbcTemplate")
    public JdbcTemplate readOnlyJdbcTemplate(
            @Qualifier("readOnlyDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

### 2.2. ReadOnly 트랜잭션 사용

```java
@Service
public class SomeService {

    private final JdbcTemplate readOnlyJdbcTemplate;

    public SomeService(@Qualifier("readOnlyJdbcTemplate") JdbcTemplate readOnlyJdbcTemplate) {
        this.readOnlyJdbcTemplate = readOnlyJdbcTemplate;
    }

    @Transactional(readOnly = true, transactionManager = "readOnlyTransactionManager")
    public List<SomeData> getReadOnlyData() {
        return readOnlyJdbcTemplate.query("SELECT * FROM some_table", 
            (rs, rowNum) -> new SomeData(rs.getString("column1"), rs.getString("column2")));
    }
}
```

## 3. JPA를 사용하는 경우의 다중 데이터소스 구성

### 3.1. JPA 엔티티 매니저 구성

```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.primary.repository",
    entityManagerFactoryRef = "primaryEntityManagerFactory",
    transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDataSourceConfig {

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.primary.model")
                .persistenceUnit("primary")
                .build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.secondary.repository",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfig {

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.secondary.model")
                .persistenceUnit("secondary")
                .build();
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
```

### 3.2. ReadOnly JPA 리포지토리 구성

```java
@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.readonly.repository",
    entityManagerFactoryRef = "readOnlyEntityManagerFactory",
    transactionManagerRef = "readOnlyTransactionManager"
)
public class ReadOnlyDataSourceConfig {

    @Bean(name = "readOnlyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean readOnlyEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("readOnlyDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.connection.provider_disables_autocommit", "true");
        properties.put("hibernate.connection.is-connection-validation-required", "false");
        
        return builder
                .dataSource(dataSource)
                .packages("com.example.readonly.model")
                .persistenceUnit("readOnly")
                .properties(properties)
                .build();
    }

    @Bean(name = "readOnlyTransactionManager")
    public PlatformTransactionManager readOnlyTransactionManager(
            @Qualifier("readOnlyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
```

## 4. 주의사항 및 추가 설정

1. **트랜잭션 전파**: `@Transactional` 어노테이션을 사용할 때 올바른 트랜잭션 매니저를 지정해야 합니다.
2. **풀 설정**: 각 데이터소스에 대한 커넥션 풀을 별도로 구성하는 것이 좋습니다.
3. **모니터링**: 다중 데이터소스 사용 시 모니터링이 더 복잡해질 수 있으므로 적절한 모니터링 도구를 구성해야 합니다.
4. **테스트**: 다중 데이터소스 환경에서는 테스트 설정도 각 데이터소스에 맞게 구성해야 합니다.

이러한 설정을 통해 Spring Boot 2.7에서 다중 데이터베이스를 효과적으로 사용하고, 특정 데이터소스에 ReadOnly 기능을 부여할 수 있습니다.