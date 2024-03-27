package org.example.Helper;

public class ConstantString {
    public static String PATTERN_FOR_WORD_MEANING =
                    "*WordType A \n" +
                    "   -Meaning A.1\n" +
                    "       =Sub meaning A.1.1 + definition A.1.1 \n" +
                    "*WordType B \n" +
                    "   -Meaning B.1\n" +
                    "       =Sub meaning B.1.1 + definition B.1.1 \n";

    public static final String ERROR_GET_WORD_MESSAGE_HTML = "<html>\n" +
            "<body>\n" +
            "    <p style='color:#FF0000; text-align:center; font-size:15px; font-family:Arial, sans-serif;'" +
            ">CAN NOT FIND THE MEANING OF THIS WORD<" +
            "/p>\n" +
            "</body>\n" +
            "</html>\n";

    public static final String VIETNAMESE_TO_ENGLISH_FILE_PATH = "Data/Viet_Anh.xml";
    public static final String ENGLISH_TO_VIETNAMESE_FILE_PATH = "Data/Anh_Viet.xml";
    public static final String VIETNAMESE_TO_ENGLISH_FAVORITE_FILE_PATH = "Data/Fav_Viet_Anh.xml";
    public static final String ENGLISH_TO_VIETNAMESE_FAVORITE_FILE_PATH = "Data/Fav_Viet_Anh.xml";
    public static final String VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_FILE_PATH = "Data/Frequency_Viet_Anh.xml";
    public static final String ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_FILE_PATH = "Data/Frequency_Anh_Viet.xml";
    public static final String IC_STAR = "/assets/un_star.png";
    public static final String IC_STAR_FILL = "/assets/star.png";
    public static final String IC_SEND = "/assets/send.png";
    public static final String IC_SAVE = "/assets/save.png";
    public static final String IC_EDIT = "/assets/edit.png";
    public static final String IC_DELETE = "/assets/delete.png";
    public static final String IC_VIETNAMESE_TO_ENGLISH = "/assets/ic1.png";
    public static final String IC_ENGLISH_TO_VIETNAMESE = "/assets/ic2.png";
}
