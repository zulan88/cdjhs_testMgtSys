package net.wanji.openx.service;

import net.wanji.openx.generated.OpenScenario;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

// 生成XML并添加注释的代码
public class JAXBExample {
    public static void main(String[] args) throws Exception {
        // 创建一个新的Document对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

// 创建XML结构
        Element rootElement = doc.createElement("root");
        doc.appendChild(rootElement);

        Element childElement = doc.createElement("child");
        childElement.setTextContent("This is the content of child element");
        rootElement.appendChild(childElement);

// 创建注释并添加到XML中
        Comment comment = doc.createComment("This is a comment");
        Comment comment2 = doc.createComment("This is a comment2");
        rootElement.appendChild(comment);
        rootElement.appendChild(comment2);

// 设置格式化选项
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 设置缩进
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // 设置缩进的空格数

// 将Document对象转换为XML字符串
//        DOMSource source = new DOMSource(doc);
//        StringWriter stringWriter = new StringWriter();
//        StreamResult result = new StreamResult(stringWriter);
//        transformer.transform(source, result);
//
//        String xmlString = stringWriter.toString();
//        System.out.println(xmlString);

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("D:\\data\\uploadPath\\scenelib\\xiaotest.xosc"));
        transformer.transform(source, result);
    }
}
