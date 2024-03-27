package org.example.Models;

import java.util.ArrayList;
import java.util.List;

public class Meaning {
    private String meaning;
    private final List<SubMeaning> subMeanings;

    public Meaning(String meaning) {
        this.meaning = meaning;
        subMeanings = new ArrayList<>();
    }

    public void addSubMeaning(SubMeaning subMeaning) {
        subMeanings.add(subMeaning);
    }

    public List<SubMeaning> getSubMeanings() {
        return subMeanings;
    }
    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}