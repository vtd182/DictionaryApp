package org.example.Models;

import org.example.Helper.ConstantString;
import org.example.Helper.GlobalFunction;

import java.time.LocalDate;
import java.util.*;

import static org.example.Helper.GlobalFunction.levenshteinDistance;
import static org.example.Helper.GlobalFunction.loadDictionaryFromXML;

public class DictionaryManager {
    private static DictionaryManager instance = null;
    private Map<String, Word> vietnameseToEnglishDictionary;
    private Map<String, Word> englishToVietnameseDictionary;
    private Map<String, Word> vietnameseToEnglishFavoriteWords;
    private Map<String, Word> englishToVietnameseFavoriteWords;
    private Map<LocalDate, Map<String, Integer>> vietnameseToEnglishSearchFrequencyMap;
    private Map<LocalDate, Map<String, Integer>> englishToVietnameseSearchFrequencyMap;

    // Error list
    // 1: Can not load data for vietnameseToEnglishDictionary
    // 2: Can not load data for englishToVietnameseDictionary
    // 3: Can not load data for vietnameseToEnglishFavoriteWords
    // 4: Can not load data for englishToVietnameseFavoriteWords
    // 5: Can not load data for vietnameseToEnglishSearchFrequencyMap
    // 6: Can not load data for englishToVietnameseSearchFrequencyMap
    public static final int ERROR_VIETNAMESE_TO_ENGLISH_DICTIONARY = 1;
    public static final int ERROR_ENGLISH_TO_VIETNAMESE_DICTIONARY = 2;
    public static final int ERROR_VIETNAMESE_TO_ENGLISH_FAVORITE_WORDS = 3;
    public static final int ERROR_ENGLISH_TO_VIETNAMESE_FAVORITE_WORDS = 4;
    public static final int ERROR_VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_MAP = 5;
    public static final int ERROR_ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_MAP = 6;
    private final Vector<Integer> errorList = new Vector<>();


    public Vector<Integer> getErrorList() {
        return errorList;
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
        this.vietnameseToEnglishSearchFrequencyMap = new HashMap<>();
        this.englishToVietnameseSearchFrequencyMap = new HashMap<>();
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
        if (vietnameseToEnglishDictionary == null) {
            errorList.add(1);
        }
        if (englishToVietnameseDictionary == null) {
            errorList.add(2);
        }
    }

    public void loadFavoriteWordsFromXML(String vietnameseToEnglishFavoriteFilePath,
                                         String englishToVietnameseFavoriteFilePath) {
        vietnameseToEnglishFavoriteWords = loadDictionaryFromXML(vietnameseToEnglishFavoriteFilePath);
        englishToVietnameseFavoriteWords = loadDictionaryFromXML(englishToVietnameseFavoriteFilePath);
        if (vietnameseToEnglishFavoriteWords == null) {
            errorList.add(3);
        }
        if (englishToVietnameseFavoriteWords == null) {
            errorList.add(4);
        }
    }

    public void saveSearchFrequencyToXML(String vietnameseToEnglishSearchFrequencyFilePath,
                                         String englishToVietnameseSearchFrequencyFilePath) {
        GlobalFunction.saveSearchFrequencyMapToXML(vietnameseToEnglishSearchFrequencyMap, vietnameseToEnglishSearchFrequencyFilePath,
                "VietnameseToEnglishSearchFrequency");
        GlobalFunction.saveSearchFrequencyMapToXML(englishToVietnameseSearchFrequencyMap, englishToVietnameseSearchFrequencyFilePath,
                "EnglishToVietnameseSearchFrequency");
    }

