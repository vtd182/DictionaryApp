package org.example.Models;

import org.example.Helper.ConstantString;
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
import java.time.LocalDate;
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
    private Map<LocalDate, Map<String, Integer>> vietnameseToEnglishSearchFrequencyMap;
    private Map<LocalDate, Map<String, Integer>> englishToVietnameseSearchFrequencyMap;
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
    public Map<LocalDate, Map<String, Integer>> getVietnameseToEnglishSearchFrequencyMap() {
        return vietnameseToEnglishSearchFrequencyMap;
    }
    public Map<LocalDate, Map<String, Integer>> getEnglishToVietnameseSearchFrequencyMap() {
        return englishToVietnameseSearchFrequencyMap;
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
        loadData();
    }

    public void loadData() {
        loadSearchFrequencyFromXML(ConstantString.VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_FILE_PATH,
                ConstantString.ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_FILE_PATH);
        loadDictionariesFromXML(ConstantString.VIETNAMESE_TO_ENGLISH_FILE_PATH,
                ConstantString.ENGLISH_TO_VIETNAMESE_FILE_PATH);
        loadFavoriteWordsFromXML(ConstantString.VIETNAMESE_TO_ENGLISH_FAVORITE_FILE_PATH,
                ConstantString.ENGLISH_TO_VIETNAMESE_FAVORITE_FILE_PATH);
    }

    public void loadDictionariesFromXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        vietnameseToEnglishDictionary = loadDictionaryFromXML(vietnameseToEnglishFilePath);
        englishToVietnameseDictionary = loadDictionaryFromXML(englishToVietnameseFilePath);
    }

    public void loadFavoriteWordsFromXML(String vietnameseToEnglishFavoriteFilePath,
                                         String englishToVietnameseFavoriteFilePath) {
        vietnameseToEnglishFavoriteWords = loadDictionaryFromXML(vietnameseToEnglishFavoriteFilePath);
        englishToVietnameseFavoriteWords = loadDictionaryFromXML(englishToVietnameseFavoriteFilePath);
        System.out.println("Load favorite words from XML");
    }

    public void saveSearchFrequencyToXML(String vietnameseToEnglishSearchFrequencyFilePath,
                                         String englishToVietnameseSearchFrequencyFilePath) {
        saveSearchFrequencyMapToXML(vietnameseToEnglishSearchFrequencyMap, vietnameseToEnglishSearchFrequencyFilePath,
                "VietnameseToEnglishSearchFrequency");
        saveSearchFrequencyMapToXML(englishToVietnameseSearchFrequencyMap, englishToVietnameseSearchFrequencyFilePath,
                "EnglishToVietnameseSearchFrequency");
    }

    private void saveSearchFrequencyMapToXML(Map<LocalDate, Map<String, Integer>> searchFrequencyMap, String filePath,
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

    public String getHtmlMeaning(String word, boolean isVietnameseToEnglishMode) {
        increaseSearchFrequency(word);
        saveSearchFrequencyToXML(ConstantString.VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_FILE_PATH,
                ConstantString.ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_FILE_PATH);
        System.out.println("Get meaning of: " + word);
        System.out.println("isVietnameseToEnglishMode: " + isVietnameseToEnglishMode);
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

    public String suggest(String keyword, boolean isVietnameseToEnglishMode) {
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

    public boolean isFavoriteWord(String word, boolean isVietnameseToEnglishMode) {
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

    public void saveDictionaryToXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        saveDictionaryToXML(vietnameseToEnglishDictionary, vietnameseToEnglishFilePath);
        saveDictionaryToXML(englishToVietnameseDictionary, englishToVietnameseFilePath);
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

    private void increaseSearchFrequency(String word) {
        LocalDate currentDate = LocalDate.now();
        if (isVietnameseToEnglishMode) {
            if (vietnameseToEnglishSearchFrequencyMap == null) {
                vietnameseToEnglishSearchFrequencyMap = new HashMap<>();
            }
            if (!vietnameseToEnglishSearchFrequencyMap.containsKey(currentDate)) {
                vietnameseToEnglishSearchFrequencyMap.put(currentDate, new HashMap<>());
            }
            if (!vietnameseToEnglishSearchFrequencyMap.get(currentDate).containsKey(word)) {
                vietnameseToEnglishSearchFrequencyMap.get(currentDate).put(word, 0);
            }
            vietnameseToEnglishSearchFrequencyMap.get(currentDate).put(word, vietnameseToEnglishSearchFrequencyMap.get(currentDate).get(word) + 1);
        } else {
            if (englishToVietnameseSearchFrequencyMap == null) {
                englishToVietnameseSearchFrequencyMap = new HashMap<>();
            }
            if (!englishToVietnameseSearchFrequencyMap.containsKey(currentDate)) {
                englishToVietnameseSearchFrequencyMap.put(currentDate, new HashMap<>());
            }
            if (!englishToVietnameseSearchFrequencyMap.get(currentDate).containsKey(word)) {
                englishToVietnameseSearchFrequencyMap.get(currentDate).put(word, 0);
            }
            englishToVietnameseSearchFrequencyMap.get(currentDate).put(word, englishToVietnameseSearchFrequencyMap.get(currentDate).get(word) + 1);
        }
    }

    public Map<LocalDate, Map<String, Integer>> loadSearchFrequencyMapFromXML(String filePath, String mapName) {
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

    public void loadSearchFrequencyFromXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        englishToVietnameseSearchFrequencyMap = loadSearchFrequencyMapFromXML(englishToVietnameseFilePath,
                "EnglishToVietnameseSearchFrequency");
        vietnameseToEnglishSearchFrequencyMap = loadSearchFrequencyMapFromXML(vietnameseToEnglishFilePath,
                "VietnameseToEnglishSearchFrequency");
    }

    public Map<String, Word> getFavoriteWords(boolean isVietnameseToEnglishMode) {
        System.out.println("Get favorite words");
        System.out.println("isVietnameseToEnglishMode: " + isVietnameseToEnglishMode);
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishFavoriteWords;
        } else {
            return englishToVietnameseFavoriteWords;
        }
    }
}

