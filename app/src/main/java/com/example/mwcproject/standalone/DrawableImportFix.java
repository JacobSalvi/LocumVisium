package com.example.mwcproject.standalone;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;
public class DrawableImportFix {

    public static void main(String[] args) {
        String rootDirectoryPath = "D:\\Github\\app\\src\\main\\res\\drawable";
        String replacementColor = "#FFFFFF"; // Replace with your desired color

        File rootDirectory = new File(rootDirectoryPath);
        processDirectoryRecursively(rootDirectory, replacementColor);
    }

    private static void processDirectoryRecursively(File directory, String replacementColor) {
        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory != null) {
            for (File fileOrDirectory : filesInDirectory) {
                if (fileOrDirectory.isFile() && fileOrDirectory.getName().endsWith(".xml")) {
                    try {
                        updateXmlFileColor(fileOrDirectory, replacementColor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (fileOrDirectory.isDirectory()) {
                    processDirectoryRecursively(fileOrDirectory, replacementColor); // Recursive call for subdirectories
                }
            }
        } else {
            System.out.println("Invalid directory: " + directory.getAbsolutePath());
        }
    }

    private static void updateXmlFileColor(File xmlFile, String replacementColor) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(xmlFile);
        System.out.println("Updating file: " + xmlFile.getAbsolutePath());
        document.getDocumentElement().normalize();

        NodeList allNodes = document.getElementsByTagName("*");
        for (int i = 0; i < allNodes.getLength(); i++) {
            Node currentNode = allNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentNode;
                if (currentElement.hasAttribute("android:fillColor")) {
                    String existingColor = currentElement.getAttribute("android:fillColor");
                    if ("currentColor".equals(existingColor)) {
                        currentElement.setAttribute("android:fillColor", replacementColor);
                    }
                }
            }
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }
}


