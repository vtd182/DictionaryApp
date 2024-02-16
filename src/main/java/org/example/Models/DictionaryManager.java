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

// todo: Map thêm thuộc tính isFavorite cho từng từ, và thêm phương thức để thêm/xóa từ vào danh sách favorite
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
            for (String word : vietnameseToEnglishDictionary.keySet()) {
                int distance = levenshteinDistance(keyword, word);
                if (distance < minDistance) {
                    minDistance = distance;
                    suggestedWord = word;
                }
            }
        } else {
            for (String word : englishToVietnameseDictionary.keySet()) {
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
}

