package com.tao.lmx.controller;

import com.tao.lmx.xml.utils.XmlConverter;
import com.tao.lmx.xml.utils.XmlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Slf4j
@RestController
@RequestMapping("/api")
public class XmlController {

    @PostMapping(value = "/xml",
                consumes = MediaType.TEXT_XML_VALUE,
                produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<String> processXml(@RequestBody String xmlString) throws Exception {
        // xmlString에는 전체 XML 문자열이 포함됩니다
        // XML 문자열을 직접 처리할 수 있습니다

        log.info("xmlString:{}", xmlString);

        try {
            // XML 문자열을 Document 객체로 파싱
            Document doc = XmlParser.parseXmlString(xmlString);

            // Document 객체를 다시 문자열로 변환
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            xmlString = writer.toString();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("XML 처리 중 오류 발생: " + e.getMessage());
        }

//
//        Document document = XmlParser.parseXmlString(xmlString);
//        String s = XmlConverter.convertToXml(document);
//        log.info("xmlString:\n{}\n", s);


        return ResponseEntity.ok(xmlString);
    }
}