package org.example.Models;

import org.example.Helper.XMLHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class DictionaryManager {

    private static DictionaryManager instance = null;
    private Map<String, Word> vietnameseToEnglishDictionary;
    private Map<String, Word> englishToVietnameseDictionary;
    private Map<String, Word> vietnameseToEnglishFavoriteWords;
    private Map<String, Word> englishToVietnameseFavoriteWords;
    public boolean isVietnameseToEnglishMode;
    private boolean isFavoriteMode;

    public void setIsFavoriteMode(boolean isFavoriteMode) {
        this.isFavoriteMode = isFavoriteMode;
    }

    public Map<String, Word> getVietnameseToEnglishDictionary() {
        return vietnameseToEnglishDictionary;
    }

    public Map<String, Word> getEnglishToVietnameseDictionary() {
        return englishToVietnameseDictionary;
    }

    public Map<String, Word> getVietnameseToEnglishFavoriteWords() {
        return vietnameseToEnglishFavoriteWords;
    }

    public Map<String, Word> getEnglishToVietnameseFavoriteWords() {
        return englishToVietnameseFavoriteWords;
    }

    public static DictionaryManager getInstance() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }
    private DictionaryManager() {
        System.out.println("DictionaryManager constructor");
        this.vietnameseToEnglishDictionary = new TreeMap<>();
        this.englishToVietnameseDictionary = new TreeMap<>();
        this.vietnameseToEnglishFavoriteWords = new TreeMap<>();
        this.englishToVietnameseFavoriteWords = new TreeMap<>();
        this.isVietnameseToEnglishMode = true; // Mặc định chế độ từ Việt-Anh
        this.isFavoriteMode = false;
    }

    public void loadDictionariesFromXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        vietnameseToEnglishDictionary = loadDictionaryFromXML(vietnameseToEnglishFilePath);
        System.out.println("á" + vietnameseToEnglishDictionary.get("á"));
        englishToVietnameseDictionary = loadDictionaryFromXML(englishToVietnameseFilePath);
    }

    public void loadFavoriteWordsFromXML(String vietnameseToEnglishFavoriteFilePath, String englishToVietnameseFavoriteFilePath) {
        vietnameseToEnglishFavoriteWords = loadDictionaryFromXML(vietnameseToEnglishFavoriteFilePath);
        englishToVietnameseFavoriteWords = loadDictionaryFromXML(englishToVietnameseFavoriteFilePath);
        System.out.println("Load favorite words from XML");
    }

//    private TreeMap<String, Word> loadDictionaryFromXML(String filePath) {
//        // Đọc dữ liệu từ file XML và trả về một HashMap
//        TreeMap<String, Word> dictionary = new TreeMap<>();
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(new File(filePath));
//
//            NodeList recordList = document.getElementsByTagName("record");
//
//            for (int i = 0; i < recordList.getLength(); i++) {
//                Node recordNode = recordList.item(i);
//
//                if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element recordElement = (Element) recordNode;
//
//                    String word = recordElement.getElementsByTagName("word").item(0).getTextContent();
//
//                    NodeList meaningList = recordElement.getElementsByTagName("meaning");
//
//                    StringBuilder meanings = new StringBuilder();
//                    for (int j = 0; j < meaningList.getLength(); j++) {
//                        Node meaningNode = meaningList.item(j);
//
//                        if (meaningNode.getNodeType() == Node.ELEMENT_NODE) {
//                            Element meaningElement = (Element) meaningNode;
//                            meanings.append(meaningElement.getTextContent()).append("\n");
//                        }
//                    }
//                    try {
//                        dictionary.put(word, Word.parseFromString(meanings.toString()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("Error while parsing word: " + word);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dictionary;
//    }

    private TreeMap<String, Word> loadDictionaryFromXML(String filePath) {
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
//        System.out.println("From " + filePath + " " + dictionary.size() + " words loaded.");
//        System.out.println(dictionary.get("á"));
        return dictionary;
    }


    public void switchMode() {
        isVietnameseToEnglishMode = !isVietnameseToEnglishMode;
    }

    public String getMeaning(String word) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishDictionary.get(word).toString();
        } else {
            return englishToVietnameseDictionary.get(word).toString();
        }
    }

    public String getHtmlMeaning(String word) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishDictionary.get(word).toHtmlString();
        } else {
            return englishToVietnameseDictionary.get(word).toHtmlString();
        }
    }

    public TreeSet<String> search(String keyword) {
        TreeSet<String> relatedWords = new TreeSet<>();

        System.out.println("Searching for: " + keyword);
        if (isVietnameseToEnglishMode) {
            for (String word : vietnameseToEnglishDictionary.keySet()) {
                if (word.contains(keyword)) {
                    relatedWords.add(word);
                }
            }
        } else {
            for (String word : englishToVietnameseDictionary.keySet()) {
                if (word.contains(keyword)) {
                    relatedWords.add(word);
                }
            }
        }

        return relatedWords;
    }

    public String suggest(String keyword) {
        String suggestedWord = null;
        int minDistance = Integer.MAX_VALUE;

        if (isVietnameseToEnglishMode) {
            var keySet = isFavoriteMode? vietnameseToEnglishFavoriteWords.keySet() : vietnameseToEnglishDictionary.keySet();
            for (String word : keySet) {
                int distance = levenshteinDistance(keyword, word);
                if (distance < minDistance) {
                    minDistance = distance;
                    suggestedWord = word;
                }
            }
        } else {
            var keySet = isFavoriteMode? englishToVietnameseFavoriteWords.keySet() : englishToVietnameseDictionary.keySet();
            for (String word : keySet) {
                int distance = levenshteinDistance(keyword, word);
                if (distance < minDistance) {
                    minDistance = distance;
                    suggestedWord = word;
                }
            }
        }

        return suggestedWord;
    }

    private int levenshteinDistance(String s1, String s2) {
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

    public boolean isFavoriteWord(String word) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishFavoriteWords.containsKey(word);
        } else {
            return englishToVietnameseFavoriteWords.containsKey(word);
        }
    }

    public void addFavoriteWord(String word, Word meaning) {
        if (isVietnameseToEnglishMode) {
            vietnameseToEnglishFavoriteWords.put(word, meaning);
        } else {
            englishToVietnameseFavoriteWords.put(word, meaning);
        }
    }

    public void removeFavoriteWord(String word) {
        if (isVietnameseToEnglishMode) {
            vietnameseToEnglishFavoriteWords.remove(word);
        } else {
            englishToVietnameseFavoriteWords.remove(word);
        }
    }

    public Word getWord(String word) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishDictionary.get(word);
        } else {
            return englishToVietnameseDictionary.get(word);
        }
    }

    public void saveFavoriteWordsToXML(String vietnameseToEnglishFavoriteFilePath, String englishToVietnameseFavoriteFilePath) {
        saveDictionaryToXML(vietnameseToEnglishFavoriteWords, vietnameseToEnglishFavoriteFilePath);
        saveDictionaryToXML(englishToVietnameseFavoriteWords, englishToVietnameseFavoriteFilePath);
    }

    private void saveDictionaryToXML(Map<String, Word> dictionary, String filePath) {
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

    public void deleteWord(String word) {
        if (isVietnameseToEnglishMode) {
            vietnameseToEnglishDictionary.remove(word);
            vietnameseToEnglishFavoriteWords.remove(word);
        } else {
            englishToVietnameseDictionary.remove(word);
            englishToVietnameseFavoriteWords.remove(word);
        }
    }
}

