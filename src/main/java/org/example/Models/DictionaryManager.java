package org.example.Models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class DictionaryManager {
    private Map<String, String> vietnameseToEnglishDictionary;
    private Map<String, String> englishToVietnameseDictionary;
    public boolean isVietnameseToEnglishMode;

    public Map<String, String> getVietnameseToEnglishDictionary() {
        return vietnameseToEnglishDictionary;
    }

    public Map<String, String> getEnglishToVietnameseDictionary() {
        return englishToVietnameseDictionary;
    }

    public DictionaryManager() {
        this.vietnameseToEnglishDictionary = new TreeMap<>();
        this.englishToVietnameseDictionary = new TreeMap<>();
        this.isVietnameseToEnglishMode = true; // Mặc định chế độ từ Việt-Anh
    }

    public void loadDictionariesFromXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        vietnameseToEnglishDictionary = loadDictionaryFromXML(vietnameseToEnglishFilePath);
        englishToVietnameseDictionary = loadDictionaryFromXML(englishToVietnameseFilePath);
    }

    private TreeMap<String, String> loadDictionaryFromXML(String filePath) {
        // Đọc dữ liệu từ file XML và trả về một HashMap
        TreeMap<String, String> dictionary = new TreeMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            NodeList recordList = document.getElementsByTagName("record");

            for (int i = 0; i < recordList.getLength(); i++) {
                Node recordNode = recordList.item(i);

                if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element recordElement = (Element) recordNode;

                    String word = recordElement.getElementsByTagName("word").item(0).getTextContent();

                    NodeList meaningList = recordElement.getElementsByTagName("meaning");

                    StringBuilder meanings = new StringBuilder();
                    for (int j = 0; j < meaningList.getLength(); j++) {
                        Node meaningNode = meaningList.item(j);

                        if (meaningNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element meaningElement = (Element) meaningNode;
                            meanings.append(meaningElement.getTextContent()).append("\n");
                        }
                    }

                    dictionary.put(word, meanings.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    public void switchMode() {
        isVietnameseToEnglishMode = !isVietnameseToEnglishMode;
    }

    public String getMeaning(String word) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishDictionary.get(word);
        } else {
            return englishToVietnameseDictionary.get(word);
        }
    }
}

