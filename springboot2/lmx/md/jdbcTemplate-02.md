# Spring Boot에서 JdbcTemplate을 수동으로 설정하여 MSSQL과 Oracle 동시 사용하기

Spring Boot에서 `@Value` 어노테이션을 사용하여 JdbcTemplate을 수동으로 설정하고, MSSQL과 Oracle 데이터베이스를 동시에 사용하는 방법을 설명드리겠습니다.

## 1. 기본 설정

먼저 `application.properties` 또는 `application.yml` 파일에 두 데이터베이스의 연결 정보를 설정합니다.

### application.properties 예시

```properties
# MSSQL 설정
spring.mssql.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=mydb
spring.mssql.datasource.username=sa
spring.mssql.datasource.password=password
spring.mssql.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Oracle 설정
spring.oracle.datasource.url=jdbc:oracle:thin:@localhost:1521:ORCL
spring.oracle.datasource.username=system
spring.oracle.datasource.password=password
spring.oracle.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

## 2. DataSource 및 JdbcTemplate 설정 클래스

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    // MSSQL 설정 값 주입
    @Value("${spring.mssql.datasource.url}")
    private String mssqlUrl;
    
    @Value("${spring.mssql.datasource.username}")
    private String mssqlUsername;
    
    @Value("${spring.mssql.datasource.password}")
    private String mssqlPassword;
    
    @Value("${spring.mssql.datasource.driver-class-name}")
    private String mssqlDriverClassName;

    // Oracle 설정 값 주입
    @Value("${spring.oracle.datasource.url}")
    private String oracleUrl;
    
    @Value("${spring.oracle.datasource.username}")
    private String oracleUsername;
    
    @Value("${spring.oracle.datasource.password}")
    private String oraclePassword;
    
    @Value("${spring.oracle.datasource.driver-class-name}")
    private String oracleDriverClassName;

    // MSSQL DataSource 빈 생성
    @Bean(name = "mssqlDataSource")
    public DataSource mssqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(mssqlDriverClassName);
        dataSource.setUrl(mssqlUrl);
        dataSource.setUsername(mssqlUsername);
        dataSource.setPassword(mssqlPassword);
        return dataSource;
    }

    // Oracle DataSource 빈 생성
    @Bean(name = "oracleDataSource")
    public DataSource oracleDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(oracleDriverClassName);
        dataSource.setUrl(oracleUrl);
        dataSource.setUsername(oracleUsername);
        dataSource.setPassword(oraclePassword);
        return dataSource;
    }

    // MSSQL JdbcTemplate 빈 생성
    @Bean(name = "mssqlJdbcTemplate")
    public JdbcTemplate mssqlJdbcTemplate() {
        return new JdbcTemplate(mssqlDataSource());
    }

    // Oracle JdbcTemplate 빈 생성
    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate() {
        return new JdbcTemplate(oracleDataSource());
    }
}
```

## 3. 사용 방법

서비스 클래스에서 각각의 JdbcTemplate을 사용할 때는 `@Qualifier` 어노테이션을 사용하여 구분합니다.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    private final JdbcTemplate mssqlJdbcTemplate;
    private final JdbcTemplate oracleJdbcTemplate;

    @Autowired
    public DatabaseService(
            @Qualifier("mssqlJdbcTemplate") JdbcTemplate mssqlJdbcTemplate,
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate) {
        this.mssqlJdbcTemplate = mssqlJdbcTemplate;
        this.oracleJdbcTemplate = oracleJdbcTemplate;
    }

    public void queryFromMssql() {
        String sql = "SELECT * FROM mssql_table";
        mssqlJdbcTemplate.query(sql, (rs, rowNum) -> {
            // 결과 처리
            return null;
        });
    }

    public void queryFromOracle() {
        String sql = "SELECT * FROM oracle_table";
        oracleJdbcTemplate.query(sql, (rs, rowNum) -> {
            // 결과 처리
            return null;
        });
    }
}
```

## 4. 주의사항

1. **의존성 추가**: 프로젝트에 MSSQL과 Oracle JDBC 드라이버를 추가해야 합니다.
   - Maven의 경우:
     ```xml
     <!-- MSSQL -->
     <dependency>
         <groupId>com.microsoft.sqlserver</groupId>
         <artifactId>mssql-jdbc</artifactId>
         <version>9.4.1.jre11</version>
     </dependency>
     
     <!-- Oracle -->
     <dependency>
         <groupId>com.oracle.database.jdbc</groupId>
         <artifactId>ojdbc8</artifactId>
         <version>21.5.0.0</version>
     </dependency>
     ```

2. **트랜잭션 관리**: 두 데이터베이스에 걸친 트랜잭션을 관리하려면 JTA(Java Transaction API)를 사용해야 합니다.

3. **성능 고려**: 연결 풀을 사용하려면 `DriverManagerDataSource` 대신 `HikariCP`나 다른 커넥션 풀을 설정하는 것이 좋습니다.

4. **예외 처리**: 각 데이터베이스마다 다른 SQL 예외가 발생할 수 있으므로 적절한 예외 처리가 필요합니다.

이 방법을 통해 Spring Boot에서 두 개의 다른 데이터베이스(MSSQL과 Oracle)를 동시에 사용하면서 JdbcTemplate을 수동으로 설정할 수 있습니다.