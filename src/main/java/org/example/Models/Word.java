package org.example.Models;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private String word;
    private String pronunciation;
    private List<WordType> wordTypes;

    public Word(String word) {
        this.word = word;
        wordTypes = new ArrayList<>();
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public void addWordType(WordType wordType) {
        wordTypes.add(wordType);
    }

    public List<WordType> getWordTypes() {
        return wordTypes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (pronunciation != null) {
            sb.append("Word: ").append(word).append("\n");
            sb.append("Pronunciation: ").append(pronunciation).append("\n");
        } else {
            sb.append("Word: ").append(word).append("\n");
        }
        for (WordType wordType : wordTypes) {
            sb.append("\tWordType: ").append(wordType.getType()).append("\n");
            for (Meaning meaning : wordType.getMeanings()) {
                sb.append("\t\tMeaning: ").append(meaning.getMeaning()).append("\n");
                for (SubMeaning subMeaning : meaning.getSubMeanings()) {
                    sb.append("\t\t\tSubMeaning: ").append(subMeaning.getSubMeaning()).append(" Definition: ").append(subMeaning.getDefinition()).append("\n");
                }
            }
        }
        return sb.toString();
    }

    public static Word parseFromString(String input) {
        String[] lines = input.split("\n");

        String wordLine = (lines[0].substring(1)).trim();
        String wordLineParts[] = wordLine.split("/");
        Word newWord = new Word(wordLine);

        if (wordLineParts.length == 2) {
            newWord.setWord(wordLineParts[0].trim());
            newWord.setPronunciation("/" + wordLineParts[1].trim() + "/");
        } else {
            newWord.setWord(wordLineParts[0].trim());
        }

        WordType wordType = null;
        Meaning meaning = null;

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("*")) {
                wordType = new WordType(line.substring(1).trim());
                newWord.addWordType(wordType);
            } else if (line.startsWith("-")) {
                if (wordType == null) {
                    wordType = new WordType("DEFAULT_TYPE");
                    newWord.addWordType(wordType);
                }
                meaning = new Meaning(line.substring(1).trim());
                wordType.addMeaning(meaning);
            } else if (line.startsWith("=")) {
                if (meaning == null) {
                    meaning = new Meaning("EMPTY_MEANING");
                    // Nếu wordtype rỗng, tạo một wordtype với giá trị mặc định
                    if (wordType == null) {
                        wordType = new WordType("DEFAULT_TYPE");
                        newWord.addWordType(wordType);
                    }
                    wordType.addMeaning(meaning);
                }
                String[] parts = line.substring(1).trim().split("\\+");
                if (parts.length != 2) {
                    SubMeaning subMeaning = new SubMeaning(parts[0], "");
                    meaning.addSubMeaning(subMeaning);
                } else {
                    SubMeaning subMeaning = new SubMeaning(parts[0], parts[1]);
                    meaning.addSubMeaning(subMeaning);
                }
            }
        }
        return newWord;
    }

    public String toHtmlString() {
        StringBuilder html = new StringBuilder("<html><body>");

        // Define font size, margin, and font family values
        int fontSizeHeading = 16;
        int fontSizeParagraph = 14;
        int marginLeft = 20;
        String fontFamily = "Arial, sans-serif";

        if (pronunciation != null) {
            html.append("<p style='font-size:" + fontSizeParagraph + "px; font-family: " + fontFamily + ";'>").append(pronunciation).append("</p>");
        }

        for (WordType wordType : wordTypes) {

            if (!wordType.getType().equals("DEFAULT_TYPE")) {
                html.append("<h2 style='font-size:" + fontSizeHeading + "px; font-family: " + fontFamily + ";'>").append(wordType.getType()).append("</h2>");
            } else {
                html.append("<h2 style='font-size:" + fontSizeHeading + "px; font-family: " + fontFamily + ";'>").append("Meaning").append("</h2>");
            }
            for (Meaning meaning : wordType.getMeanings()) {
                html.append("<p style='margin-left:" + marginLeft + "px;font-size:" + fontSizeParagraph + "px; font-family: " + fontFamily + ";'>").append("<strong>").append(meaning.getMeaning()).append("</strong>").append("</p>");

                for (SubMeaning subMeaning : meaning.getSubMeanings()) {
                    html.append("<p style='margin-left:" + marginLeft + "px;font-size:" + (fontSizeParagraph - 2) + "px; font-family: " + fontFamily + ";'>").append(subMeaning.getSubMeaning())
                            .append(": ")
                            .append("<i>")
                            .append(subMeaning.getDefinition()).append("</i>")
                            .append("</p>");
//                    html.append("<p style='margin-left:" + marginLeft + "px;font-size:" + (fontSizeParagraph - 2) + "px; font-family: " + fontFamily + ";'>").append("<i>")
//                            .append(subMeaning.getDefinition()).append("</i>")
//                            .append("</p>");
                }
            }
        }
        html.append("</body></html>");
        return html.toString();
    }

}