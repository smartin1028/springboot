package com.tao.lmx.xml.vo;

import com.tao.lmx.xml.utils.XmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.*;

@Slf4j
public class PersonVOTest {

    @Test
    public void t_Person02Test_true_00() {
        // 사용 예시:
        PersonVO personVO = new PersonVO("001", "홍길동", 30, "hong@example.com");
        personVO.getPhoneNumbers().add("010-1234-5678");
        personVO.getPhoneNumbers().add("02-999-9999");
        personVO.getHobbies().add("독서");
        personVO.getHobbies().add("등산");

        PersonVO.AddressVO addressVO = new PersonVO.AddressVO();
        addressVO.setStreet("강남대로 123");
        addressVO.setCity("서울");
        addressVO.setZipCode("06000");
        addressVO.setCountry("대한민국");
        personVO.setAddress(addressVO);

        try {
            String xml = XmlConverter.convertToXml(personVO);
            log.info(xml);

        } catch (JAXBException e) {
            log.error(e.getMessage());
        }

    }
}