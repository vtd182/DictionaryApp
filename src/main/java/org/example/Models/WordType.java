package org.example.Models;

import java.util.ArrayList;
import java.util.List;

public class WordType {
    private String type;
    private List<Meaning> meanings;

    public WordType(String type) {
        this.type = type;
        meanings = new ArrayList<>();
    }

    public void addMeaning(Meaning meaning) {
        meanings.add(meaning);
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public String getType() {
        return type;
    }
}