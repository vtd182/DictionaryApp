package org.example.Helper;

import org.example.Models.Word;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GlobalFunction {
    public static void setButtonIcon(JButton button, String iconPath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(GlobalFunction.class.getResource(iconPath));
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }

    public static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    public static void saveDictionaryToXML(Map<String, Word> dictionary, String filePath) {
        // Ghi dữ liệu từ HashMap vào file XML
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("dictionary");
            document.appendChild(rootElement);

            for (String word : dictionary.keySet()) {
                Element recordElement = document.createElement("record");
                rootElement.appendChild(recordElement);

                Element wordElement = document.createElement("word");
                wordElement.appendChild(document.createTextNode(word));
                recordElement.appendChild(wordElement);

                Element meaningElement = document.createElement("meaning");
                meaningElement.appendChild(document.createTextNode(dictionary.get(word).toString()));
                recordElement.appendChild(meaningElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<LocalDate, Map<String, Integer>> loadSearchFrequencyMapFromXML(String filePath, String mapName) {
        Map<LocalDate, Map<String, Integer>> searchFrequencyMap = new HashMap<>();

        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            // Lấy phần tử gốc dựa trên mapName
            NodeList nodeList = document.getElementsByTagName(mapName);

            if (nodeList.getLength() > 0) {
                Node rootElement = nodeList.item(0);

                NodeList dateList = ((Element) rootElement).getElementsByTagName("date");

                for (int i = 0; i < dateList.getLength(); i++) {
                    Node dateNode = dateList.item(i);

                    if (dateNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element dateElement = (Element) dateNode;
                        LocalDate date = LocalDate.parse(dateElement.getAttribute("date"));
                        Map<String, Integer> wordFrequencyMap = new HashMap<>();

                        NodeList wordList = dateElement.getElementsByTagName("word");

                        for (int j = 0; j < wordList.getLength(); j++) {
                            Node wordNode = wordList.item(j);

                            if (wordNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element wordElement = (Element) wordNode;
                                String word = wordElement.getAttribute("value");
                                int frequency = Integer.parseInt(wordElement.getAttribute("frequency"));
                                wordFrequencyMap.put(word, frequency);
                            }
                        }

                        searchFrequencyMap.put(date, wordFrequencyMap);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchFrequencyMap;
    }

    public static TreeMap<String, Word> loadDictionaryFromXML(String filePath) {
        TreeMap<String, Word> dictionary = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLHandler xmlHandler = new XMLHandler();
            saxParser.parse(new File(filePath), xmlHandler);
            dictionary = xmlHandler.getDictionary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    public static void saveSearchFrequencyMapToXML(Map<LocalDate, Map<String, Integer>> searchFrequencyMap, String filePath,
                                             String mapName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement(mapName);
            document.appendChild(rootElement);

            for (LocalDate date : searchFrequencyMap.keySet()) {
                Element dateElement = document.createElement("date");
                dateElement.setAttribute("date", date.toString());
                rootElement.appendChild(dateElement);

                Map<String, Integer> wordFrequencyMap = searchFrequencyMap.get(date);
                for (String word : wordFrequencyMap.keySet()) {
                    Element wordElement = document.createElement("word");
                    wordElement.setAttribute("value", word);
                    wordElement.setAttribute("frequency", String.valueOf(wordFrequencyMap.get(word)));
                    dateElement.appendChild(wordElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
