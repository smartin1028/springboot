package com.tao.lmx.xml.utils;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.StringReader;
import org.xml.sax.InputSource;

public class XmlParser {
    public static Document parseXmlString(String xmlStr) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlStr));
        return builder.parse(is);
    }
}
