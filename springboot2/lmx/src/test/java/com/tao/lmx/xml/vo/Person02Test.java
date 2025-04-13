package com.tao.lmx.xml.vo;

import com.tao.lmx.xml.utils.XmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;

@Slf4j
public class Person02Test {

    @Test
    public void t_Person02Test_true_00() {
        Person02 person = new Person02();
        person.setName("John");
        person.setAge(30);

        try {
            String xml = XmlConverter.convertToXml(person);
            System.out.println(xml);
            /**
             * 결과
             * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
             * <person02>
             *     <name>John</name>
             *     <age>30</age>
             * </person02>
             */
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }

    }

}