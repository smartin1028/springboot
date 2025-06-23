# Spring Boot에서 JDBCTemplate로 SQL Server 테스트 방법

## 1. 프로젝트 설정

### 의존성 추가 (pom.xml 또는 build.gradle)

#### Maven (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Starter JDBC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    
    <!-- SQL Server JDBC 드라이버 -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- 테스트용 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Gradle (build.gradle)
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    runtimeOnly 'com.microsoft.sqlserver:mssql-jdbc'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## 2. 프로퍼티 설정 (application.properties 또는 application.yml)

### application.properties 방식
```properties
# SQL Server 연결 설정
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=YourDatabaseName;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JdbcTemplate 설정
spring.jdbc.template.max-rows=1000
spring.jdbc.template.query-timeout=30
```

### application.yml 방식
```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=YourDatabaseName;encrypt=true;trustServerCertificate=true
    username: your_username
    password: your_password
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
  jdbc:
    template:
      max-rows: 1000
      query-timeout: 30
```

## 3. JdbcTemplate 사용 예제

### Repository 클래스 예제
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SampleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SampleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 단순 쿼리 실행 예제
    public List<Map<String, Object>> findAllUsers() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.queryForList(sql);
    }

    // 파라미터 바인딩 예제
    public String findUserNameById(Long id) {
        String sql = "SELECT name FROM Users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    // INSERT 예제
    public int insertUser(String name, String email) {
        String sql = "INSERT INTO Users (name, email) VALUES (?, ?)";
        return jdbcTemplate.update(sql, name, email);
    }
}
```

## 4. 테스트 클래스 작성

### 통합 테스트 예제
```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SampleRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    public void testDatabaseConnection() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result);
    }

    @Test
    public void testFindAllUsers() {
        // 테스트 데이터 삽입
        jdbcTemplate.update("INSERT INTO Users (name, email) VALUES (?, ?)", "TestUser", "test@example.com");
        
        // 테스트 실행
        var users = sampleRepository.findAllUsers();
        
        // 검증
        assertFalse(users.isEmpty());
    }

    @Test
    public void testFindUserNameById() {
        // 테스트 데이터 삽입
        jdbcTemplate.update("INSERT INTO Users (name, email) VALUES (?, ?)", "TestUser", "test@example.com");
        Long id = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);
        
        // 테스트 실행
        String userName = sampleRepository.findUserNameById(id);
        
        // 검증
        assertEquals("TestUser", userName);
    }
}
```

## 5. 테스트용 프로퍼티 설정 (test/resources/application.properties)

테스트 환경에서는 별도의 데이터베이스를 사용할 수 있도록 설정:

```properties
# 테스트용 SQL Server 연결 설정 (H2 인메모리 DB 예시)
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# 테스트 시에만 스키마 생성
spring.datasource.initialization-mode=always
spring.datasource.schema=classpath:schema.sql
spring.datasource.data=classpath:data.sql
```

## 6. 테스트용 스키마 및 데이터 파일 (선택사항)

### schema.sql (test/resources/)
```sql
CREATE TABLE IF NOT EXISTS Users (
    id BIGINT IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);
```

### data.sql (test/resources/)
```sql
INSERT INTO Users (name, email) VALUES ('Test User 1', 'test1@example.com');
INSERT INTO Users (name, email) VALUES ('Test User 2', 'test2@example.com');
```

## 7. 프로파일별 설정 (선택사항)

환경별로 다른 설정을 적용하려면:

### application-dev.properties (개발용)
```properties
spring.datasource.url=jdbc:sqlserver://dev-server:1433;databaseName=DevDB
spring.datasource.username=dev_user
spring.datasource.password=dev_password
```

### application-prod.properties (운영용)
```properties
spring.datasource.url=jdbc:sqlserver://prod-server:1433;databaseName=ProdDB
spring.datasource.username=prod_user
spring.datasource.password=prod_password
```

### 테스트 실행 시 프로파일 지정
```java
@SpringBootTest
@ActiveProfiles("dev") // 개발용 프로파일 사용
public class SampleRepositoryDevTest {
    // 테스트 코드
}
```

이렇게 설정하면 Spring Boot 애플리케이션에서 JdbcTemplate을 사용하여 SQL Server와 연동하고, 프로퍼티 파일에서 설정을 관리할 수 있습니다. 테스트 환경에서는 실제 SQL Server 대신 H2 같은 인메모리 데이터베이스를 사용할 수 있습니다.