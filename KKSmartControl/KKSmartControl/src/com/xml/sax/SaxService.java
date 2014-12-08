package com.xml.sax;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory; 
 

public class SaxService { 

	public static List<HashMap<String, String>> readXML(
			InputStream inputStream, String nodeName) {
		try {
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();// 解析xml
			XMLHandler xmlhandler = new XMLHandler(nodeName);
			parser.parse(inputStream, xmlhandler);
			inputStream.close();
			return xmlhandler.getList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
