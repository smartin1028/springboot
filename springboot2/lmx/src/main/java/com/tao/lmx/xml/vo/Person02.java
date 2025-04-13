package com.tao.lmx.xml.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;

// 1. 먼저 객체 클래스에 JAXB 어노테이션을 추가합니다
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Person02 {
    private String name;
    private int age;

    // getter, setter 생략
}
//
//// 2. 객체를 XML로 변환하는 메서드
//public class XmlConverter {
//    public static String convertToXml(Object object) throws JAXBException {
//        JAXBContext context = JAXBContext.newInstance(object.getClass());
//        Marshaller marshaller = context.createMarshaller();
//
//        // XML 포맷팅 설정
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//        StringWriter sw = new StringWriter();
//        marshaller.marshal(object, sw);
//
//        return sw.toString();
//    }
//}
//