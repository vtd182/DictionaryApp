package org.example;

import org.example.Views.NavigationBarForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static class DictionaryApp {

        private Map<String, String> dictionary;

        public DictionaryApp() {
            dictionary = new HashMap<>();
        }

        public void loadDictionaryFromFile(String filePath) {
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
        }

        public String getMeaning(String word) {
            return dictionary.get(word);
        }
    }

    public static void main(String[] args) {
//        DictionaryApp dictionaryApp = new DictionaryApp();
//        dictionaryApp.loadDictionaryFromFile("Assets/Viet_Anh.xml");
//
//        // Ví dụ tra cứu từ "a"
//        String meaningOfA = dictionaryApp.getMeaning("a");
//        System.out.println("Meaning of 'a':");
//        System.out.println(meaningOfA);
        NavigationBarForm navigationBarForm = new NavigationBarForm();
    }
}