package org.example.Helper;

public class ConstantString {
    private static String patternForWordMeaning =
                    "*WordType A \n" +
                    "   -Meaning A.1\n" +
                    "       =Sub meaning A.1.1 + definition A.1.1 \n" +
                    "*WordType B \n" +
                    "   -Meaning B.1\n" +
                    "       =Sub meaning B.1.1 + definition B.1.1 \n";

    public static final String VIETNAMESE_TO_ENGLISH_FILE_PATH = "Assets/Viet_Anh.xml";
    public static final String ENGLISH_TO_VIETNAMESE_FILE_PATH = "Assets/Anh_Viet.xml";
    public static final String VIETNAMESE_TO_ENGLISH_FAVORITE_FILE_PATH = "Assets/Fav_Viet_Anh.xml";
    public static final String ENGLISH_TO_VIETNAMESE_FAVORITE_FILE_PATH = "Assets/Fav_Anh_Viet.xml";
    public static final String VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_FILE_PATH = "Assets/VietnameseToEnglishSearchFrequency.xml";
    public static final String ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_FILE_PATH = "Assets/EnglishToVietnameseSearchFrequency.xml";
    public static String PatternForWordMeaning() {
        return patternForWordMeaning;
    }


}
