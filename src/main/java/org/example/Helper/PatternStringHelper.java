package org.example.Helper;

public class PatternStringHelper {
    private static String patternForWordMeaning =
                    "*WordType A \n" +
                    "   -Meaning A.1\n" +
                    "       =Sub meaning A.1.1 + definition A.1.1 \n" +
                    "*WordType B \n" +
                    "   -Meaning B.1\n" +
                    "       =Sub meaning B.1.1 + definition B.1.1 \n";


    public static String PatternForWordMeaning() {
        return patternForWordMeaning;
    }
}
