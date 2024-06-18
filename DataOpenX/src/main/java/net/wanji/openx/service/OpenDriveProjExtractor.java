package net.wanji.openx.service;

import net.wanji.common.utils.StringUtils;
import net.wanji.openx.generated.OpenScenario;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class OpenDriveProjExtractor {
    public static void main(String[] args) throws IOException {
        String address = "D:\\data\\uploadPath\\scenelib\\tjtest2.xosc";
        String online = "C:\\Users\\wanji\\Downloads\\tjtest.xodr";
//        trackentry(address);
        try {
            File inputFile = new File(address);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList privateList = doc.getElementsByTagName("Private");
            for (int temp = 0; temp < privateList.getLength(); temp++) {
                Node privateNode = privateList.item(temp);
                if (privateNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element privateElement = (Element) privateNode;
                    NamedNodeMap attributes = privateElement.getAttributes();
                    Node entityRefNode = attributes.getNamedItem("entityRef");
                    if (entityRefNode != null && entityRefNode.getNodeValue().equals("Ego")) {
                        System.out.println(privateElement.getChildNodes().item(1).getNextSibling().getTextContent());;
//                        privateNode.getParentNode().insertBefore(comment, privateNode);
                        break;
                    }
                }
            }

            // Write the updated document back to the XML file
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(new File("D:\\data\\uploadPath\\scenelib\\tjtest2.xosc"));
//            transformer.transform(source, result);
//
//            System.out.println("Comment added successfully to <Private entityRef=\"Ego\"> element.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackentry(String address) {
        File file = new File(address);
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(OpenScenario.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            OpenScenario openScenario = (OpenScenario) jaxbUnmarshaller.unmarshal(file);
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();

            // 设置格式化输出，即添加换行和缩进
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // 设置字符编码
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            // 生成XML
            marshaller.marshal(openScenario, writer);
            String xmlString = writer.toString();

            // 使用DOM解析添加注释
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            // 创建注释节点
            Comment p1 = doc.createComment(
                    "Information of the ego vehicle will be hidden, and its initial state and driving task will be explained in the comments below");

            NodeList privateList = doc.getElementsByTagName("Private");
            for (int temp = 0; temp < privateList.getLength(); temp++) {
                Node privateNode = privateList.item(temp);
                if (privateNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element privateElement = (Element) privateNode;
                    NamedNodeMap attributes = privateElement.getAttributes();
                    Node entityRefNode = attributes.getNamedItem("entityRef");
                    if (entityRefNode != null && entityRefNode.getNodeValue().equals("Ego")) {
                        privateNode.getParentNode().insertBefore(p1, privateNode);
                        break;
                    }
                }
            }

            // 输出带注释的XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(System.out));

        } catch (JAXBException | ParserConfigurationException | IOException | TransformerException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

}

