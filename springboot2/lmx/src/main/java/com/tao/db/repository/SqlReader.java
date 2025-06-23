package com.tao.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SqlReader {

    private final ResourceLoader resourceLoader;

    public String readSqlFile(String fileName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:sql/" + fileName);
            return new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("SQL 파일을 읽는데 실패했습니다.", e);
        }
    }
}