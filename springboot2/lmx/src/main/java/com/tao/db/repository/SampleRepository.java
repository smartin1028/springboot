package com.tao.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SampleRepository {

    private final JdbcTemplate mssqlJdbcTemplate;
    // 단순 쿼리 실행 예제
    public List<Map<String, Object>> findAllUsers() {
        String sql = "SELECT * FROM Users";
        return mssqlJdbcTemplate.queryForList(sql);
    }

    // 파라미터 바인딩 예제
    public String findUserNameById(Long id) {
        String sql = "SELECT name FROM Users WHERE id = ?";
        return mssqlJdbcTemplate.queryForObject(sql, String.class, id);
    }

    // INSERT 예제
    public int insertUser(String name, String email) {
        String sql = "INSERT INTO Users (name, email) VALUES (?, ?)";
        return mssqlJdbcTemplate.update(sql, name, email);
    }
}