    public String getHtmlMeaning(String word, boolean isVietnameseToEnglishMode) {
        increaseSearchFrequency(word, isVietnameseToEnglishMode);
        saveSearchFrequencyToXML(ConstantString.VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_FILE_PATH,
                ConstantString.ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_FILE_PATH);
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishDictionary.get(word).toHtmlString();
        } else {
            return englishToVietnameseDictionary.get(word).toHtmlString();
        }
    }

    public TreeSet<String> search(String keyword, boolean isVietnameseToEnglishMode, boolean isFavoriteMode) {
        System.out.println("Searching in favorite mode: " + isFavoriteMode);
        TreeSet<String> relatedWords = new TreeSet<>();
        Set<String> keySet;

        if (isFavoriteMode) {
            keySet = isVietnameseToEnglishMode ? vietnameseToEnglishFavoriteWords.keySet() : englishToVietnameseFavoriteWords.keySet();
        } else {
            keySet = isVietnameseToEnglishMode ? vietnameseToEnglishDictionary.keySet() : englishToVietnameseDictionary.keySet();
        }

        for (String word : keySet) {
            if (word.contains(keyword)) {
                relatedWords.add(word);
            }
        }

        return relatedWords;
    }

    public String suggest(String keyword, boolean isVietnameseToEnglishMode, boolean isFavoriteMode) {
        String suggestedWord = null;
        int minDistance = Integer.MAX_VALUE;
        Set<String> keySet;

        if (isFavoriteMode) {
            keySet = isVietnameseToEnglishMode ? vietnameseToEnglishFavoriteWords.keySet() : englishToVietnameseFavoriteWords.keySet();
        } else {
            keySet = isVietnameseToEnglishMode ? vietnameseToEnglishDictionary.keySet() : englishToVietnameseDictionary.keySet();
        }


        for (String word : keySet) {
            int distance = levenshteinDistance(keyword, word);
            if (distance < minDistance) {
                minDistance = distance;
                suggestedWord = word;
            }
        }
        return suggestedWord;
    }

    public boolean isFavoriteWord(String word, boolean isVietnameseToEnglishMode) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishFavoriteWords.containsKey(word);
        } else {
            return englishToVietnameseFavoriteWords.containsKey(word);
        }
    }

    public void addFavoriteWord(String word, Word meaning, boolean isVietnameseToEnglishMode) {
        if (isVietnameseToEnglishMode) {
            vietnameseToEnglishFavoriteWords.put(word, meaning);
        } else {
            englishToVietnameseFavoriteWords.put(word, meaning);
        }
    }

    public void removeFavoriteWord(String word, boolean isVietnameseToEnglishMode) {
        if (isVietnameseToEnglishMode) {
            vietnameseToEnglishFavoriteWords.remove(word);
        } else {
            englishToVietnameseFavoriteWords.remove(word);
        }
    }

    public Word getWord(String word, boolean isVietnameseToEnglishMode) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishDictionary.get(word);
        } else {
            return englishToVietnameseDictionary.get(word);
        }
    }

    public void saveFavoriteWordsToXML(String vietnameseToEnglishFavoriteFilePath, String englishToVietnameseFavoriteFilePath) {
        GlobalFunction.saveDictionaryToXML(vietnameseToEnglishFavoriteWords, vietnameseToEnglishFavoriteFilePath);
        GlobalFunction.saveDictionaryToXML(englishToVietnameseFavoriteWords, englishToVietnameseFavoriteFilePath);
    }

    public void saveDictionaryToXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        GlobalFunction.saveDictionaryToXML(vietnameseToEnglishDictionary, vietnameseToEnglishFilePath);
        GlobalFunction.saveDictionaryToXML(englishToVietnameseDictionary, englishToVietnameseFilePath);
    }

    public void deleteWord(String word, boolean isVietnameseToEnglishMode) {
        if (isVietnameseToEnglishMode) {
            vietnameseToEnglishDictionary.remove(word);
            vietnameseToEnglishFavoriteWords.remove(word);
        } else {
            englishToVietnameseDictionary.remove(word);
            englishToVietnameseFavoriteWords.remove(word);
        }
    }

    private void increaseSearchFrequency(String word, boolean isVietnameseToEnglishMode) {
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
            vietnameseToEnglishSearchFrequencyMap.get(currentDate)
                    .put(word, vietnameseToEnglishSearchFrequencyMap.get(currentDate).get(word) + 1);
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
            englishToVietnameseSearchFrequencyMap.get(currentDate)
                    .put(word, englishToVietnameseSearchFrequencyMap.get(currentDate).get(word) + 1);
        }
    }

    public void loadSearchFrequencyFromXML(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        System.out.println("Loading search frequency data");
        englishToVietnameseSearchFrequencyMap = GlobalFunction.loadSearchFrequencyMapFromXML(englishToVietnameseFilePath,
                "EnglishToVietnameseSearchFrequency");
        vietnameseToEnglishSearchFrequencyMap = GlobalFunction.loadSearchFrequencyMapFromXML(vietnameseToEnglishFilePath,
                "VietnameseToEnglishSearchFrequency");
        if (vietnameseToEnglishSearchFrequencyMap.keySet().isEmpty()) {
            System.out.println("Can not load data for vietnameseToEnglishSearchFrequencyMap");
            errorList.add(5);
        }
        if (englishToVietnameseSearchFrequencyMap.keySet().isEmpty()) {
            System.out.println("Can not load data for englishToVietnameseSearchFrequencyMap");
            errorList.add(6);
        }
    }

    public Map<String, Word> getFavoriteWords(boolean isVietnameseToEnglishMode) {
        if (isVietnameseToEnglishMode) {
            return vietnameseToEnglishFavoriteWords;
        } else {
            return englishToVietnameseFavoriteWords;
        }
    }
}
