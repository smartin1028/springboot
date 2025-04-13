package com.tao.lmx.controller;

import com.tao.lmx.xml.utils.XmlConverter;
import com.tao.lmx.xml.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;

@Slf4j
@RestController
@RequestMapping("/api")
public class PersonController {

    @PostMapping(value = "/person",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PersonVO> createPerson(@RequestBody PersonVO personVO) {
        // personVO 객체를 사용하여 비즈니스 로직 처리
        log.info("{}", personVO);
        try {
            String xml = XmlConverter.convertToXml(personVO);
            log.info(xml);
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }

        return ResponseEntity.ok(personVO);
    }
//
//    @GetMapping(value = "/person/{id}",
//                produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PersonVO> getPerson(@PathVariable String id) {
//        // id를 사용하여 PersonVO 객체 조회
//        PersonVO personVO = // ... 조회 로직
//        return ResponseEntity.ok(personVO);
//    }
}
